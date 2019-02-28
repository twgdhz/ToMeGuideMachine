package com.guidemachine.ui.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.guidemachine.R;
import com.guidemachine.base.adapter.BaseRecyclerAdapter;
import com.guidemachine.base.adapter.BaseViewHolder;
import com.guidemachine.base.adapter.Listener.OnRecyclerItemClickListener;
import com.guidemachine.service.entity.ShopListBean;
import com.guidemachine.ui.activity.ShopDetailActivity;
import com.guidemachine.ui.activity.ShopListActivity;
import com.guidemachine.util.IntentUtils;

import java.util.List;

public class ShopListAdapter extends RecyclerView.Adapter<ShopListAdapter.BeautyViewHolder> {
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 数据集合
     */
    private List<ShopListBean.ListBean> data;
    BaseRecyclerAdapter adapter;

    public ShopListAdapter(List<ShopListBean.ListBean> data, Context context) {
        this.data = data;
        this.mContext = context;
    }

    @Override
    public BeautyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //加载item 布局文件
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop, parent, false);
        return new BeautyViewHolder(view);
    }

    LinearLayoutManager linearLayoutManager;

    @Override
    public void onBindViewHolder(BeautyViewHolder holder, final  int position) {
        final ShopListBean.ListBean bean = data.get(position);
        //将数据设置到item上
        Glide.with(mContext).load(data.get(position).getImageUrl()).into(holder.imgPhoto);
        holder.tvDistance.setText("距离您" + bean.getDistance() + "千米");
        holder.tvShopName.setText(bean.getName());
        holder.tvSales.setText("已售" + bean.getVolume() + "件");
        linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder.ryGoodList.setLayoutManager(linearLayoutManager);
        holder.llItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.openActivity(mContext, ShopDetailActivity.class, "shopId", bean.getShopId());
            }
        });
        adapter = new BaseRecyclerAdapter(mContext, data.get(position).getShopGoodsList(), R.layout.itme_shop_good_list) {
            @Override
            protected void convert(BaseViewHolder helper, Object item, int subPosition) {
                ShopListBean.ListBean.ShopGoodsListBean goodsListBean = data.get(position).getShopGoodsList().get(subPosition);
                helper.setText(R.id.tv_good_price_one, "￥" + goodsListBean.getPrice());
                Glide.with(mContext).load(goodsListBean.getGoodsImageUrl()).into((ImageView) helper.getView(R.id.img_goods_photo_one));
            }
        };
        holder.ryGoodList.setAdapter(adapter);
        adapter.setOnRecyclerItemClickListener(new OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                IntentUtils.openActivity(mContext, ShopDetailActivity.class, "shopId", bean.getShopId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size() > 3 ? 3 : data.size();
    }

    static class BeautyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPhoto;
        TextView tvShopName, tvDistance, tvSales;
        RecyclerView ryGoodList;
        LinearLayout llItem;

        public BeautyViewHolder(View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.img_shop_logo);
            tvShopName = itemView.findViewById(R.id.tv_shop_name);
            tvDistance = itemView.findViewById(R.id.tv_distance);
            tvSales = itemView.findViewById(R.id.tv_sales);
            ryGoodList = itemView.findViewById(R.id.ry_good_list);
            llItem = itemView.findViewById(R.id.ll_item);
        }
    }
}
