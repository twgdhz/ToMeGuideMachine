package com.guidemachine.ui.activity.order;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guidemachine.R;
import com.guidemachine.base.ui.BaseActivity;
import com.guidemachine.ui.activity.shop.adapter.MyFragmentPagerAdapter;
import com.guidemachine.ui.activity.shop.fragment.CompletedFragment;
import com.guidemachine.ui.activity.shop.fragment.RefundAndAfterSaleFragment;
import com.guidemachine.ui.activity.shop.fragment.ShoppingAllOrdersFragment;
import com.guidemachine.ui.activity.shop.fragment.ShoppingUnpaidFragment;
import com.guidemachine.ui.activity.shop.fragment.WaitingGettingSelfFragment;
import com.guidemachine.util.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author ChenLinWang
 * @email 422828518@qq.com
 * @create 2018/11/20 0020 14:01
 * description: 我的订单
 */
public class OrdersActivity extends BaseActivity {
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_title_center)
    TextView tvTitleCenter;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    // 将fragment装进列表中
    private List<Fragment> listFragment = new ArrayList<>();
    private List<String> listTitle;
    private ShoppingAllOrdersFragment shoppingAllOrdersFragment;//购物全部
    private ShoppingUnpaidFragment shoppingUnpaidFragment;
    private CompletedFragment completedFragment;//待收货
    private WaitingGettingSelfFragment waitingGettingSelfFragment;//待自提
    private RefundAndAfterSaleFragment refundAndAfterSaleFragment;//退款与售后
    private MyFragmentPagerAdapter myFragmentPagerAdapter;

    @Override
    protected int setRootViewId() {
        return R.layout.activity_orders;
    }

    @Override
    protected boolean setIsFull() {
        return false;
    }

    @Override
    protected void InitialView() {
        initTabLayout();
    }

    private void initTabLayout() {
//        ToastUtils.msg(orderType + "");
        tvTitleCenter.setText("订单");
        StatusBarUtils.setWindowStatusBarColor(OrdersActivity.this, R.color.text_color4);
        shoppingAllOrdersFragment = new ShoppingAllOrdersFragment();
        shoppingUnpaidFragment = new ShoppingUnpaidFragment();
        completedFragment = new CompletedFragment();
        waitingGettingSelfFragment = new WaitingGettingSelfFragment();
        refundAndAfterSaleFragment = new RefundAndAfterSaleFragment();

        listFragment.add(shoppingAllOrdersFragment);
        listFragment.add(shoppingUnpaidFragment);
        listFragment.add(completedFragment);
        listFragment.add(waitingGettingSelfFragment);
        listFragment.add(refundAndAfterSaleFragment);

        // 将名称加载tab名字列表，正常情况下，我们应该在values/arrays.xml中进行定义然后调用
        listTitle = new ArrayList<>();
//        listTitle.clear();

        listTitle.add("全部");
        listTitle.add("待付款");
        listTitle.add("已完成");
        listTitle.add("待自提");
        listTitle.add("退款/售后");

        for (Fragment f : listFragment) {
            Log.d("OrderActivity", f.toString());
        }
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        // 设置TabLayout的模式
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        // tabLayout.setTabMode(TabLayout.MODE_FIXED);
        // 为TabLayout添加tab名称
        tabLayout.addTab(tabLayout.newTab().setText(listTitle.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(listTitle.get(1)));
        tabLayout.addTab(tabLayout.newTab().setText(listTitle.get(2)));
        tabLayout.addTab(tabLayout.newTab().setText(listTitle.get(3)));
        tabLayout.addTab(tabLayout.newTab().setText(listTitle.get(4)));
        myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), listFragment, listTitle);
        viewPager.setAdapter(myFragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

//        Intent intent = getIntent();
//        Bundle bundle = intent.getExtras();
//        String str = bundle.getString("order");
//        if (str.equals("0")) {
//            //选择默认选中的tab
//            tabLayout.getTabAt(0).select();
//            viewPager.setCurrentItem(0);
//        } else if (str.equals("1")) {
//            //选择默认选中的tab
//            tabLayout.getTabAt(1).select();
//            viewPager.setCurrentItem(1);
//        } else if (str.equals("2")) {
//            //选择默认选中的tab
//            tabLayout.getTabAt(2).select();
//            viewPager.setCurrentItem(2);
//        } else if (str.equals("3")) {
//            //选择默认选中的tab
//            tabLayout.getTabAt(3).select();
//            viewPager.setCurrentItem(3);
//        } else if (str.equals("4")) {
//            //选择默认选中的tab
//            tabLayout.getTabAt(4).select();
//            viewPager.setCurrentItem(4);
//        }
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
