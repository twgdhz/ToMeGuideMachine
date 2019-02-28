package com.guidemachine.ui.guide;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guidemachine.R;
import com.guidemachine.base.ui.BaseActivity;
import com.guidemachine.ui.superadmin.fragment.CalendarViewFragment;
import com.guidemachine.ui.superadmin.fragment.SuperAdminDataStatisticsFragment;
import com.guidemachine.util.StatusBarUtils;
import com.guidemachine.util.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author ChenLinWang
 * @email 422828518@qq.com
 * @create 2018/12/24 0024 10:19
 * description: 报警信息
 */
public class GuideAlarmMessageActivity extends BaseActivity
        implements CalendarViewFragment.OnFrgDataListener {

    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_title_center)
    TextView tvTitleCenter;
    @BindView(R.id.rl_right)
    RelativeLayout rlRight;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    //弹出的时间弹窗
    CalendarViewFragment calendarViewFragment;

    @Override
    protected int setRootViewId() {
        return R.layout.activity_guide_alarm_message;
    }

    @Override
    protected boolean setIsFull() {
        return false;
    }

    @Override
    protected void InitialView() {
        StatusBarUtils.setWindowStatusBarColor(GuideAlarmMessageActivity.this, R.color.white);
        calendarViewFragment = new CalendarViewFragment();
        calendarViewFragment.setIListener(GuideAlarmMessageActivity.this);
        rlRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendarViewFragment.show(getSupportFragmentManager(), "timeChoose");
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void progress(Date date) {//选择的日期
        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd ");
//        tvTime.setText(sdf3.format(date.getTime()));
        ToastUtils.msg(sdf3.format(date.getTime()).toString());
    }
    @OnClick(R.id.rl_back)
    public void onClick() {
        finish();
    }
}
