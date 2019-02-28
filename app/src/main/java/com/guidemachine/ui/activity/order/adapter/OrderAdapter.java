package com.guidemachine.ui.activity.order.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.guidemachine.R;
import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.SelectOrderBean;
import com.guidemachine.service.presenter.BuyAgainPresenter;
import com.guidemachine.service.presenter.CancelOrderPresenter;
import com.guidemachine.service.view.BaseView;
import com.guidemachine.ui.activity.order.ApplyRefundActivity;
import com.guidemachine.ui.activity.order.GetGoodsBySelfActivity;
import com.guidemachine.ui.activity.order.NoPaymentOrderDetailActivity;
import com.guidemachine.ui.activity.order.OrderCompletedActivity;
import com.guidemachine.ui.activity.order.RefundOrderDetailActivity;
import com.guidemachine.ui.activity.shop.GoodsOrderPaymentActivity;
import com.guidemachine.ui.activity.shop.ShoppingCartActivity;
import com.guidemachine.ui.view.CustomDialog;
import com.guidemachine.ui.view.GetSelfPop;
import com.guidemachine.util.IntentUtils;
import com.guidemachine.util.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * @author ChenLinWang
 * @email 422828518@qq.com
 * @create 2018/10/10 0010 13:51
 * description: 订单
 */
public class OrderAdapter extends BaseExpandableListAdapter {

    private Context context;
    BaseBean<SelectOrderBean> mList;
    private String totalPrice;
    //取消订单
    CancelOrderPresenter cancelOrderPresenter;
    //再来一单
    BuyAgainPresenter buyAgainPresenter;
    int myGroupPosition;
    int myChildPosition;
    CustomDialog logDialog;
    public OrderAdapter(Context context, BaseBean<SelectOrderBean> mList) {
        this.context = context;
        this.mList = mList;
        cancelOrderPresenter = new CancelOrderPresenter(context);
        cancelOrderPresenter.onCreate();
        cancelOrderPresenter.attachView(baseView);
        buyAgainPresenter = new BuyAgainPresenter(context);
        buyAgainPresenter.onCreate();
        buyAgainPresenter.attachView(baseView1);
        if (logDialog == null) {
            logDialog = new CustomDialog(context, "加载中...");
        }
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public int getGroupCount() {
        if (mList.getValue() != null && mList.getValue().getList().size() > 0) {
            return mList.getValue().getList().size();
        } else {
            return 0;
        }
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (mList.getValue().getList().get(groupPosition).getItems() != null && mList.getValue().getList().get(groupPosition).getItems().size() > 0) {
            return mList.getValue().getList().get(groupPosition).getItems().size();
        } else {
            return 0;
        }
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupPosition;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mList.getValue().getList().get(groupPosition).getItems().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_orders_group, null);
            groupViewHolder = new GroupViewHolder(convertView);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        //店铺ID
        String store_id = mList.getValue().getList().get(groupPosition).getShopId();
        //店铺名称
        String store_name = mList.getValue().getList().get(groupPosition).getShopName();
        /* 订单状态 (0:待付款 2:待自提 4:已完成 5:交易关闭)*/
        int pay_status = mList.getValue().getList().get(groupPosition).getOrderStatus();
        //订单类型  订单类型(1:正常 2:退款单)
        int order_type = mList.getValue().getList().get(groupPosition).getOrderType();
        int refundStatus = 0;
        //订单类型  (1:导游机/套票 2:购物)
        if (mList.getValue().getList().get(groupPosition).getOrderType() != 1) {
            refundStatus = mList.getValue().getList().get(groupPosition).getRefundStatus();
        }
        //支付状态
        //订单搜索状态 (导游机/套票: 1:全部2:待付款3:待使用4:待归还5:退款)
        //(购物: 1:全部2:待付款3:待发货4:待收货5:待自提6:退款/售后)
        int orderSearchStatus = mList.getValue().getList().get(groupPosition).getOrderSearchStatus();
        int saleStatus = mList.getValue().getList().get(groupPosition).getOrderStatus();
        if (store_name != null) {
            groupViewHolder.tvStoreName.setText(store_name);
        }
//        if (order_type == 1) {
//            if (orderSearchStatus == 5) {
//                if (refundStatus == 0) {
//                    groupViewHolder.tvPayStatus.setText("退款中");
//                } else {
//                    groupViewHolder.tvPayStatus.setText("已退款");
//                }
//            } else {
//                if (pay_status == 0) {
//                    groupViewHolder.tvPayStatus.setText("待付款");
//                } else if (pay_status == 1) {
//                    groupViewHolder.tvPayStatus.setText("待使用");
//                } else if (pay_status == 2) {
//                    groupViewHolder.tvPayStatus.setText("待归还");
//                } else if (pay_status == 3) {
//                    groupViewHolder.tvPayStatus.setText("已完成");
//                } else if (pay_status == 4) {
//                    groupViewHolder.tvPayStatus.setText("关闭交易");
//
//                }
//            }
//
//        } else
        if (order_type == 1) {
            if (pay_status == 0) {
                groupViewHolder.tvPayStatus.setText("待付款");
            } else if (pay_status == 1) {
                groupViewHolder.tvPayStatus.setText("待发货");
            } else if (pay_status == 2) {
                groupViewHolder.tvPayStatus.setText("待自提");
            } else if (pay_status == 3) {
                groupViewHolder.tvPayStatus.setText("待收货");
            } else if (pay_status == 4) {
                groupViewHolder.tvPayStatus.setText("已完成");
            } else if (pay_status == 5) {
                groupViewHolder.tvPayStatus.setText("交易关闭");
            }
        } else if (order_type == 2) {
            if (refundStatus == 0) {//退款状态 (0退款中1已退款)
                groupViewHolder.tvPayStatus.setText("退款中");
            } else if (refundStatus == 1) {
                groupViewHolder.tvPayStatus.setText("已退款");
            }
        }
        return convertView;
    }

    static class GroupViewHolder {
        @BindView(R.id.tv_name)
        TextView tvStoreName;
        @BindView(R.id.tv_pay_status)
        TextView tvPayStatus;

        GroupViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildViewHolder childViewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_orders_child, null);
            childViewHolder = new ChildViewHolder(convertView);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        final SelectOrderBean.ListBean.ItemsBean goodsBean = mList.getValue().getList().get(groupPosition).getItems().get(childPosition);
        final SelectOrderBean.ListBean shopsBean = mList.getValue().getList().get(groupPosition);

        //商品图片
        String goods_image = goodsBean.getGoodsImageUrl();
        //商品ID
        final String goods_id = goodsBean.getGoodsId();
        //商品名称
        String goods_name = goodsBean.getGoodsName();
        //商品价格
        int goods_price = goodsBean.getUnitPrice();
        //商品数量
        int goods_num = goodsBean.getGoodsNum();

        //订单类型  (1:正常 2:退款)
        final int order_type = shopsBean.getOrderType();

        Glide.with(context)
                .load(goods_image)
                .into(childViewHolder.ivPhoto);
        if (goods_name != null) {
            childViewHolder.tvGoodName.setText(goods_name);
        }
        if (goods_price != 0) {
            childViewHolder.tvGoodPrice.setText("￥" + goods_price);
        }
        childViewHolder.tvGoodNum.setText("数量X" + goods_num);
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
//        String date = simpleDateFormat.format(shopsBean.getCreateTime());

        childViewHolder.tvDate.setText(shopsBean.getCreateTime());
        childViewHolder.tvPayPrice.setText("付款：" + shopsBean.getPrice() + "元");
        if (order_type == 2) {//订单类型  (1:正常 2:退款单)
        }
        childViewHolder.llDistribution.setVisibility(View.VISIBLE);
        childViewHolder.llSpecification.setVisibility(View.VISIBLE);
        //配送方式
        childViewHolder.tvDistribution.setText("自提");
        // 是否有规格 (0:没有1:有)
        int goodsSkuFlag = goodsBean.getFlag();
        if (goodsSkuFlag == 0) {
            childViewHolder.llSpecification.setVisibility(View.GONE);
        } else {
            childViewHolder.llSpecification.setVisibility(View.VISIBLE);
            //商品规格
            String goods_specification = goodsBean.getGoodsSkuName().toString();
            childViewHolder.tvSpecification.setText(goods_specification);
        }
        //支付状态
        /* (0待付款1待发货2待自提3待收货4已完成5交易关闭6退款7售后)*/
        int pay_status = shopsBean.getOrderStatus();

        //订单搜索状态 (导游机/套票: 1:全部2:待付款3:待使用4:待归还5:退款)
        //(购物: 1:全部2:待付款3:待发货4:待收货5:待自提6:退款/售后)
        int orderSearchStatus = shopsBean.getOrderSearchStatus();
//        if (order_type == 1) { //订单类型  (1:导游机/套票 2:购物)
//            if (orderSearchStatus == 5) {//退款
//                childViewHolder.tvDeleteOrder.setVisibility(View.GONE);//交易关闭
//                childViewHolder.llWay.setVisibility(View.GONE);//待付款
//                childViewHolder.llUnWriteOff.setVisibility(View.GONE);//待使用
//                childViewHolder.llUnReturnBack.setVisibility(View.GONE);//待归还
//                childViewHolder.llSeeDetail.setVisibility(View.VISIBLE);//查看退款详情
//                childViewHolder.llWaitingReceive.setVisibility(View.GONE);
//                childViewHolder.llWaitingGetSelf.setVisibility(View.GONE);
//                childViewHolder.llBuyAgain.setVisibility(View.GONE);
//                childViewHolder.llApplyRefund.setVisibility(View.GONE);
//                childViewHolder.llSeeDetail.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        ToastUtils.msg("导游机套票 查看详情");
//                    }
//                });
//                childViewHolder.llItem.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
////                        Intent intent = new Intent(context, GuideMachineOrSeasonTicketRefundActivity.class);
////                        intent.putExtra("goodsRefundId", shopsBean.getGoodsRefundId());
////                        context.startActivity(intent);
////                        Log.d("orderId", groupPosition + "   " + shopsBean.getGoodsOrderId());
//                    }
//                });
//            } else if (orderSearchStatus != 5) {
//                if (pay_status == 0) {//待付款
//                    childViewHolder.tvDeleteOrder.setVisibility(View.GONE);//交易关闭
//                    childViewHolder.llWay.setVisibility(View.VISIBLE);//待付款
//                    childViewHolder.llUnWriteOff.setVisibility(View.GONE);//待使用
//                    childViewHolder.llUnReturnBack.setVisibility(View.GONE);//待归还
//                    childViewHolder.llSeeDetail.setVisibility(View.GONE);
//                    childViewHolder.llWaitingReceive.setVisibility(View.GONE);
//                    childViewHolder.llWaitingGetSelf.setVisibility(View.GONE);
//                    childViewHolder.llBuyAgain.setVisibility(View.GONE);
//                    childViewHolder.llApplyRefund.setVisibility(View.GONE);
//                    childViewHolder.tvGoPay.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
////                            Intent intent = new Intent(context, MyOrderPaymentActivity.class);
////                            intent.putExtra("goodsOrderId", shopsBean.getGoodsOrderId());
////                            intent.putExtra("orderType", 1);
////                            context.startActivity(intent);
////                            Log.d("orderId", groupPosition + "   " + shopsBean.getGoodsOrderId());
//                        }
//                    });
//                    childViewHolder.tvCancelPay.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
////                            JSONObject requestData = new JSONObject();
////                            try {
////                                requestData.put("goodsOrderId", shopsBean.getGoodsOrderId());
////                            } catch (JSONException e) {
////                                e.printStackTrace();
////                            }
////                            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestData.toString());
////                            cancelOrderPresenter.cancelKindOrder(requestBody, order_type);//订单类型  (1:导游机/套票 2:购物)
////                            Log.d("orderId", groupPosition + "   " + shopsBean.getGoodsOrderId());
////                            myGroupPosition = groupPosition;
////                            myChildPosition = childPosition;
//                        }
//                    });
//                    childViewHolder.llItem.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
////                            Intent intent = new Intent(context, GuideMachineOrSeasonTicketWaitingPaymentActivity.class);
////                            intent.putExtra("goodsOrderId", shopsBean.getGoodsOrderId());
////                            context.startActivity(intent);
////                            Log.d("orderId", groupPosition + "   " + shopsBean.getGoodsOrderId());
//                        }
//                    });
//                } else if (pay_status == 1) {//待使用
//                    childViewHolder.tvDeleteOrder.setVisibility(View.GONE);//交易关闭
//                    childViewHolder.llWay.setVisibility(View.GONE);//待付款
//                    childViewHolder.llUnWriteOff.setVisibility(View.VISIBLE);//待使用
//                    childViewHolder.llUnReturnBack.setVisibility(View.GONE);//待归还
//                    childViewHolder.llSeeDetail.setVisibility(View.GONE);
//                    childViewHolder.llWaitingReceive.setVisibility(View.GONE);
//                    childViewHolder.llWaitingGetSelf.setVisibility(View.GONE);
//                    childViewHolder.llBuyAgain.setVisibility(View.GONE);
//                    childViewHolder.llApplyRefund.setVisibility(View.GONE);
//                    if (shopsBean.getIsRefund() == 0) {//是否支持退款(0:不支持1:支持)
//                        childViewHolder.tvApplicationForDrawback.setVisibility(View.GONE);
//                    } else {
//                        childViewHolder.tvApplicationForDrawback.setVisibility(View.VISIBLE);
//                        childViewHolder.tvApplicationForDrawback.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
////                                ToastUtils.msg("导游机/套票 申请退款");
////                                Intent intent = new Intent(context, ApplyRefundActivity.class);
////                                intent.putExtra("goodsOrderId", shopsBean.getGoodsOrderId());
////                                intent.putExtra("orderType", 1);
////                                context.startActivity(intent);
//                            }
//                        });
//                    }
//
//                    childViewHolder.tvCheckQrCode.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            ToastUtils.msg("导游机/套票 使用二维码");
//                        }
//                    });
//                    childViewHolder.llItem.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
////                            Intent intent = new Intent(context, GuideMachineOrSeasonTicketOrderWaitForUseActivity.class);
////                            intent.putExtra("goodsOrderId", shopsBean.getGoodsOrderId());
////                            context.startActivity(intent);
////                            Log.d("orderId", groupPosition + "   " + shopsBean.getGoodsOrderId());
//                        }
//                    });
//                } else if (pay_status == 2) {//待归还
////                groupViewHolder.tvPayStatus.setText("待归还");
//                    childViewHolder.tvDeleteOrder.setVisibility(View.GONE);//交易关闭
//                    childViewHolder.llWay.setVisibility(View.GONE);//待付款
//                    childViewHolder.llUnWriteOff.setVisibility(View.GONE);//待使用
//                    childViewHolder.llUnReturnBack.setVisibility(View.VISIBLE);//待归还
//                    childViewHolder.llSeeDetail.setVisibility(View.GONE);
//                    childViewHolder.llWaitingReceive.setVisibility(View.GONE);
//                    childViewHolder.llWaitingGetSelf.setVisibility(View.GONE);
//                    childViewHolder.llBuyAgain.setVisibility(View.GONE);
//                    childViewHolder.llApplyRefund.setVisibility(View.GONE);
//                    childViewHolder.tvReturnCode.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            ToastUtils.msg(" 导游机/套票 归还二维码");
//                        }
//                    });
//                    childViewHolder.llItem.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
////                            Intent intent = new Intent(context, GuideMachineOrSeasonTicketToBeReturnedActivity.class);
////                            intent.putExtra("goodsOrderId", shopsBean.getGoodsOrderId());
////                            context.startActivity(intent);
////                            Log.d("orderId", groupPosition + "   " + shopsBean.getGoodsOrderId());
//                        }
//                    });
//                } else if (pay_status == 3) {//已完成
//                    childViewHolder.tvDeleteOrder.setVisibility(View.GONE);//交易关闭
//                    childViewHolder.llWay.setVisibility(View.GONE);//待付款
//                    childViewHolder.llUnWriteOff.setVisibility(View.GONE);//待使用
//                    childViewHolder.llUnReturnBack.setVisibility(View.GONE);//待归还
//                    childViewHolder.llSeeDetail.setVisibility(View.GONE);//查看退款详情
//                    childViewHolder.llWaitingReceive.setVisibility(View.GONE);
//                    childViewHolder.llWaitingGetSelf.setVisibility(View.GONE);
//                    childViewHolder.llBuyAgain.setVisibility(View.VISIBLE);
//                    childViewHolder.llApplyRefund.setVisibility(View.GONE);
//                    childViewHolder.tvBuyAgain.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            ToastUtils.msg("导游机/套票 再来一单");
//                        }
//                    });
//
//                    childViewHolder.llItem.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
////                            Intent intent = new Intent(context, GuideMachineOrSeasonTicketOrderCompletedActivity.class);
////                            intent.putExtra("goodsOrderId", shopsBean.getGoodsOrderId());
////                            context.startActivity(intent);
////                            Log.d("orderId", groupPosition + "   " + shopsBean.getGoodsOrderId());
//                        }
//                    });
//                } else if (pay_status == 4) {//关闭交易
//                    childViewHolder.llWay.setVisibility(View.GONE);
//                    childViewHolder.tvDeleteOrder.setVisibility(View.GONE);
//                    childViewHolder.llUnWriteOff.setVisibility(View.GONE);
//                    childViewHolder.llUnReturnBack.setVisibility(View.GONE);//待归还
//                    childViewHolder.llSeeDetail.setVisibility(View.GONE);
//                    childViewHolder.llWaitingReceive.setVisibility(View.GONE);
//                    childViewHolder.llWaitingGetSelf.setVisibility(View.GONE);
//                    childViewHolder.llBuyAgain.setVisibility(View.GONE);
//                    childViewHolder.llApplyRefund.setVisibility(View.GONE);
//                    childViewHolder.llItem.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
////                            Intent intent = new Intent(context, GuideMachineOrSeasonTicketOrderClosedActivity.class);
////                            intent.putExtra("goodsOrderId", shopsBean.getGoodsOrderId());
////                            context.startActivity(intent);
////                            Log.d("orderId", groupPosition + "   " + shopsBean.getGoodsOrderId());
//                        }
//                    });
//                    childViewHolder.tvDeleteOrder.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            ToastUtils.msg("导游机/套票 删除订单");
////                            Log.d("orderId", groupPosition + "   " + shopsBean.getGoodsOrderId());
//                        }
//                    });
//                }
//            }
//        }

        if (order_type == 1) {//正常
            if (orderSearchStatus != 6) {
                //支付状态
       /*
          (0待付款1待发货2待自提3待收货4已完成5交易关闭6退款7售后)*/
                if (pay_status == 0) {
                    childViewHolder.llWay.setVisibility(View.VISIBLE);
                    childViewHolder.tvDeleteOrder.setVisibility(View.GONE);
                    childViewHolder.llUnWriteOff.setVisibility(View.GONE);
                    childViewHolder.llUnReturnBack.setVisibility(View.GONE);//待归还
                    childViewHolder.llSeeDetail.setVisibility(View.GONE);
                    childViewHolder.llWaitingReceive.setVisibility(View.GONE);
                    childViewHolder.llWaitingGetSelf.setVisibility(View.GONE);
                    childViewHolder.llApplyRefund.setVisibility(View.GONE);
                    childViewHolder.llBuyAgain.setVisibility(View.GONE);
                    childViewHolder.tvGoPay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            ToastUtils.msg("去支付");
                            Intent intent = new Intent(context, GoodsOrderPaymentActivity.class);
                            intent.putExtra("goodsOrderId", shopsBean.getOrderId());
                            intent.putExtra("orderType", 1);//微信支付默认都是1
                            intent.putExtra("flag", goodsBean.getFlag());
                            intent.putExtra("goodsId", goodsBean.getGoodsId());
                            if (goodsBean.getFlag()==0){
                                intent.putExtra("goodsSkuId", "");
                            }else {
                                intent.putExtra("goodsSkuId", goodsBean.getGoodsSkuId().toString());
                            }

                            intent.putExtra("goodsNum", goodsBean.getGoodsNum());
                            intent.putExtra("saleAmount", goodsBean.getUnitPrice());
                            intent.putExtra("goodsPriceId", goodsBean.getUnitPrice());
                            intent.putExtra("source","2");//此字段用来判断是从订单页面过来还是直接购买。1：直接购买 2：订单页面
                            context.startActivity(intent);
                        }
                    });
                    childViewHolder.tvCancelPay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            JSONObject requestData = new JSONObject();
                            try {
                                requestData.put("orderId", shopsBean.getOrderId());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestData.toString());
                            cancelOrderPresenter.cancelKindOrder(requestBody);//订单类型  (1:导游机/套票 2:购物)
                            myGroupPosition = groupPosition;
                            myChildPosition = childPosition;
                            showProgressDialog();
                        }
                    });
                    childViewHolder.llItem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, NoPaymentOrderDetailActivity.class);
                            intent.putExtra("orderId", shopsBean.getOrderId());
                            context.startActivity(intent);
                            Log.d("orderId", groupPosition + "   " + shopsBean.getOrderId());
                        }
                    });
                } else if (pay_status == 1) {//待发货
//                    childViewHolder.tvDeleteOrder.setVisibility(View.GONE);//交易关闭
//                    childViewHolder.llWay.setVisibility(View.GONE);//待付款
//                    childViewHolder.llUnWriteOff.setVisibility(View.GONE);//待使用
//                    childViewHolder.llUnReturnBack.setVisibility(View.GONE);//待归还
//                    childViewHolder.llSeeDetail.setVisibility(View.GONE);
//                    childViewHolder.llApplyRefund.setVisibility(View.VISIBLE);
//                    childViewHolder.llWaitingReceive.setVisibility(View.GONE);
//                    childViewHolder.llWaitingGetSelf.setVisibility(View.GONE);
//                    childViewHolder.llBuyAgain.setVisibility(View.GONE);
//                    if (shopsBean.getIsRefund() == 0) {//是否支持退款(0:不支持1:支持)
//                        childViewHolder.tvApplyRefund.setVisibility(View.GONE);
//                    } else {
//                        childViewHolder.tvApplyRefund.setVisibility(View.VISIBLE);
//                        childViewHolder.tvApplyRefund.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Intent intent = new Intent(context, ApplyRefundActivity.class);
//                                intent.putExtra("goodsOrderId", shopsBean.getGoodsOrderId());
//                                intent.putExtra("orderType", 2);
//                                context.startActivity(intent);
//                            }
//                        });
//                    }
//                    childViewHolder.llItem.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent intent = new Intent(context, WaitingSendGoodsDetailActivity.class);
//                            intent.putExtra("goodsOrderId", shopsBean.getGoodsOrderId());
//                            context.startActivity(intent);
//                            Log.d("orderId", groupPosition + "   " + shopsBean.getGoodsOrderId());
//                        }
//                    });
                } else if (pay_status == 2) {//待自提
                    childViewHolder.llWay.setVisibility(View.GONE);
                    childViewHolder.tvDeleteOrder.setVisibility(View.GONE);
                    childViewHolder.llUnWriteOff.setVisibility(View.GONE);
                    childViewHolder.llUnReturnBack.setVisibility(View.GONE);//待归还
                    childViewHolder.llSeeDetail.setVisibility(View.GONE);
                    childViewHolder.llWaitingReceive.setVisibility(View.GONE);
                    childViewHolder.llApplyRefund.setVisibility(View.GONE);
                    childViewHolder.llWaitingGetSelf.setVisibility(View.VISIBLE);
                    childViewHolder.llBuyAgain.setVisibility(View.GONE);
                    if (shopsBean.getIsRefund() == 0) {//是否支持退款(0:不支持1:支持)
                        childViewHolder.tvWaitingGetSelfApplyRefund.setVisibility(View.GONE);
                    } else {
                        childViewHolder.tvWaitingGetSelfApplyRefund.setVisibility(View.VISIBLE);
                        childViewHolder.tvWaitingGetSelfApplyRefund.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ToastUtils.msg("待自提 申请退款");
                                IntentUtils.openActivity(context, ApplyRefundActivity.class,"orderId", shopsBean.getOrderId());
                            }
                        });
                    }
                    childViewHolder.tvGetSelfQrCode.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            ToastUtils.msg("待自提 提货二维码");
                            GetSelfPop getSelfPop=new GetSelfPop(context,shopsBean.getOrderId());
                            getSelfPop.showPopupWindow(childViewHolder.tvGetSelfQrCode);
                        }
                    });
                    childViewHolder.llItem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, GetGoodsBySelfActivity.class);
                            intent.putExtra("orderId", shopsBean.getOrderId());
                            context.startActivity(intent);
                        }
                    });
                } else if (pay_status == 3) {//待收货
//                    childViewHolder.llWay.setVisibility(View.GONE);
//                    childViewHolder.tvDeleteOrder.setVisibility(View.GONE);
//                    childViewHolder.llUnWriteOff.setVisibility(View.GONE);
//                    childViewHolder.llUnReturnBack.setVisibility(View.GONE);
//                    childViewHolder.llSeeDetail.setVisibility(View.GONE);
//                    childViewHolder.llApplyRefund.setVisibility(View.GONE);
//                    childViewHolder.llWaitingReceive.setVisibility(View.VISIBLE);
//                    childViewHolder.llWaitingGetSelf.setVisibility(View.GONE);
//                    childViewHolder.llBuyAgain.setVisibility(View.GONE);
//
//                    childViewHolder.tvEnsureReceived.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            ToastUtils.msg("确认收货");
////                            confirmOrderPresenter.confirmOrder(shopsBean.getGoodsOrderId());
//                            myGroupPosition = groupPosition;
//                            myChildPosition = childPosition;
////                            Log.d("orderId", groupPosition + "   " + shopsBean.getGoodsOrderId());
//                        }
//                    });
//                    childViewHolder.llItem.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
////                            Intent intent = new Intent(context, ShippedGoodsDetailActivity.class);
////                            intent.putExtra("goodsOrderId", shopsBean.getGoodsOrderId());
////                            context.startActivity(intent);
//                        }
//                    });
                } else if (pay_status == 4) {//已完成
                    childViewHolder.tvDeleteOrder.setVisibility(View.GONE);//交易关闭
                    childViewHolder.llWay.setVisibility(View.GONE);//待付款
                    childViewHolder.llUnWriteOff.setVisibility(View.GONE);//待使用
                    childViewHolder.llUnReturnBack.setVisibility(View.GONE);//待归还
                    childViewHolder.llSeeDetail.setVisibility(View.GONE);
                    childViewHolder.llApplyRefund.setVisibility(View.GONE);
                    childViewHolder.llWaitingReceive.setVisibility(View.GONE);
                    childViewHolder.llWaitingGetSelf.setVisibility(View.GONE);
                    childViewHolder.llBuyAgain.setVisibility(View.VISIBLE);
                    childViewHolder.tvBuyAgain.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            ToastUtils.msg("实体商品 再来一单");
                            JSONObject requestData = new JSONObject();
                            try {
                                requestData.put("orderId", shopsBean.getOrderId());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestData.toString());
                            buyAgainPresenter.buyAgain(requestBody);
                            showProgressDialog();
                        }
                    });
                    childViewHolder.llItem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, OrderCompletedActivity.class);
                            intent.putExtra("orderId", shopsBean.getOrderId());
                            context.startActivity(intent);
                            Log.d("orderId", groupPosition + "   " + shopsBean.getOrderId());
                        }
                    });
                } else if (pay_status == 5) {//交易关闭
                    childViewHolder.tvDeleteOrder.setVisibility(View.GONE);//交易关闭
                    childViewHolder.llWay.setVisibility(View.GONE);//待付款
                    childViewHolder.llUnWriteOff.setVisibility(View.GONE);//待使用
                    childViewHolder.llUnReturnBack.setVisibility(View.GONE);//待归还
                    childViewHolder.llSeeDetail.setVisibility(View.GONE);
                    childViewHolder.llApplyRefund.setVisibility(View.GONE);
                    childViewHolder.llWaitingReceive.setVisibility(View.GONE);
                    childViewHolder.llWaitingGetSelf.setVisibility(View.GONE);
                    childViewHolder.llBuyAgain.setVisibility(View.GONE);
                    childViewHolder.llItem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            Intent intent = new Intent(context, OrderClosedActivity.class);
//                            intent.putExtra("goodsOrderId", shopsBean.getGoodsOrderId());
//                            intent.putExtra("orderType", 2);
//                            context.startActivity(intent);
                        }
                    });
                }
            } else if (pay_status == 6) {//退款
                childViewHolder.tvDeleteOrder.setVisibility(View.GONE);//交易关闭
                childViewHolder.llWay.setVisibility(View.GONE);//待付款
                childViewHolder.llUnWriteOff.setVisibility(View.GONE);//待使用
                childViewHolder.llUnReturnBack.setVisibility(View.GONE);//待归还
                childViewHolder.llSeeDetail.setVisibility(View.VISIBLE);
                childViewHolder.llApplyRefund.setVisibility(View.GONE);
                childViewHolder.llWaitingReceive.setVisibility(View.GONE);
                childViewHolder.llWaitingGetSelf.setVisibility(View.GONE);
                childViewHolder.llBuyAgain.setVisibility(View.GONE);
                childViewHolder.llSeeDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtils.msg("实体商品 查看详情");

//                        if (kindOrderType == 2) {//2:退款单3:售后单
//                            IntentUtils.openActivity(context, RefundDetailActivity.class);
//                        } else {
//
//                        }
                    }
                });
                childViewHolder.llItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent intent = new Intent(context, RefundOrderDetailActivity.class);
//                        intent.putExtra("goodsRefundId", shopsBean.getGoodsRefundId());
//                        context.startActivity(intent);
                    }
                });
            }
        } else if (order_type == 2) {//退款
            if (shopsBean.getRefundStatus() == 0) {//退款状态(0退款中1已退款)
                childViewHolder.tvDeleteOrder.setVisibility(View.GONE);//交易关闭
                childViewHolder.llWay.setVisibility(View.GONE);//待付款
                childViewHolder.llUnWriteOff.setVisibility(View.GONE);//待使用
                childViewHolder.llUnReturnBack.setVisibility(View.GONE);//待归还
                childViewHolder.llSeeDetail.setVisibility(View.VISIBLE);
                childViewHolder.llApplyRefund.setVisibility(View.GONE);
                childViewHolder.llWaitingReceive.setVisibility(View.GONE);
                childViewHolder.llWaitingGetSelf.setVisibility(View.GONE);
                childViewHolder.llBuyAgain.setVisibility(View.GONE);
                childViewHolder.llSeeDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, RefundOrderDetailActivity.class);
                        intent.putExtra("goodsRefundId", shopsBean.getGoodsRefundId());
                        context.startActivity(intent);
//                        ToastUtils.msg("实体商品 查看详情");
//                        if (kindOrderType == 2) {//2:退款单3:售后单
//                            IntentUtils.openActivity(context, RefundDetailActivity.class);
//                        } else {
//
//                        }
                    }
                });
                childViewHolder.llItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, RefundOrderDetailActivity.class);
                        intent.putExtra("goodsRefundId", shopsBean.getGoodsRefundId());
                        context.startActivity(intent);
                    }
                });
            } else {
                childViewHolder.tvDeleteOrder.setVisibility(View.GONE);//交易关闭
                childViewHolder.llWay.setVisibility(View.GONE);//待付款
                childViewHolder.llUnWriteOff.setVisibility(View.GONE);//待使用
                childViewHolder.llUnReturnBack.setVisibility(View.GONE);//待归还
                childViewHolder.llSeeDetail.setVisibility(View.GONE);
                childViewHolder.llApplyRefund.setVisibility(View.GONE);
                childViewHolder.llWaitingReceive.setVisibility(View.GONE);
                childViewHolder.llWaitingGetSelf.setVisibility(View.GONE);
                childViewHolder.llBuyAgain.setVisibility(View.GONE);
            }
        }
        return convertView;
    }

    static class ChildViewHolder {
        @BindView(R.id.iv_photo)
        ImageView ivPhoto;
        @BindView(R.id.tv_good_name)
        TextView tvGoodName;
        @BindView(R.id.ll_specification)
        LinearLayout llSpecification;
        @BindView(R.id.tv_specification)
        TextView tvSpecification;
        @BindView(R.id.tv_distribution)
        TextView tvDistribution;
        @BindView(R.id.ll_distribution)
        LinearLayout llDistribution;
        @BindView(R.id.tv_good_price)
        TextView tvGoodPrice;
        @BindView(R.id.tv_good_num)
        TextView tvGoodNum;
        @BindView(R.id.tv_date)
        TextView tvDate;
        @BindView(R.id.tv_pay_price)
        TextView tvPayPrice;
        @BindView(R.id.tv_cancel_pay)
        TextView tvCancelPay;
        @BindView(R.id.tv_go_pay)
        TextView tvGoPay;
        @BindView(R.id.ll_item)
        LinearLayout llItem;
        @BindView(R.id.ll_way)
        LinearLayout llWay;
        @BindView(R.id.tv_delete_order)
        TextView tvDeleteOrder;
        @BindView(R.id.ll_un_write_off)
        LinearLayout llUnWriteOff;
        @BindView(R.id.tv_application_for_drawback)
        TextView tvApplicationForDrawback;//申请退款
        @BindView(R.id.tv_check_qr_code)
        TextView tvCheckQrCode;//使用二维码
        @BindView(R.id.tv_deposit)//押金
                TextView tvDeposit;
        @BindView(R.id.ll_un_return_back)//归还二维码
                LinearLayout llUnReturnBack;
        @BindView(R.id.ll_see_detail)//查看详情
                LinearLayout llSeeDetail;
        @BindView(R.id.ll_waiting_receive)//待收货
                LinearLayout llWaitingReceive;
        @BindView(R.id.tv_ensure_received)
        TextView tvEnsureReceived;
        @BindView(R.id.ll_waiting_get_self)
        LinearLayout llWaitingGetSelf;
        @BindView(R.id.tv_return_code)
        TextView tvReturnCode;
        @BindView(R.id.ll_apply_refund)
        LinearLayout llApplyRefund;
        @BindView(R.id.tv_apply_refund)
        TextView tvApplyRefund;
        @BindView(R.id.tv_waiting_get_self_apply_refund)
        TextView tvWaitingGetSelfApplyRefund;
        @BindView(R.id.tv_get_self_qr_code)
        TextView tvGetSelfQrCode;
        @BindView(R.id.ll_buy_again)
        LinearLayout llBuyAgain;
        @BindView(R.id.tv_buy_again)
        TextView tvBuyAgain;

        ChildViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    BaseView baseView = new BaseView() {
        @Override
        public void onSuccess(BaseBean mBaseBean) {
            dismissProgressDialog();
            mList.getValue().getList().remove(myGroupPosition);
            notifyDataSetChanged();
            ToastUtils.msg(mBaseBean.getResultStatus().getResultMessage().toString());
        }

        @Override
        public void onError(String result) {
            dismissProgressDialog();
            ToastUtils.msg(result);
        }
    };
    BaseView baseView1 = new BaseView() {
        @Override
        public void onSuccess(BaseBean mBaseBean) {
            dismissProgressDialog();
            notifyDataSetChanged();
            ToastUtils.msg(mBaseBean.getResultStatus().getResultMessage().toString());
            IntentUtils.openActivity(context, ShoppingCartActivity.class);
        }

        @Override
        public void onError(String result) {
            dismissProgressDialog();
            ToastUtils.msg(result);
        }
    };

    //下面是加载框
    public void showProgressDialog() {
        logDialog.show();
    }

    /**
     * 对话框
     */
    protected void dismissProgressDialog() {
        if (logDialog == null) {
            return;
        }
        logDialog.dismiss();
    }
}
