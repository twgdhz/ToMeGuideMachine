package com.guidemachine.ui.activity.order;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guidemachine.R;
import com.guidemachine.base.ui.BaseActivity;
import com.guidemachine.ui.view.UnderLineLinearLayout;
import com.guidemachine.util.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author ChenLinWang
 * @email 422828518@qq.com
 * @create 2018/12/10 0010 17:24
 * description: 退款进度
 */
public class RefundProgressActivity extends BaseActivity {

    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_title_center)
    TextView tvTitleCenter;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.rl_screen)
    RelativeLayout rlScreen;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.underline_layout)
    UnderLineLinearLayout underlineLayout;
    List<String> list;
    String logisticsNo;
    @Override
    protected int setRootViewId() {
        return R.layout.activity_refund_progress;
    }

    @Override
    protected boolean setIsFull() {
        return false;
    }
    @Override
    protected void InitialView() {
        tvTitleCenter.setText("订单详情");
        StatusBarUtils.setWindowStatusBarColor(RefundProgressActivity.this, R.color.text_color4);
//        logisticsNo = getIntent().getExtras().getString("logisticsNo");
        list = new ArrayList<>();
        list.add("发货");
        list.add("路上");
        list.add("快递");
        list.add("到了");
        initBody(list);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.rl_back)
    public void onClick() {
        finish();
    }

    //初始化物流时间轴
    private void initBody(List<String> list) {
        for (int i = 0; i < list.size(); i++) {
            View v = LayoutInflater.from(this).inflate(R.layout.item_logicst, underlineLayout, false);
            ((TextView) v.findViewById(R.id.tv_title)).setText(list.get(i));
            if (i == 0) {
                ((TextView) v.findViewById(R.id.tv_title)).setTextColor(ContextCompat.getColor(this, R.color.orange));
            }
            underlineLayout.addView(v);
        }
    }

}
