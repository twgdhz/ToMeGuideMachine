package com.guidemachine.ui.activity.shop.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.guidemachine.R;
import com.guidemachine.service.entity.ShoppingCartListBean;
import com.guidemachine.ui.activity.shop.ShoppingCartGoodsOrderInputActivity;
import com.guidemachine.util.ToastUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 购物车的adapter
 * 因为使用的是ExpandableListView，所以继承BaseExpandableListAdapter
 */
public class ShoppingCarAdapter extends BaseExpandableListAdapter {

    private final Context context;
    private final LinearLayout llSelectAll;
    private final ImageView ivSelectAll;
    private final Button btnOrder;
    private final Button btnDelete;
    private final RelativeLayout rlTotalPrice;
    private final TextView tvTotalPrice;
    private List<ShoppingCartListBean.RecordsBean> data;
    private boolean isSelectAll = false;
    private double total_price;
    //创建字符串 存储选中的商品id
    StringBuilder sb = new StringBuilder();

    public ShoppingCarAdapter(Context context, LinearLayout llSelectAll,
                              ImageView ivSelectAll, Button btnOrder, Button btnDelete,
                              RelativeLayout rlTotalPrice, TextView tvTotalPrice) {
        this.context = context;
        this.llSelectAll = llSelectAll;
        this.ivSelectAll = ivSelectAll;
        this.btnOrder = btnOrder;
        this.btnDelete = btnDelete;
        this.rlTotalPrice = rlTotalPrice;
        this.tvTotalPrice = tvTotalPrice;
    }

    /**
     * 自定义设置数据方法；
     * 通过notifyDataSetChanged()刷新数据，可保持当前位置
     *
     * @param data 需要刷新的数据
     */
    public void setData(List<ShoppingCartListBean.RecordsBean> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        if (data != null && data.size() > 0) {
            return data.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        return data.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_shopping_car_group, null);
            groupViewHolder = new GroupViewHolder(convertView);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        final ShoppingCartListBean.RecordsBean datasBean = data.get(groupPosition);
        //店铺ID
        String store_id = datasBean.getId();
        //店铺名称
        String store_name = datasBean.getName();

        if (store_name != null) {
            groupViewHolder.tvStoreName.setText(store_name);
        } else {
            groupViewHolder.tvStoreName.setText("");
        }

        //店铺内的商品都选中的时候，店铺的也要选中
        for (int i = 0; i < datasBean.getKindGoodsItems().size(); i++) {
            ShoppingCartListBean.RecordsBean.KindGoodsItemsBean goodsBean = datasBean.getKindGoodsItems().get(i);
            boolean isSelect = goodsBean.getIsSelect();
            if (isSelect) {
                datasBean.setIsSelect_shop(true);
            } else {
                datasBean.setIsSelect_shop(false);
                break;
            }
        }

        //因为set之后要重新get，所以这一块代码要放到一起执行
        //店铺是否在购物车中被选中
        final boolean isSelect_shop = datasBean.getIsSelect_shop();
        if (isSelect_shop) {
            groupViewHolder.ivSelect.setImageResource(R.mipmap.ic_home_souvenir_radio_selected);
        } else {
            groupViewHolder.ivSelect.setImageResource(R.mipmap.ic_home_souvenir_radio_default);
        }

        //店铺选择框的点击事件
        groupViewHolder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datasBean.setIsSelect_shop(!isSelect_shop);

                List<ShoppingCartListBean.RecordsBean.KindGoodsItemsBean> goods = datasBean.getKindGoodsItems();
                for (int i = 0; i < goods.size(); i++) {
                    ShoppingCartListBean.RecordsBean.KindGoodsItemsBean goodsBean = goods.get(i);
                    goodsBean.setIsSelect(!isSelect_shop);
                    if (goodsBean.getIsSelect() == true) {
                        set.add(goodsBean.getGoodsItemId());
                    } else {
                        set.remove(goodsBean.getGoodsItemId());
                    }

                }
                notifyDataSetChanged();
            }
        });

        //当所有的选择框都是选中的时候，全选也要选中
        w:
        for (int i = 0; i < data.size(); i++) {
            List<ShoppingCartListBean.RecordsBean.KindGoodsItemsBean> goods = data.get(i).getKindGoodsItems();
            for (int y = 0; y < goods.size(); y++) {
                ShoppingCartListBean.RecordsBean.KindGoodsItemsBean goodsBean = goods.get(y);
                boolean isSelect = goodsBean.getIsSelect();
                if (isSelect) {
                    isSelectAll = true;
                } else {
                    isSelectAll = false;
                    break w;//根据标记，跳出嵌套循环
                }
            }
        }
        if (isSelectAll) {
            ivSelectAll.setBackgroundResource(R.mipmap.ic_home_souvenir_radio_selected);
        } else {
            ivSelectAll.setBackgroundResource(R.mipmap.ic_home_souvenir_radio_default);
        }

        //全选的点击事件
        llSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSelectAll = !isSelectAll;

                if (isSelectAll) {
                    for (int i = 0; i < data.size(); i++) {
                        List<ShoppingCartListBean.RecordsBean.KindGoodsItemsBean> goods = data.get(i).getKindGoodsItems();
                        for (int y = 0; y < goods.size(); y++) {
                            ShoppingCartListBean.RecordsBean.KindGoodsItemsBean goodsBean = goods.get(y);
                            goodsBean.setIsSelect(true);
                            set.add(goodsBean.getGoodsItemId());
                        }
                    }
                } else {
                    for (int i = 0; i < data.size(); i++) {
                        List<ShoppingCartListBean.RecordsBean.KindGoodsItemsBean> goods = data.get(i).getKindGoodsItems();
                        for (int y = 0; y < goods.size(); y++) {
                            ShoppingCartListBean.RecordsBean.KindGoodsItemsBean goodsBean = goods.get(y);
                            goodsBean.setIsSelect(false);
                            set.remove(goodsBean.getGoodsItemId());
                        }
                    }
                }
                notifyDataSetChanged();
            }
        });

        //合计的计算
        total_price = 0.0;
        tvTotalPrice.setText("¥0.00");
        for (int i = 0; i < data.size(); i++) {
            List<ShoppingCartListBean.RecordsBean.KindGoodsItemsBean> goods = data.get(i).getKindGoodsItems();
            for (int y = 0; y < goods.size(); y++) {
                ShoppingCartListBean.RecordsBean.KindGoodsItemsBean goodsBean = goods.get(y);
                boolean isSelect = goodsBean.getIsSelect();
                if (isSelect) {
                    String num = goodsBean.getGoodsNum() + "";
                    String price = goodsBean.getSaleAmount() + "";

                    double v = Double.parseDouble(num);
                    double v1 = Double.parseDouble(price);

                    total_price = total_price + v * v1;

                    //让Double类型完整显示，不用科学计数法显示大写字母E
                    DecimalFormat decimalFormat = new DecimalFormat("0.00");
                    tvTotalPrice.setText("¥" + decimalFormat.format(total_price));
                }
            }
        }

        //去结算的点击事件
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //创建临时的List，用于存储被选中的商品
                List<ShoppingCartListBean.RecordsBean.KindGoodsItemsBean> temp = new ArrayList<>();
                for (int i = 0; i < data.size(); i++) {
                    List<ShoppingCartListBean.RecordsBean.KindGoodsItemsBean> goods = data.get(i).getKindGoodsItems();
                    for (int y = 0; y < goods.size(); y++) {
                        ShoppingCartListBean.RecordsBean.KindGoodsItemsBean goodsBean = goods.get(y);
                        boolean isSelect = goodsBean.getIsSelect();
                        if (isSelect) {
                            temp.add(goodsBean);
                        }
                    }
                }

                if (temp != null && temp.size() > 0) {//如果有被选中的
                    /**
                     * 实际开发中，如果有被选中的商品，
                     * 则跳转到确认订单页面，完成后续订单流程。
                     */
//                    ToastUtils.msg("跳转到确认订单页面，完成后续订单流程");
                    sb.delete(0, sb.length());
                    for (String string : set) {
                        sb.append(string + ",");
                    }
                    Intent intent = new Intent(context, ShoppingCartGoodsOrderInputActivity.class);
                    intent.putExtra("goodsItemIds", sb.toString());
                    intent.putExtra("totalPrice", tvTotalPrice.getText().toString());
                    context.startActivity(intent);
                } else {
                    ToastUtils.msg("请选择要购买的商品");
                }
            }
        });

        //删除的点击事件
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 实际开发中，通过回调请求后台接口实现删除操作
                 */
                if (mDeleteListener != null) {
                    mDeleteListener.onDelete(set);
                }
            }
        });

        return convertView;
    }

    static class GroupViewHolder {
        @BindView(R.id.iv_select)
        ImageView ivSelect;
        @BindView(R.id.tv_store_name)
        TextView tvStoreName;
        @BindView(R.id.ll)
        LinearLayout ll;

        GroupViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    //------------------------------------------------------------------------------------------------
    @Override
    public int getChildrenCount(int groupPosition) {
        if (data.get(groupPosition).getKindGoodsItems() != null && data.get(groupPosition).getKindGoodsItems().size() > 0) {
            return data.get(groupPosition).getKindGoodsItems().size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return data.get(groupPosition).getKindGoodsItems().get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    Set<String> set = new HashSet<String>();//存储选中的goodsid

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_shopping_car_child, null);

            childViewHolder = new ChildViewHolder(convertView);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        final ShoppingCartListBean.RecordsBean datasBean = data.get(groupPosition);
        //店铺ID
        String store_id = datasBean.getId();
        //店铺名称
        String store_name = datasBean.getName();
        //店铺是否在购物车中被选中
        final boolean isSelect_shop = datasBean.getIsSelect_shop();
        final ShoppingCartListBean.RecordsBean.KindGoodsItemsBean goodsBean = datasBean.getKindGoodsItems().get(childPosition);
        //商品图片
        String goods_image = goodsBean.getImageUrl();
        //商品ID
        final String goods_id = goodsBean.getGoodsId();
        //商品名称
        String goods_name = goodsBean.getGoodsName();
        //商品价格
        String goods_price = goodsBean.getSaleAmount() + "";
        //商品数量
        String goods_num = goodsBean.getGoodsNum() + "";
        //商品是否被选中
        final boolean isSelect = goodsBean.getIsSelect();
        //配送方式
        int self_type = goodsBean.getSelfType();

        Glide.with(context)
                .load(goods_image)
                .into(childViewHolder.ivPhoto);
        if (goods_name != null) {
            childViewHolder.tvName.setText(goods_name);
        } else {
            childViewHolder.tvName.setText("");
        }
        if (goods_price != null) {
            childViewHolder.tvPriceValue.setText(goods_price);
        } else {
            childViewHolder.tvPriceValue.setText("");
        }
        if (goods_num != null) {
            childViewHolder.tvEditBuyNumber.setText(goods_num);
        } else {
            childViewHolder.tvEditBuyNumber.setText("");
        }

        //商品是否被选中
        if (isSelect) {
            childViewHolder.ivSelect.setImageResource(R.mipmap.ic_home_souvenir_radio_selected);
        } else {
            childViewHolder.ivSelect.setImageResource(R.mipmap.ic_home_souvenir_radio_default);
        }
        if (self_type == 0) {
            childViewHolder.tvWay.setText("配送");
        } else if (self_type == 1) {
            childViewHolder.tvWay.setText("自提");
        } else if (self_type == 2) {
            childViewHolder.tvWay.setText("自提、包邮");
        }
        //商品选择框的点击事件
        childViewHolder.ivSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goodsBean.setIsSelect(!isSelect);
                if (goodsBean.getIsSelect() == true) {
                    set.add(goodsBean.getGoodsItemId());
                } else {
                    set.remove(goodsBean.getGoodsItemId());
                }
                if (!isSelect == false) {
                    datasBean.setIsSelect_shop(false);
                }
                notifyDataSetChanged();
            }
        });

        //加号的点击事件
        childViewHolder.ivEditAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //模拟加号操作
                int num = goodsBean.getGoodsNum();
                Integer integer = Integer.valueOf(num);
                integer++;
                goodsBean.setGoodsNum(integer);
                notifyDataSetChanged();

                /**
                 * 实际开发中，通过回调请求后台接口实现数量的加减
                 */
                if (mChangeCountListener != null) {
                    mChangeCountListener.onChangeCount(goodsBean);
                }
            }
        });
        //减号的点击事件
        childViewHolder.ivEditSubtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //模拟减号操作
                int num = goodsBean.getGoodsNum();
                Integer integer = Integer.valueOf(num);
                if (integer > 1) {
                    integer--;
                    goodsBean.setGoodsNum(integer);

                    /**
                     * 实际开发中，通过回调请求后台接口实现数量的加减
                     */
                    if (mChangeCountListener != null) {
                        mChangeCountListener.onChangeCount(goodsBean);
                    }
                } else {
                    ToastUtils.msg("商品不能再减少了");
                }
                notifyDataSetChanged();
            }
        });

        if (childPosition == data.get(groupPosition).getKindGoodsItems().size() - 1) {
            childViewHolder.view.setVisibility(View.GONE);
            childViewHolder.viewLast.setVisibility(View.VISIBLE);
        } else {
            childViewHolder.view.setVisibility(View.VISIBLE);
            childViewHolder.viewLast.setVisibility(View.GONE);
        }

        return convertView;
    }

    static class ChildViewHolder {
        @BindView(R.id.iv_select)
        ImageView ivSelect;
        @BindView(R.id.iv_photo)
        ImageView ivPhoto;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_price_key)
        TextView tvPriceKey;
        @BindView(R.id.tv_price_value)
        TextView tvPriceValue;
        @BindView(R.id.iv_edit_subtract)
        ImageView ivEditSubtract;
        @BindView(R.id.tv_edit_buy_number)
        TextView tvEditBuyNumber;
        @BindView(R.id.iv_edit_add)
        ImageView ivEditAdd;
        @BindView(R.id.view)
        View view;
        @BindView(R.id.view_last)
        View viewLast;
        @BindView(R.id.tv_way)
        TextView tvWay;

        ChildViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    //-----------------------------------------------------------------------------------------------

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    //删除的回调
    public interface OnDeleteListener {
        void onDelete(Set<String> goodsItemId);
    }

    public void setOnDeleteListener(OnDeleteListener listener) {
        mDeleteListener = listener;
    }

    private OnDeleteListener mDeleteListener;

    //修改商品数量的回调
    public interface OnChangeCountListener {
        void onChangeCount(ShoppingCartListBean.RecordsBean.KindGoodsItemsBean goods_id);
    }

    public void setOnChangeCountListener(OnChangeCountListener listener) {
        mChangeCountListener = listener;
    }

    private OnChangeCountListener mChangeCountListener;
}
