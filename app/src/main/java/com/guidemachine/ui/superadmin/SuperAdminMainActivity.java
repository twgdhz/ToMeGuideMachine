package com.guidemachine.ui.superadmin;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.guidemachine.R;
import com.guidemachine.base.ui.BaseActivity;
import com.guidemachine.ui.guide.adapter.FragmentTabAdapter;
import com.guidemachine.ui.superadmin.fragment.SuperAdminDataStatisticsFragment;
import com.guidemachine.ui.superadmin.fragment.SuperAdminServiceProviderFragment;
import com.guidemachine.util.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author ChenLinWang
 * @email 422828518@qq.com
 * @create 2018/12/13 0013 12:25
 * description: 超级管理员主页
 */
public class SuperAdminMainActivity extends BaseActivity implements SuperAdminDataStatisticsFragment.Callback {

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.tb_fm)
    FrameLayout tbFm;
    @BindView(R.id.rb_data_statistics)
    RadioButton rbDataStatistics;
    @BindView(R.id.rb_service_provider)
    RadioButton rbServiceProvider;
    @BindView(R.id.tb_rg)
    RadioGroup tbRg;
    @BindView(R.id.nav_view)
    NavigationView navView;
    private List<Fragment> fragments = new ArrayList<>();
    private FragmentTabAdapter tab;
    SuperAdminDataStatisticsFragment superAdminDataStatisticsFragment;
    SuperAdminServiceProviderFragment superAdminServiceProviderFragment;

    @Override
    protected int setRootViewId() {
        return R.layout.activity_super_admin_main;
    }


    @Override
    protected boolean setIsFull() {
        return false;
    }

    @Override
    protected void InitialView() {
        StatusBarUtils.setWindowStatusBarColor(SuperAdminMainActivity.this, R.color.white);
        superAdminDataStatisticsFragment = new SuperAdminDataStatisticsFragment();
        superAdminServiceProviderFragment = new SuperAdminServiceProviderFragment();
        fragments.add(superAdminDataStatisticsFragment);
        fragments.add(superAdminServiceProviderFragment);
        tab = new FragmentTabAdapter(this, fragments, R.id.tb_fm, tbRg);
        tab.setOnRgsExtraCheckedChangedListener(new FragmentTabAdapter.OnRgsExtraCheckedChangedListener() {
                                                    public void OnRgsExtraCheckedChanged(RadioGroup radioGroup, int checkedId, int index) {
                                                        if (checkedId == R.id.rb_team_location) {
                                                            StatusBarUtils.setWindowStatusBarColor(SuperAdminMainActivity.this, R.color.title_tour_color);
                                                        } else {
                                                            StatusBarUtils.setWindowStatusBarColor(SuperAdminMainActivity.this, R.color.white);
                                                        }
                                                    }
                                                }
        );

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    public void drawerOpen() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START) == true) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }
}
