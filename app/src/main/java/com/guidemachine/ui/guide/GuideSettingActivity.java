//package com.guidemachine.ui.guide;
//
//import android.os.Bundle;
//import android.view.View;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.guidemachine.R;
//import com.guidemachine.base.ui.BaseActivity;
//import com.guidemachine.util.IntentUtils;
//import com.guidemachine.util.StatusBarUtils;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//
///**
// * @author ChenLinWang
// * @email 422828518@qq.com
// * @create 2018/12/12 0012 16:05
// * description: 导游设置
// */
//public class GuideSettingActivity extends BaseActivity implements View.OnClickListener {
//
//    @BindView(R.id.rl_back)
//    RelativeLayout rlBack;
//    @BindView(R.id.tv_title_center)
//    TextView tvTitleCenter;
//    @BindView(R.id.rl_title)
//    RelativeLayout rlTitle;
//    @BindView(R.id.ll_job_mode)
//    LinearLayout llJobMode;
//    @BindView(R.id.ll_first)
//    LinearLayout llFirst;
//    @BindView(R.id.ll_second)
//    LinearLayout llSecond;
//    @BindView(R.id.ll_emergency_contact)
//    LinearLayout llEmergencyContact;
//
//    @Override
//    protected int setRootViewId() {
//        return R.layout.activity_guide_setting;
//    }
//
//
//    @Override
//    protected boolean setIsFull() {
//        return false;
//    }
//
//    @Override
//    protected void InitialView() {
//        StatusBarUtils.setWindowStatusBarColor(GuideSettingActivity.this, R.color.title_tour_color);
//        llJobMode.setOnClickListener(this);
//        llEmergencyContact.setOnClickListener(this);
//    }
//
//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.ll_job_mode:
//                IntentUtils.openActivity(GuideSettingActivity.this, GuideJobModeActivity.class);
//                break;
//            case R.id.ll_emergency_contact:
//                IntentUtils.openActivity(GuideSettingActivity.this, EmergencyContactActivity.class);
//                break;
//        }
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        // TODO: add setContentView(...) invocation
//        ButterKnife.bind(this);
//    }
//
//    @OnClick(R.id.rl_back)
//    public void onClick() {
//        finish();
//    }
//}
