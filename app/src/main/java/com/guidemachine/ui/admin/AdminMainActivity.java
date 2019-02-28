package com.guidemachine.ui.admin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.guidemachine.R;
import com.guidemachine.base.ui.BaseActivity;
import com.guidemachine.ui.admin.fragment.AdminDataStatisticsFragment;
import com.guidemachine.ui.admin.fragment.TourTeamFragment;
import com.guidemachine.ui.guide.adapter.FragmentTabAdapter;
import com.guidemachine.ui.superadmin.SuperAdminMainActivity;
import com.guidemachine.util.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author ChenLinWang
 * @email 422828518@qq.com
 * @create 2018/12/28 0028 9:49
 * description: 普通管理员主页
 */
public class AdminMainActivity extends BaseActivity {

    AdminDataStatisticsFragment adminDataStatisticsFragment;
    TourTeamFragment tourTeamFragment;
    @BindView(R.id.tb_fm)
    FrameLayout tbFm;
    @BindView(R.id.rb_data_statistics)
    RadioButton rbDataStatistics;
    @BindView(R.id.rb_service_provider)
    RadioButton rbServiceProvider;
    @BindView(R.id.tb_rg)
    RadioGroup tbRg;
    private List<Fragment> fragments = new ArrayList<>();
    private FragmentTabAdapter tab;

    @Override
    protected int setRootViewId() {
        return R.layout.activity_admin_main;
    }


    @Override
    protected boolean setIsFull() {
        return false;
    }

    @Override
    protected void InitialView() {
        StatusBarUtils.setWindowStatusBarColor(AdminMainActivity.this, R.color.text_color61);
        adminDataStatisticsFragment = new AdminDataStatisticsFragment();
        tourTeamFragment = new TourTeamFragment();
        fragments.add(adminDataStatisticsFragment);
        fragments.add(tourTeamFragment);
        tab = new FragmentTabAdapter(this, fragments, R.id.tb_fm, tbRg);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
