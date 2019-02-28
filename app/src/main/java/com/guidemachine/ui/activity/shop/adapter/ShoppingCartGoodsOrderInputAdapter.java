package com.guidemachine.ui.activity.shop.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.guidemachine.R;
import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.CartKindOrderFillingBean;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 购物车订单填写页面
 */
public class ShoppingCartGoodsOrderInputAdapter extends BaseExpandableListAdapter {
    private Context context;
    BaseBean<List<CartKindOrderFillingBean>> mList;
    private String totalPrice;

    public ShoppingCartGoodsOrderInputAdapter(Context context, BaseBean<List<CartKindOrderFillingBean>> mList) {
        this.context = context;
        this.mList = mList;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public int getGroupCount() {
        if (mList.getValue() != null && mList.getValue().size() > 0) {
            return mList.getValue().size();
        } else {
            return 0;
        }
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (mList.getValue().get(groupPosition).getKindOrderItems() != null && mList.getValue().get(groupPosition).getKindOrderItems().size() > 0) {
            return mList.getValue().get(groupPosition).getKindOrderItems().size();
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
        return mList.getValue().get(groupPosition).getKindOrderItems().get(childPosition);
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
            convertView = View.inflate(context, R.layout.item_shopping_cart_goods_order_input_group, null);
            groupViewHolder = new GroupViewHolder(convertView);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        //店铺ID
        String store_id = mList.getValue().get(groupPosition).getId();
        //店铺名称
        String store_name = mList.getValue().get(groupPosition).getName();
        if (store_name != null) {
            groupViewHolder.tvStoreName.setText(store_name);
        }
        return convertView;
    }

    static class GroupViewHolder {
        @BindView(R.id.tv_store_name)
        TextView tvStoreName;

        GroupViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_shopping_cart_goods_order_input_child, null);
            childViewHolder = new ChildViewHolder(convertView);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        CartKindOrderFillingBean.KindOrderItemsBean goodsBean = mList.getValue().get(groupPosition).getKindOrderItems().get(childPosition);
        //商品图片
        String goods_image = goodsBean.getImageUrl();
        //商品ID
        final String goods_id = goodsBean.getGoodsId();
        //商品规格
        final String goods_specification = goodsBean.getGoodsSkuName();
        //商品名称
        String goods_name = goodsBean.getGoodsName();
        //商品价格
        String goods_price = goodsBean.getUnitPrice() + "";
        //商品数量
        BigDecimal goods_num = goodsBean.getGoodsNum();
        //是否有折扣
        int good_isDiscount = goodsBean.getIsDiscountsPrice();//是否有优惠 (0:无优惠1:有优惠)
        //商品折扣
        BigDecimal goods_discount = goodsBean.getDiscountsPrice();
        //配送方式
        int goods_selfType = goodsBean.getSelfType();
        //用户选择的配送方式
        Integer user_selfType = goodsBean.getUserSelfType();
        Glide.with(context)
                .load(goods_image)
                .into(childViewHolder.ivPhoto);
        if (goods_name != null) {
            childViewHolder.tvName.setText(goods_name);
        }
        childViewHolder.tvCount.setText("数量X" + goods_num);
        childViewHolder.tvSpecification.setText(goods_specification);
        childViewHolder.tvPriceValue.setText(goods_price + "");
        if (good_isDiscount == 0) {
            childViewHolder.tvDiscount.setVisibility(View.GONE);
        } else {
            childViewHolder.tvDiscount.setVisibility(View.VISIBLE);
            childViewHolder.tvDiscount.setText("优惠" + goods_discount.multiply(goods_num));
        }

//        childViewHolder.tvIsBill.setText(goods_specification);
        //用户姓名、电话
        if (goodsBean == null) {
            return null;
        }
        if (user_selfType != null) {
            if (user_selfType == 0) {
                childViewHolder.tvDistributionWay.setText("配送");
                childViewHolder.llAddress.setVisibility(View.VISIBLE);
                childViewHolder.llGetSelf.setVisibility(View.GONE);
                if (goodsBean.getCenterTransport() != null) {
                    //用户地址
                    String address = goodsBean.getCenterTransport().getAddress() == null ? "" : goodsBean.getCenterTransport().getAddress();
                    String name_phone = goodsBean.getCenterTransport().getLinkName() + goodsBean.getCenterTransport().getTelephone();
                    childViewHolder.tvGetDistributionNamePhone.setText(name_phone);
                    childViewHolder.tvGetDistributionAddress.setText(address);
                }

            } else if (user_selfType == 1) {
                childViewHolder.tvDistributionWay.setText("自提");
                childViewHolder.llAddress.setVisibility(View.GONE);
                childViewHolder.llGetSelf.setVisibility(View.VISIBLE);
                childViewHolder.tvGetSelfNamePhone.setText(mList.getValue().get(groupPosition).getName() + " " + goodsBean.getTelephone());
                childViewHolder.tvGetSelfAddress.setText(goodsBean.getAddress() == null ? "" : goodsBean.getAddress().toString());
                if (goodsBean.getIsOrderDue() == 0) {//是否有订单有效期  (0:无1:有)
                    childViewHolder.tvOrderDue.setVisibility(View.GONE);
                } else {
                    childViewHolder.tvOrderDue.setVisibility(View.VISIBLE);
                    childViewHolder.tvOrderDue.setText("请于" + goodsBean.getOrderDue() + "个工作日之内自提，否则过期作废");
                }
            }
            if (mList.getValue().get(groupPosition).getIsBill().equals("1")) {//是否支持发票 (0:否 1:是)
                childViewHolder.tvIsBill.setText("支持");
            } else {
                childViewHolder.tvIsBill.setText("不支持");
            }

        } else {
            childViewHolder.tvDistributionWay.setText("请选择");
            childViewHolder.llAddress.setVisibility(View.GONE);
            childViewHolder.llGetSelf.setVisibility(View.GONE);
        }
        return convertView;
    }

    static class ChildViewHolder {
        @BindView(R.id.iv_photo)
        ImageView ivPhoto;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_count)
        TextView tvCount;
        @BindView(R.id.tv_specification)
        TextView tvSpecification;
        @BindView(R.id.tv_get_distribution_name_phone)
        TextView tvGetDistributionNamePhone;
        @BindView(R.id.tv_get_distribution_address)
        TextView tvGetDistributionAddress;
        @BindView(R.id.tv_is_bill)
        TextView tvIsBill;
        @BindView(R.id.tv_distribution_way)
        TextView tvDistributionWay;
        @BindView(R.id.tv_price_value)
        TextView tvPriceValue;
        @BindView(R.id.ll_address)
        LinearLayout llAddress;
        @BindView(R.id.ll_get_self)
        LinearLayout llGetSelf;
        @BindView(R.id.tv_get_self_name_phone)
        TextView tvGetSelfNamePhone;
        @BindView(R.id.tv_get_self_address)
        TextView tvGetSelfAddress;
        @BindView(R.id.tv_order_due)
        TextView tvOrderDue;
        @BindView(R.id.tv_discount)
        TextView tvDiscount;

        ChildViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
