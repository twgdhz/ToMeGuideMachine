package com.guidemachine.ui.activity.order;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.guidemachine.util.IntentUtils;
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
 * @create 2018/12/10 0010 14:54
 * description:退款详情
 */
public class RefundOrderDetailActivity extends BaseActivity {

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
    //退款
    OrderDetailPresenter orderDetailPresenter;
    BaseRecyclerAdapter adapter;
    String kindRefundId;
    @BindView(R.id.tv_order_status)
    TextView tvOrderStatus;
    String goodsRefundId;
    String orderSaleId;
    @BindView(R.id.tv_refund_detail)
    TextView tvRefundDetail;
    //订单
//    SaleOrderDetailPresenter saleOrderDetailPresenter;

    @Override
    protected int setRootViewId() {
        return R.layout.activity_refund_order_detail;
    }

    @Override
    protected boolean setIsFull() {
        return false;
    }

    @Override
    protected void InitialView() {
        Add(this);
        tvTitleCenter.setText("订单详情");
        StatusBarUtils.setWindowStatusBarColor(RefundOrderDetailActivity.this, R.color.text_color4);
        goodsRefundId = getIntent().getExtras().getString("goodsRefundId");
        showProgressDialog();
        orderDetailPresenter = new OrderDetailPresenter(RefundOrderDetailActivity.this);
        orderDetailPresenter.onCreate();
        orderDetailPresenter.attachView(refundOrderDetailView);
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("goodsRefundId", goodsRefundId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestData.toString());
        orderDetailPresenter.selectOrderDetail(requestBody);
        tvRefundDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.openActivity(RefundOrderDetailActivity.this,RefundProgressActivity.class);
            }
        });
    }

    OrderDetailView refundOrderDetailView = new OrderDetailView() {
        @Override
        public void onSuccess(final BaseBean<OrderDetailBean> bean) {
            dismissProgressDialog();
            Log.d(TAG, bean.toString());
            final List<OrderDetailBean.GoodsDetailsBean> list = bean.getValue().getGoodsDetails();
            LinearLayoutManager manager = new LinearLayoutManager(RefundOrderDetailActivity.this, LinearLayoutManager.VERTICAL, false) {//禁止滑动，解决滑动卡顿
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            };
            ryOrderClose.setLayoutManager(manager);
            adapter = new BaseRecyclerAdapter(RefundOrderDetailActivity.this, list, R.layout.item_order_close) {
                @Override
                protected void convert(BaseViewHolder helper, Object item, int position) {
                    Glide.with(RefundOrderDetailActivity.this).load(list.get(position).getGoodsImageUrl()).into((ImageView) helper.getView(R.id.img_good_photo));
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
            if (bean.getValue().getRefundStatus() == 0) {//退款状态  (0退款中1已退款)
                tvOrderStatus.setText("退款中");
            } else if (bean.getValue().getRefundStatus() == 1) {
                tvOrderStatus.setText("已退款");
            }

            if (bean.getValue().getGainType() == 1) {//配送方式  (0:配送1:自提)
                llGetBySelf.setVisibility(View.VISIBLE);
                llGetDistribution.setVisibility(View.GONE);
                tvGetBySelfAddress.setText("自提地址:" + bean.getValue().getShopAddress());
                tvGetBySelfPhone.setText("联系电话:" + bean.getValue().getTelephone());
                tvPhone.setText("电话号码：" + bean.getValue().getTelephone());
                imgDialPhone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        Uri data = Uri.parse("tel:" + bean.getValue().getTelephone());
                        intent.setData(data);
                        startActivity(intent);
                    }
                });
            } else {

            }
            tvRealPay.setText("￥" + bean.getValue().getRealPrice());
            if (bean.getValue().getPayType() == 1) {//支付方式 (0:未知1:微信2:支付宝)
                tvPayType.setText("支付方式：微信");
            } else if (bean.getValue().getPayType() == 2) {
                tvPayType.setText("支付方式：支付宝");
            } else if (bean.getValue().getPayType() == 0) {
                tvPayType.setText("支付方式：未知");
            }
            if (bean.getValue().getGainType() == 0) {//配送方式(0:配送1:自提)
                tvDistributionType.setText("配送方式：配送");

            } else if (bean.getValue().getPayType() == 1) {
                tvDistributionType.setText("配送方式：自提");
            }
            tvOrderNo.setText("订单编号:" + bean.getValue().getOrderNo());
            tvPlaceOrderTime.setText("下单时间：" + bean.getValue().getCreateTime());
//            tvCloseOrderTime.setText("关闭时间：" + bean.getValue().getCloseTime());
        }

        @Override
        public void onError(String result) {
            dismissProgressDialog();
            ToastUtils.msg(result);
        }
    };
//    SaleOrderDetailView saleOrderDetailView = new SaleOrderDetailView() {
//        @Override
//        public void onSuccess(BaseBean<SaleOrderDetailBean> bean) {
//            dismissProgressDialog();
//            Log.d(TAG, bean.toString());
//            final List<SaleOrderDetailBean.GoodsDetailsBean> list = bean.getValue().getGoodsDetails();
//            LinearLayoutManager manager = new LinearLayoutManager(RefundOrderDetailActivity.this, LinearLayoutManager.VERTICAL, false) {//禁止滑动，解决滑动卡顿
//                @Override
//                public boolean canScrollVertically() {
//                    return false;
//                }
//            };
//            ryOrderClose.setLayoutManager(manager);
//            adapter = new BaseRecyclerAdapter(RefundOrderDetailActivity.this, list, R.layout.item_order_close) {
//                @Override
//                protected void convert(BaseViewHolder helper, Object item, int position) {
//                    Glide.with(RefundOrderDetailActivity.this).load(list.get(position).getGoodsImageUrl()).into((ImageView) helper.getView(R.id.img_good_photo));
//                    helper.setText(R.id.tv_good_name, list.get(position).getGoodsName());
//                    helper.setText(R.id.tv_unit_price, "￥" + list.get(position).getSaleAmount());
//                    helper.setText(R.id.tv_good_num, "x" + list.get(position).getGoodsNum());
//                    helper.setText(R.id.tv_good_price, "￥" + list.get(position).getSaleAmount() * list.get(position).getGoodsNum());
//                    if (list.get(position).getDiscountsAmountFlag() == 1) {//是否有优惠 (0:没有1:有)
//                        helper.setText(R.id.tv_good_discount_num, "-￥" + list.get(position).getDiscountsAmount());
//                    }
//                    helper.setText(R.id.tv_freight, "￥" + list.get(position).getExpressAmount());
//                }
//            };
//            ryOrderClose.setAdapter(adapter);
//            int saltStatus = bean.getValue().getSaleStatus();
//            if (saltStatus == 0) {//售后单状态 (0审核中1同意2拒绝3确认收货)
//                tvOrderStatus.setText("审核中");
//            } else if (saltStatus == 1) {
//                tvOrderStatus.setText("同意");
//            } else if (saltStatus == 2) {
//                tvOrderStatus.setText("拒绝");
//            } else if (saltStatus == 3) {
//                tvOrderStatus.setText("确认收货");
//            }
//
//            if (bean.getValue().getSelfType() == 1) {//配送方式  (0:配送1:自提)
////                llGetBySelf.setVisibility(View.VISIBLE);
////                llGetDistribution.setVisibility(View.GONE);
////                tvGetBySelfAddress.setText("自提地址:" + bean.getValue().getShopAddress());
////                tvGetBySelfPhone.setText("联系电话:" + bean.getValue().getTelephone());
////                tvPhone.setText("电话号码：" + bean.getValue().getTelephone());
////                imgDialPhone.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View v) {
////                        Intent intent = new Intent(Intent.ACTION_DIAL);
////                        Uri data = Uri.parse("tel:" + bean.getValue().getTelephone());
////                        intent.setData(data);
////                        startActivity(intent);
////                    }
////                });
//            } else {
//                llGetBySelf.setVisibility(View.GONE);
//                llGetDistribution.setVisibility(View.VISIBLE);
//                tvReceiverNamePhone.setText("收货人：" + bean.getValue().getSelfUserName() + "  " + bean.getValue().getSelfUserTelephone());
//                tvReceiverAddress.setText(bean.getValue().getSelfAddress().toString());
//                tvPhone.setText("电话号码：" + bean.getValue().getSelfUserTelephone());
//            }
//            tvRealPay.setText("￥" + bean.getValue().getRealAmount());
//            if (bean.getValue().getPayType() == 1) {//支付方式 (0:未知1:微信2:支付宝)
//                tvPayType.setText("支付方式：微信");
//            } else if (bean.getValue().getPayType() == 2) {
//                tvPayType.setText("支付方式：支付宝");
//            } else if (bean.getValue().getPayType() == 0) {
//                tvPayType.setText("支付方式：未知");
//            }
//            if (bean.getValue().getSelfType() == 0) {//配送方式(0:配送1:自提)
//                tvDistributionType.setText("配送方式：配送");
//
//            } else if (bean.getValue().getPayType() == 1) {
//                tvDistributionType.setText("配送方式：自提");
//            }
//            tvOrderNo.setText("订单编号:" + bean.getValue().getOrderNo());
//            tvPlaceOrderTime.setText("下单时间：" + bean.getValue().getCreateTime());
////            tvCloseOrderTime.setText("关闭时间：" + bean.getValue().getCloseTime());
//        }
//
//        @Override
//        public void onError(String result) {
//            dismissProgressDialog();
//            ToastUtils.msg(result);
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (orderDetailPresenter != null) {
            orderDetailPresenter.onStop();
        }
//        if (saleOrderDetailPresenter != null) {
//            saleOrderDetailPresenter.onStop();
//        }
    }

    @OnClick(R.id.rl_back)
    public void onClick() {
        finish();
    }
}

