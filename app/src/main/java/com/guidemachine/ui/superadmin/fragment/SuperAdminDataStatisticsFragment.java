package com.guidemachine.ui.superadmin.fragment;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.guidemachine.R;
import com.guidemachine.base.adapter.BaseRecyclerAdapter;
import com.guidemachine.base.adapter.BaseViewHolder;
import com.guidemachine.base.ui.BaseFragment;
import com.guidemachine.service.entity.AdminDeviceRentNumBean;
import com.guidemachine.service.entity.AdminStatisticsBean;
import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.presenter.AdminDeviceRentNumPresenter;
import com.guidemachine.service.presenter.SuperAdminStatisticsPresenter;
import com.guidemachine.service.view.AdminDeviceRentNumView;
import com.guidemachine.service.view.AdminStatisticsView;
import com.guidemachine.ui.activity.MapActivity;
import com.guidemachine.ui.superadmin.chart.manager.LineChartManager;
import com.guidemachine.ui.superadmin.chart.utils.LocalJsonAnalyzeUtil;
import com.guidemachine.ui.superadmin.data.CompositeIndexBean;
import com.guidemachine.ui.superadmin.data.IncomeBean;
import com.guidemachine.ui.superadmin.data.LineChartBean;
import com.guidemachine.util.IntentUtils;
import com.guidemachine.util.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * @author ChenLinWang
 * @email 422828518@qq.com
 * @create 2018/12/13 0013 14:07
 * description: 超级管理员 数据统计页面
 */
public class SuperAdminDataStatisticsFragment extends BaseFragment implements CalendarViewFragment.OnFrgDataListener {

    @BindView(R.id.rl_setting)
    RelativeLayout rlSetting;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.rl_right)
    RelativeLayout rlRight;
    @BindView(R.id.rl_title)
    LinearLayout rlTitle;
    Callback callback;
    @BindView(R.id.lineChart)
    LineChart lineChart1;
    @BindView(R.id.view_mine)
    View viewMine;
    @BindView(R.id.view_shanghai)
    TextView viewShanghai;
    @BindView(R.id.cl_shanghai)
    ConstraintLayout clShanghai;
    Unbinder unbinder;
    @BindView(R.id.tv_start_time)
    TextView tvStartTime;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;
    @BindView(R.id.pie_chart)
    PieChart pieChart;
    @BindView(R.id.tv_device_online)
    TextView tvDeviceOnline;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.view_shenzhen)
    View viewShenzhen;
    @BindView(R.id.cl_shenzhen)
    ConstraintLayout clShenzhen;
    @BindView(R.id.cl_fold_line)
    ConstraintLayout clFoldLine;
    @BindView(R.id.tv_device_rent)
    TextView tvDeviceRent;
    @BindView(R.id.tv_start_time_title)
    TextView tvStartTimeTitle;
    @BindView(R.id.tv_end_time_title)
    TextView tvEndTimeTitle;
    @BindView(R.id.ll_pie_chart)
    LinearLayout llPieChart;
    @BindView(R.id.view_yesterday)
    TextView viewYesterday;
    @BindView(R.id.cs_yesterday)
    ConstraintLayout csYesterday;
    @BindView(R.id.view_today)
    TextView viewToday;
    @BindView(R.id.tv_total_num)
    TextView tvTotalNum;
    @BindView(R.id.ry_scene_rent_num)
    RecyclerView rySceneRentNum;

    private LineChartBean lineChartBean;
    private List<IncomeBean> incomeBeanList;//个人收益
    private List<CompositeIndexBean> shanghai;//沪市指数
    private List<CompositeIndexBean> shenzhen;//沪市指数

    private LineChartManager lineChartManager1;
    //弹出的时间弹窗
    CalendarViewFragment calendarViewFragment;
    //哪个时间的标志位  1：在线设备数的时间标志位
    //2:设备租赁数的开始时间
    //3：设备租赁数的结束时间
    private int timeFlag = 1;
    //超级管理员统计图表
    SuperAdminStatisticsPresenter superAdminStatisticsPresenter;
    private List<AdminStatisticsBean> mAdminStatisticsList;
    //超级管理员统计设备租赁次数
    AdminDeviceRentNumPresenter adminDeviceRentNumPresenter;
    BaseRecyclerAdapter adapter;

    @Override
    protected int setRootViewId() {
        return R.layout.fragment_super_admin_data_statistics;
    }


    @Override
    protected void initView(View view, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        calendarViewFragment = new CalendarViewFragment();
        calendarViewFragment.setIListener(SuperAdminDataStatisticsFragment.this);
        superAdminStatisticsPresenter = new SuperAdminStatisticsPresenter(getContext());
        superAdminStatisticsPresenter.onCreate();
        superAdminStatisticsPresenter.attachView(adminStatisticsView);

        adminDeviceRentNumPresenter = new AdminDeviceRentNumPresenter(getContext());
        adminDeviceRentNumPresenter.onCreate();
        adminDeviceRentNumPresenter.attachView(adminDeviceRentNumView);
        tvTime.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()).toString());
        tvStartTime.setText(new SimpleDateFormat("yyyy-MM-dd").format(getNextDay(new Date())).toString());
        tvEndTime.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()).toString());
        initDeviceRentNum("", new SimpleDateFormat("yyyy-MM-dd").format(getNextDay(new Date())),
                new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        //请求数据统计折线图接口
        initStatisticsData(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        rlSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callback != null) {
                    callback.drawerOpen();
                }
            }
        });
        rlRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.openActivity(getActivity(), MapActivity.class);
                getActivity().finish();
            }
        });
//        initData();

//        initPie();
        tvTime.setOnClickListener(new View.OnClickListener() {//日期选择
            @Override
            public void onClick(View view) {
                timeFlag = 1;
                calendarViewFragment.show(getActivity().getSupportFragmentManager(), "timeChoose");
            }
        });
        tvStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeFlag = 2;
                calendarViewFragment.show(getActivity().getSupportFragmentManager(), "timeChoose");
            }
        });
        tvEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeFlag = 3;
                calendarViewFragment.show(getActivity().getSupportFragmentManager(), "timeChoose");
            }
        });

    }

    public void initStatisticsData(String date) {
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("date", date);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestData.toString());
        superAdminStatisticsPresenter.selectStatisticsAdmin(requestBody);
    }

    public void initDeviceRentNum(String name, String startTime, String endTime) {
        showProgressDialog();
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("sceneryName", name);
            requestData.put("startTime", startTime);
            requestData.put("endTime", endTime);
            requestData.put("pageNum", 1);
            requestData.put("pageSize", 10);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestData.toString());
        adminDeviceRentNumPresenter.selectStatisticsRentNumAdmin(requestBody);
    }

    AdminStatisticsView adminStatisticsView = new AdminStatisticsView() {
        @Override
        public void onSuccess(BaseBean<List<AdminStatisticsBean>> mStatisticsBeanList) {
            dismissProgressDialog();
            Logger.d("mStatisticsBeanList", mStatisticsBeanList.getValue().toString());
            mAdminStatisticsList = mStatisticsBeanList.getValue();
            initView();
        }

        @Override
        public void onError(String result) {
            dismissProgressDialog();
        }
    };
    AdminDeviceRentNumView adminDeviceRentNumView = new AdminDeviceRentNumView() {
        @Override
        public void onSuccess(BaseBean<AdminDeviceRentNumBean> bean) {
            dismissProgressDialog();
            Logger.d("AdminDeviceRentNumBean", bean.getValue().toString());
            tvTotalNum.setText(bean.getValue().getTotalRentNum() + "");
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            rySceneRentNum.setLayoutManager(linearLayoutManager);
            final List<AdminDeviceRentNumBean.SceneryRentNumPageBean.ListBean> list = bean.getValue().getSceneryRentNumPage().getList();
            adapter = new BaseRecyclerAdapter(getContext(), list, R.layout.item_scene_rent_num) {
                @Override
                protected void convert(BaseViewHolder helper, Object item, int position) {
                    helper.setText(R.id.tv_scene_name, list.get(position).getSceneryName());
                    helper.setText(R.id.tv_total_num, list.get(position).getRentNum()+"");
                }
            };
            rySceneRentNum.setAdapter(adapter);

        }

        @Override
        public void onError(String result) {
            dismissProgressDialog();
        }
    };
//    private void initPie() {
//        pieChart.setCenterText("");//饼状图中间的文字  
//        List<String> names = new ArrayList<>(); //每个模块的内容描述
//        names.add("昨天");
//        names.add("今天");
//        List<Float> date = new ArrayList<>(); //每个模块的值（占比率）
//        date.add(20f);
//        date.add(80f);
//        List<Integer> colors = new ArrayList<>(); //每个模块的颜色
////        colors.add(Color.parseColor("#4A92FC"));
//        colors.add(Color.rgb(255, 144, 62));
//        colors.add(Color.rgb(93, 146, 250));
//        //饼状图管理类
//        PieChartManager pieChartManager1 = new PieChartManager(pieChart);
//        pieChartManager1.setPieChart(names, date, colors);
//    }

    private void initData() {
        //获取数据
        lineChartBean = LocalJsonAnalyzeUtil.JsonToObject(getContext(), "line_chart.json", LineChartBean.class);
        //个人收益
        incomeBeanList = lineChartBean.getGRID0().getResult().getClientAccumulativeRate();
        shanghai = lineChartBean.getGRID0().getResult().getCompositeIndexShanghai();
        shenzhen = lineChartBean.getGRID0().getResult().getCompositeIndexShenzhen();
    }

    private void initView() {
        lineChartManager1 = new LineChartManager(lineChart1);
        //展示图表（折线图）
        lineChartManager1.showLineChart(mAdminStatisticsList, "离线设备", getResources().getColor(R.color.title_tour_color));
        lineChartManager1.addLine(mAdminStatisticsList, "在线设备", getResources().getColor(R.color.orange));
        lineChartManager1.addTotalDeviceLine(mAdminStatisticsList, "总设备数", getResources().getColor(R.color.red));

        //设置曲线填充色 以及 MarkerView
        Drawable drawable = getResources().getDrawable(R.drawable.fade_blue);
        lineChartManager1.setChartFillDrawable(drawable);
        lineChartManager1.setMarkerView(getContext());
    }

    @Override
    public void onAttach(Context context) {//绑定Fragment到Activity中
        if (context != null)
            callback = (Callback) context;//context就代表Activity
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        adminDeviceRentNumPresenter.onStop();
        superAdminStatisticsPresenter.onStop();
    }

    @Override
    public void progress(Date date) {//选择的日期
        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd ");
        if (timeFlag == 1) {
            tvTime.setText(sdf3.format(date.getTime()));
            initStatisticsData(sdf3.format(date.getTime()));
        } else if (timeFlag == 2) {
            tvStartTime.setText(sdf3.format(date.getTime()));
        }
        if (timeFlag == 3) {
            tvEndTime.setText(sdf3.format(date.getTime()));
            if (!tvStartTime.getText().equals("") && !tvEndTime.getText().toString().equals("")) {
                initDeviceRentNum("", tvStartTime.getText().toString(), tvEndTime.getText().toString());
            }
        }
    }

    public interface Callback {
        void drawerOpen();
    }

    public static Date getNextDay(Date date) {//获取当前系统前一天日期
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        date = calendar.getTime();
        return date;
    }

}
