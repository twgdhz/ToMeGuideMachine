package com.guidemachine.ui.activity.order;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.guidemachine.R;
import com.guidemachine.base.adapter.BaseRecyclerAdapter;
import com.guidemachine.base.adapter.BaseViewHolder;
import com.guidemachine.base.ui.BaseActivity;
import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.OrderDetailBean;
import com.guidemachine.service.presenter.OrderDetailPresenter;
import com.guidemachine.service.view.OrderDetailView;
import com.guidemachine.ui.activity.shop.GoodsOrderPaymentActivity;
import com.guidemachine.util.Logger;
import com.guidemachine.util.StatusBarUtils;
import com.guidemachine.util.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * @author ChenLinWang
 * @email 422828518@qq.com
 * @create 2018/12/10 0010 11:38
 * description: 待付款
 */
public class NoPaymentOrderDetailActivity extends BaseActivity {

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
    @BindView(R.id.tv_get_by_self_address)
    TextView tvGetBySelfAddress;
    @BindView(R.id.tv_get_by_self_phone)
    TextView tvGetBySelfPhone;
    @BindView(R.id.img_dial_phone)
    ImageView imgDialPhone;
    @BindView(R.id.ll_get_by_self)
    LinearLayout llGetBySelf;
    @BindView(R.id.tv_receiver_name_phone)
    TextView tvReceiverNamePhone;
    @BindView(R.id.tv_receiver_address)
    TextView tvReceiverAddress;
    @BindView(R.id.ll_get_distribution)
    LinearLayout llGetDistribution;
    @BindView(R.id.ry_order_close)
    RecyclerView ryOrderClose;
    @BindView(R.id.tv_real_pay)
    TextView tvRealPay;
    @BindView(R.id.tv_pay_type)
    TextView tvPayType;
    @BindView(R.id.tv_distribution_type)
    TextView tvDistributionType;
    @BindView(R.id.tv_order_no)
    TextView tvOrderNo;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_place_order_time)
    TextView tvPlaceOrderTime;
    @BindView(R.id.tv_close_order_time)
    TextView tvCloseOrderTime;
    @BindView(R.id.tv_cancel_pay)
    TextView tvCancelPay;
    @BindView(R.id.tv_go_pay)
    TextView tvGoPay;
    @BindView(R.id.tv_refund_detail)
    TextView tvRefundDetail;
    OrderDetailPresenter orderDetailPresenter;
    String orderId;
    BaseRecyclerAdapter adapter;

    @Override
    protected int setRootViewId() {
        return R.layout.activity_no_payment_order_detail;
    }


    @Override
    protected boolean setIsFull() {
        return false;
    }


    @Override
    protected void InitialView() {
        Add(this);
        tvTitleCenter.setText("订单详情");
        StatusBarUtils.setWindowStatusBarColor(NoPaymentOrderDetailActivity.this, R.color.text_color4);
        orderId = getIntent().getExtras().getString("orderId");
        orderDetailPresenter = new OrderDetailPresenter(NoPaymentOrderDetailActivity.this);
        orderDetailPresenter.onCreate();
        orderDetailPresenter.attachView(orderBySelfDetailDetailView);
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("orderId", orderId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestData.toString());
        orderDetailPresenter.selectOrderDetail(requestBody);
        showProgressDialog();
    }

    OrderDetailView orderBySelfDetailDetailView = new OrderDetailView() {
        @Override
        public void onSuccess(final BaseBean<OrderDetailBean> bean) {
            dismissProgressDialog();
            Logger.d("OrderDetailBean:", bean.toString());
            final List<OrderDetailBean.GoodsDetailsBean> list = bean.getValue().getGoodsDetails();
            LinearLayoutManager manager = new LinearLayoutManager(NoPaymentOrderDetailActivity.this, LinearLayoutManager.VERTICAL, false) {//禁止滑动，解决滑动卡顿
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            };
            ryOrderClose.setLayoutManager(manager);
            adapter = new BaseRecyclerAdapter(NoPaymentOrderDetailActivity.this, list, R.layout.item_order_close) {
                @Override
                protected void convert(BaseViewHolder helper, Object item, int position) {
                    Glide.with(NoPaymentOrderDetailActivity.this).load(list.get(position).getGoodsImageUrl()).into((ImageView) helper.getView(R.id.img_good_photo));
                    helper.setText(R.id.tv_good_name, list.get(position).getGoodsName());
                    helper.setText(R.id.tv_unit_price, "￥" + list.get(position).getUnitPrice());
                    helper.setText(R.id.tv_good_num, "x" + list.get(position).getGoodsNum());
                    helper.setText(R.id.tv_good_price, "￥" + list.get(position).getUnitPrice().multiply(list.get(position).getGoodsNum()));
                    if (list.get(position).getIsDiscountsPrice() == 1) {//是否有优惠 (0:没有1:有)
                        helper.setText(R.id.tv_good_discount_num, "-￥" + list.get(position).getDiscountsPrice());
                    }
                }
            };
            ryOrderClose.setAdapter(adapter);
            tvRealPay.setText("￥" + bean.getValue().getRealPrice());
            if (bean.getValue().getGainType() == 1) {//配送方式  (0:配送1:自提)
                llGetBySelf.setVisibility(View.VISIBLE);
                llGetDistribution.setVisibility(View.GONE);
                tvGetBySelfAddress.setText("自提地址:" + bean.getValue().getShopAddress());
                tvGetBySelfPhone.setText("联系电话:" + bean.getValue().getShopTelephone());
                imgDialPhone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        Uri data = Uri.parse("tel:" + bean.getValue().getTelephone());
                        intent.setData(data);
                        startActivity(intent);
                    }
                });
            }
            tvPayType.setVisibility(View.GONE);
            tvDistributionType.setVisibility(View.GONE);
//            if (bean.getValue().getPayType() == 1) {//支付方式 (0:未知1:微信2:支付宝)
//                tvPayType.setText("支付方式：微信");
//            } else if (bean.getValue().getPayType() == 2) {
//                tvPayType.setText("支付方式：支付宝");
//            } else if (bean.getValue().getPayType() == 0) {
//                tvPayType.setText("支付方式：未知");
//            }
//            if (bean.getValue().getSelfType() == 0) {//配送方式(0:配送1:自提)
//                tvDistributionType.setText("配送方式：配送");
//            } else if (bean.getValue().getPayType() == 1) {
//                tvDistributionType.setText("配送方式：自提");
//            }
            tvOrderNo.setText("订单编号:" + bean.getValue().getOrderNo());
            tvPlaceOrderTime.setText("下单时间：" + bean.getValue().getCreateTime());
            tvPhone.setText("手机号:" + bean.getValue().getTelephone());
//            tvCloseOrderTime.setText("关闭时间：" + bean.getValue().getCloseTime());
            tvGoPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(NoPaymentOrderDetailActivity.this, GoodsOrderPaymentActivity.class);
                    intent.putExtra("goodsOrderId", bean.getValue().getOrderId());
                    intent.putExtra("source","2");//此字段用来判断是从订单页面过来还是直接购买。1：直接购买 2：订单页面
                    startActivity(intent);
                }
            });
        }

        @Override
        public void onError(String result) {
            ToastUtils.msg(result);
            dismissProgressDialog();
        }
    };

    @OnClick(R.id.rl_back)
    public void onClick() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        orderDetailPresenter.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}

