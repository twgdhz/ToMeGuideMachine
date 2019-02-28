package com.guidemachine.ui.guide.login;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.guidemachine.R;
import com.guidemachine.base.ui.BaseActivity;
import com.guidemachine.ui.activity.order.OrdersActivity;
import com.guidemachine.ui.activity.shop.adapter.MyFragmentPagerAdapter;
import com.guidemachine.ui.guide.fragment.GuideLoginFragment;
import com.guidemachine.ui.guide.fragment.ManagerLoginFragment;
import com.guidemachine.util.StatusBarUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author ChenLinWang
 * @email 422828518@qq.com
 * @create 2018/12/21 0021 9:43
 * description: 导游模块登录
 */
public class GuideLoginActivity extends BaseActivity {
    @BindView(R.id.img_logo)
    ImageView imgLogo;
    @BindView(R.id.tab)
    TabLayout tab;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    // 将fragment装进列表中
    private List<Fragment> listFragment = new ArrayList<>();
    private List<String> listTitle;
    //导游登录
    GuideLoginFragment guideLoginFragment;
    //管理者登录
    ManagerLoginFragment managerLoginFragment;
    private MyFragmentPagerAdapter myFragmentPagerAdapter;

    @Override
    protected int setRootViewId() {
        return R.layout.activity_guide_login;
    }

    @Override
    protected boolean setIsFull() {
        return false;
    }

    @Override
    protected void InitialView() {
        Add(this);
        StatusBarUtils.setWindowStatusBarColor(GuideLoginActivity.this, R.color.text_color57);
        guideLoginFragment = new GuideLoginFragment();
        managerLoginFragment = new ManagerLoginFragment();
        listFragment.add(managerLoginFragment);
        listFragment.add(guideLoginFragment);
        // 将名称加载tab名字列表，正常情况下，我们应该在values/arrays.xml中进行定义然后调用
        listTitle = new ArrayList<>();
//        listTitle.clear();

        listTitle.add("管理版");
        listTitle.add("导游版");
        tab.setTabGravity(TabLayout.GRAVITY_FILL);
        // 设置TabLayout的模式
        tab.setTabMode(TabLayout.MODE_FIXED);
        // tabLayout.setTabMode(TabLayout.MODE_FIXED);
        // 为TabLayout添加tab名称
        tab.addTab(tab.newTab().setText(listTitle.get(0)));
        tab.addTab(tab.newTab().setText(listTitle.get(1)));
        myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), listFragment, listTitle);
        viewpager.setAdapter(myFragmentPagerAdapter);
        tab.setupWithViewPager(viewpager);
        tab.post(new Runnable() {
            @Override
            public void run() {
                setIndicator(tab, 50, 50);
            }
        });
    }

    public void setIndicator(TabLayout tabs, int leftDip, int rightDip) {//设置tablayout下划线长度
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
