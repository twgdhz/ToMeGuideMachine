package com.guidemachine.ui.guide;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.guidemachine.R;
import com.guidemachine.base.ui.BaseActivity;
import com.guidemachine.util.StatusBarUtils;
import com.guidemachine.util.ToastUtils;
import com.guidemachine.util.share.SPHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author ChenLinWang
 * @email 422828518@qq.com
 * @create 2019/1/18 0018 14:44
 * description: 工作模式
 */
public class GuideJobModeActivity extends BaseActivity {

    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_title_center)
    TextView tvTitleCenter;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.tv_common_mode)
    TextView tvCommonMode;
    @BindView(R.id.tv_emergency_mode)
    TextView tvEmergencyMode;
    @BindView(R.id.tv_custom_mode)
    TextView tvCustomMode;
    @BindView(R.id.et_custom_time)
    EditText etCustomTime;
    @BindView(R.id.sw_half_hour)
    Switch swHalfHour;
    @BindView(R.id.sw_ten_minute)
    Switch swTenMinute;
    @BindView(R.id.sw_custom_self)
    Switch swCustomSelf;

    @Override
    protected int setRootViewId() {
        return R.layout.activity_guide_job_mode;
    }

    @Override
    protected boolean setIsFull() {
        return false;
    }

    @Override
    protected void InitialView() {
        StatusBarUtils.setWindowStatusBarColor(GuideJobModeActivity.this, R.color.white);
        swHalfHour.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true) {
                    swTenMinute.setChecked(false);
                    swCustomSelf.setChecked(false);
                    SPHelper.getInstance(GuideJobModeActivity.this).setLocationMode("1800000");
                }
            }
        });
        swTenMinute.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true) {
                    swHalfHour.setChecked(false);
                    swCustomSelf.setChecked(false);
                    SPHelper.getInstance(GuideJobModeActivity.this).setLocationMode("10000");
                }
            }
        });
        swCustomSelf.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (etCustomTime.getText() == null || etCustomTime.getText().equals("")) {
                    ToastUtils.msg("自定义模式，时间不能为空");
                    return;
                }
                if (b == true) {
                    swTenMinute.setChecked(false);
                    swHalfHour.setChecked(false);
                    int time = Integer.parseInt(etCustomTime.getText().toString()) * 60 * 1000;
                    SPHelper.getInstance(GuideJobModeActivity.this).setLocationMode(time + "");
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.rl_back)
    public void onClick() {
        finish();
    }
}
