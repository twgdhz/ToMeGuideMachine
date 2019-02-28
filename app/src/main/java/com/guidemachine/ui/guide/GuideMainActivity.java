package com.guidemachine.ui.guide;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.guidemachine.R;
import com.guidemachine.base.ui.BaseActivity;
import com.guidemachine.ui.guide.adapter.FragmentTabAdapter;
import com.guidemachine.ui.guide.fragment.GuideSettingFragment;
import com.guidemachine.ui.guide.fragment.TeamLocationFragment;
import com.guidemachine.ui.guide.fragment.TourJourneyListFragment;
import com.guidemachine.ui.guide.fragment.TourManagerFragment;
import com.guidemachine.util.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author ChenLinWang
 * @email 422828518@qq.com
 * @create 2018/12/12 0012 11:10
 * description: 导游主页
 */
public class GuideMainActivity extends BaseActivity {

    @BindView(R.id.tb_fm)
    FrameLayout tbFm;
    @BindView(R.id.tb_rg)
    RadioGroup tbRg;
    @BindView(R.id.rb_team_location)
    RadioButton rbTeamLocation;
    @BindView(R.id.rb_tour_manager)
    RadioButton rbTourManager;
    @BindView(R.id.rb_journey_list)
    RadioButton rbJourneyList;
    @BindView(R.id.rb_guide_setting)
    RadioButton rbGuideSetting;
    private List<Fragment> fragments = new ArrayList<>();
    private FragmentTabAdapter tab;
    TeamLocationFragment teamLocationFragment;//全团定位
    TourManagerFragment tourManagerFragment;//游客管理
    TourJourneyListFragment tourJourneyListFragment;//行程列表
    GuideSettingFragment guideSettingFragment;//设置

    @Override
    protected int setRootViewId() {
        return R.layout.activity_tour_main;
    }


    @Override
    protected boolean setIsFull() {
        return false;
    }

    @Override
    protected void InitialView() {
        StatusBarUtils.setWindowStatusBarColor(GuideMainActivity.this, R.color.title_tour_color);
        teamLocationFragment = new TeamLocationFragment();
        tourManagerFragment = new TourManagerFragment();
        tourJourneyListFragment = new TourJourneyListFragment();
        guideSettingFragment = new GuideSettingFragment();
        fragments.add(teamLocationFragment);
        fragments.add(tourManagerFragment);
        fragments.add(tourJourneyListFragment);
        fragments.add(guideSettingFragment);
        tab = new FragmentTabAdapter(this, fragments, R.id.tb_fm, tbRg);
        tab.setOnRgsExtraCheckedChangedListener(new FragmentTabAdapter.OnRgsExtraCheckedChangedListener() {
                                                    public void OnRgsExtraCheckedChanged(RadioGroup radioGroup, int checkedId, int index) {
                                                        if (checkedId == R.id.rb_team_location||checkedId==R.id.rb_guide_setting) {
                                                            StatusBarUtils.setWindowStatusBarColor(GuideMainActivity.this, R.color.title_tour_color);
                                                        } else {
                                                            StatusBarUtils.setWindowStatusBarColor(GuideMainActivity.this, R.color.white);
                                                        }
                                                    }
                                                }
        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
