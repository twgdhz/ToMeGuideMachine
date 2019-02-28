package com.guidemachine.ui.admin.fragment;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.guidemachine.R;
import com.guidemachine.base.ui.BaseFragment;
import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.SceneryServiceInfoDetail;
import com.guidemachine.service.presenter.AdminDataStatisticsPresenter;
import com.guidemachine.service.view.AdminDataStatisticsView;
import com.guidemachine.ui.activity.MapActivity;
import com.guidemachine.ui.admin.AdminSettingActivity;
import com.guidemachine.ui.superadmin.chart.manager.LineChartManager;
import com.guidemachine.ui.superadmin.chart.manager.SceneServiceDetailLineChartManager;
import com.guidemachine.ui.superadmin.data.CompositeIndexBean;
import com.guidemachine.ui.superadmin.data.IncomeBean;
import com.guidemachine.ui.superadmin.data.LineChartBean;
import com.guidemachine.ui.superadmin.fragment.CalendarViewFragment;
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
 * @create 2018/12/28 0028 9:51
 * description: 普通管理员数据统计页面
 */
public class AdminDataStatisticsFragment extends BaseFragment implements CalendarViewFragment.OnFrgDataListener {

    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.et_search)
    TextView etSearch;
    @BindView(R.id.rl_setting)
    RelativeLayout rlSetting;
    @BindView(R.id.rl_title)
    LinearLayout rlTitle;
    @BindView(R.id.tv_start_time_title)
    TextView tvStartTimeTitle;
    @BindView(R.id.tv_end_time_title)
    TextView tvEndTimeTitle;
    @BindView(R.id.ll_time_title)
    LinearLayout llTimeTitle;
    @BindView(R.id.tv_start_time)
    TextView tvStartTime;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;
    @BindView(R.id.lineChart)
    LineChart lineChart;
    @BindView(R.id.view_mine)
    View viewMine;
    @BindView(R.id.view_shanghai)
    TextView viewShanghai;
    @BindView(R.id.cl_shanghai)
    ConstraintLayout clShanghai;
    @BindView(R.id.view_shenzhen)
    View viewShenzhen;
    @BindView(R.id.cl_shenzhen)
    ConstraintLayout clShenzhen;
    @BindView(R.id.tv_total_time)
    TextView tvTotalTime;
    Unbinder unbinder;
//    private LineChartBean lineChartBean;
//    private List<IncomeBean> incomeBeanList;//个人收益
//    private List<CompositeIndexBean> shanghai;//沪市指数
//    private List<CompositeIndexBean> shenzhen;//沪市指数

    private SceneServiceDetailLineChartManager lineChartManager1;
    //弹出的时间弹窗
    CalendarViewFragment calendarViewFragment;
    //2:设备租赁数的开始时间
    //3：设备租赁数的结束时间
    private int timeFlag = 2;
    //管理员数据统计接口
    AdminDataStatisticsPresenter adminDataStatisticsPresenter;

    @Override
    protected int setRootViewId() {
        return R.layout.fragment_admin_data_statistics;
    }

    @Override
    protected void initView(View view, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        calendarViewFragment = new CalendarViewFragment();
        calendarViewFragment.setIListener(AdminDataStatisticsFragment.this);
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
        tvStartTime.setText(new SimpleDateFormat("yyyy-MM-dd").format(getNextDay(new Date())).toString());
        tvEndTime.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()).toString());

        rlSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.openActivity(getActivity(), AdminSettingActivity.class);
            }
        });
        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.openActivity(getActivity(), MapActivity.class);
                getActivity().finish();
            }
        });
        adminDataStatisticsPresenter = new AdminDataStatisticsPresenter(getContext());
        adminDataStatisticsPresenter.onCreate();
        adminDataStatisticsPresenter.attachView(adminDataStatisticsView);

        initData(tvStartTime.getText().toString(), tvEndTime.getText().toString());
    }

    private void initData(String startTime, String endTime) {
        showProgressDialog();
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("startTime", startTime);
            requestData.put("endTime", endTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestData.toString());
        adminDataStatisticsPresenter.getAdminDataStatistics(requestBody);
    }



    AdminDataStatisticsView adminDataStatisticsView = new AdminDataStatisticsView() {
        @Override
        public void onSuccess(BaseBean<SceneryServiceInfoDetail> bean) {
            dismissProgressDialog();
            Logger.d("SceneryServiceInfoDetail", bean.toString());
            tvTotalTime.setText(bean.getValue().getTotalRentNum() + "");
            initView(bean.getValue());
        }

        @Override
        public void onError(String result) {
            dismissProgressDialog();
        }
    };
    private void initView(SceneryServiceInfoDetail bean) {
        lineChartManager1 = new SceneServiceDetailLineChartManager(lineChart);
        //展示图表（折线图）
        lineChartManager1.showLineChart(bean.getData(), "离线设备", getResources().getColor(R.color.title_tour_color));
        lineChartManager1.addLine(bean.getData(), "在线设备", getResources().getColor(R.color.orange));
        lineChartManager1.addLine(bean.getData(), "总设备数", getResources().getColor(R.color.red));

        //设置曲线填充色 以及 MarkerView
        Drawable drawable = getResources().getDrawable(R.drawable.fade_blue);
        lineChartManager1.setChartFillDrawable(drawable);
        lineChartManager1.setMarkerView(getContext());
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
        adminDataStatisticsPresenter.onStop();
    }

    @Override
    public void progress(Date date) {
        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd ");
        if (timeFlag == 2) {
            tvStartTime.setText(sdf3.format(date.getTime()));
        }
        if (timeFlag == 3) {
            tvEndTime.setText(sdf3.format(date.getTime()));
            if (!tvStartTime.getText().equals("") && !tvEndTime.getText().toString().equals("")) {
                initData(tvStartTime.getText().toString(), tvEndTime.getText().toString());
            }
        }
    }

    public static Date getNextDay(Date date) {//获取当前系统前一天日期
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        date = calendar.getTime();
        return date;
    }
}
