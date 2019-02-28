package com.guidemachine.ui.superadmin;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.guidemachine.R;
import com.guidemachine.base.ui.BaseActivity;
import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.SceneryServiceInfoDetail;
import com.guidemachine.service.presenter.SceneryServiceDetailPresenter;
import com.guidemachine.service.view.SceneryServiceDetailView;
import com.guidemachine.ui.admin.fragment.AdminDataStatisticsFragment;
import com.guidemachine.ui.superadmin.chart.manager.SceneServiceDetailLineChartManager;
import com.guidemachine.ui.superadmin.chart.utils.LocalJsonAnalyzeUtil;
import com.guidemachine.ui.superadmin.data.CompositeIndexBean;
import com.guidemachine.ui.superadmin.data.IncomeBean;
import com.guidemachine.ui.superadmin.data.LineChartBean;
import com.guidemachine.ui.superadmin.fragment.CalendarViewFragment;
import com.guidemachine.util.Logger;
import com.guidemachine.util.StatusBarUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * @author ChenLinWang
 * @email 422828518@qq.com
 * @create 2018/12/14 0014 13:55
 * description:单个景区设备使用详情
 */
public class SuperAdminSceneDeviceDetailActivity extends BaseActivity implements CalendarViewFragment.OnFrgDataListener {


    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_title_center)
    TextView tvTitleCenter;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
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
    private LineChartBean lineChartBean;
    private List<IncomeBean> incomeBeanList;//个人收益
    private List<CompositeIndexBean> shanghai;//沪市指数
    private List<CompositeIndexBean> shenzhen;//沪市指数

    private SceneServiceDetailLineChartManager lineChartManager1;
    //弹出的时间弹窗
    CalendarViewFragment calendarViewFragment;
    //景区服务商信息
    SceneryServiceDetailPresenter sceneryServiceDetailPresenter;
    //2:设备租赁数的开始时间
    //3：设备租赁数的结束时间
    private int timeFlag = 2;

    @Override
    protected int setRootViewId() {
        return R.layout.activity_super_admin_scene_device_detail;
    }


    @Override
    protected boolean setIsFull() {
        return false;
    }

    @Override
    protected void InitialView() {

        StatusBarUtils.setWindowStatusBarColor(SuperAdminSceneDeviceDetailActivity.this, R.color.white);
        calendarViewFragment = new CalendarViewFragment();
        calendarViewFragment.setIListener(SuperAdminSceneDeviceDetailActivity.this);
        tvStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeFlag = 2;
                calendarViewFragment.show(SuperAdminSceneDeviceDetailActivity.this.getSupportFragmentManager(), "timeChoose");
            }
        });
        tvEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeFlag = 3;
                calendarViewFragment.show(SuperAdminSceneDeviceDetailActivity.this.getSupportFragmentManager(), "timeChoose");
            }
        });
        tvStartTime.setText(new SimpleDateFormat("yyyy-MM-dd").format(getNextDay(new Date())).toString());
        tvEndTime.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()).toString());
        sceneryServiceDetailPresenter = new SceneryServiceDetailPresenter(SuperAdminSceneDeviceDetailActivity.this);
        sceneryServiceDetailPresenter.onCreate();
        sceneryServiceDetailPresenter.attachView(sceneryServiceDetailView);
        initData(tvStartTime.getText().toString(), tvEndTime.getText().toString());
    }

    private void initData(String startTime, String endTime) {
        JSONObject requestData = new JSONObject();
        try {
//            requestData.put("sceneryId", getIntent().getExtras().getString("sceneryId"));
            requestData.put("sceneryId", "1");
            requestData.put("startTime", startTime);
            requestData.put("endTime", endTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestData.toString());
        sceneryServiceDetailPresenter.getSceneryServiceDetail(requestBody);
    }


    SceneryServiceDetailView sceneryServiceDetailView = new SceneryServiceDetailView() {
        @Override
        public void onSuccess(BaseBean<SceneryServiceInfoDetail> bean) {
            Logger.d("SceneryServiceInfoDetail", bean.toString());
            initView(bean.getValue());
            tvTotalTime.setText(bean.getValue().getTotalRentNum() + "");
        }

        @Override
        public void onError(String result) {

        }
    };

    private void initView(SceneryServiceInfoDetail bean) {
        lineChartManager1 = new SceneServiceDetailLineChartManager(lineChart);
        //展示图表（折线图）
        lineChartManager1.showLineChart(bean.getData(), "离线设备", getResources().getColor(R.color.title_tour_color));
        lineChartManager1.addLine(bean.getData(), "在线设备", getResources().getColor(R.color.orange));
        lineChartManager1.addTotal(bean.getData(), "总设备数", getResources().getColor(R.color.red));

        //设置曲线填充色 以及 MarkerView
        Drawable drawable = getResources().getDrawable(R.drawable.fade_blue);
        lineChartManager1.setChartFillDrawable(drawable);
        lineChartManager1.setMarkerView(SuperAdminSceneDeviceDetailActivity.this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    public static Date getNextDay(Date date) {//获取当前系统前一天日期
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        date = calendar.getTime();
        return date;
    }

    @OnClick(R.id.rl_back)
    public void onClick() {
        finish();
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
}
