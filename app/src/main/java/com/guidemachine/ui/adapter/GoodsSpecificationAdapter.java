package com.guidemachine.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guidemachine.R;
import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.GoodSpecBean;
import com.guidemachine.util.Logger;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 商品规格列表
 */
public class GoodsSpecificationAdapter extends RecyclerView.Adapter<GoodsSpecificationAdapter.BeautyViewHolder> {
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 数据集合
     */
    BaseBean<GoodSpecBean> getSpecificsBean;
    private List<String> subSpecification;//子规格集合
    private Map<String, String> skuMap = new HashMap<>();
    //    StringBuilder stringBuilder = new StringBuilder();
    private String skuString = null;
    private GetStock getStock;

    public void setGetStock(GetStock getStock) {
        this.getStock = getStock;
    }

    public GoodsSpecificationAdapter(BaseBean<GoodSpecBean> getSpecificsBean, Context context) {
        this.getSpecificsBean = getSpecificsBean;
        this.mContext = context;
    }

    @Override
    public BeautyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //加载item 布局文件
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_goods_specification, parent, false);
        return new BeautyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final BeautyViewHolder holder, final int position) {
        final List<GoodSpecBean.ParentSpecListBean> list = getSpecificsBean.getValue().getParentSpecList();
        //将数据设置到item上
        holder.tvTitle.setText(list.get(position).getName());
        subSpecification = new ArrayList<>();
        for (int i = 0; i < list.get(position).getChildSpecList().size(); i++) {
            subSpecification.add(list.get(position).getChildSpecList().get(i).getName());
            //子规格
            holder.ryGoodList.setAdapter(new TagAdapter<String>(subSpecification) {
                @Override
                public View getView(FlowLayout parent, int position, String s) {
                    TextView text = (TextView) LayoutInflater.from(mContext).inflate(R.layout.tag_souvenir_color, holder.ryGoodList, false);
                    text.setText(s);
                    return text;
                }
            });
            holder.ryGoodList.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
                @Override
                public void onSelected(Set<Integer> selectPosSet) {

                }
            });
            holder.ryGoodList.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                @Override
                public boolean onTagClick(View view, int subPosition, FlowLayout parent) {
                    Logger.d("父规格", list.get(position).getName());
                    Logger.d("子规格", list.get(position).getChildSpecList().get(subPosition).getName());
                    skuMap.put(list.get(position).getName(), list.get(position).getChildSpecList().get(subPosition).getName());
                    Set<String> keys = skuMap.keySet();
                    StringBuilder stringBuilder = new StringBuilder();
                    for (String key : keys) {
                        stringBuilder.append(key).append(":").append(skuMap.get(key)).append(",");
                    }
                    skuString = stringBuilder.substring(0, stringBuilder.length() - 1);
                    Logger.d("选择后拼接后的规格", skuString);
                    Logger.d("选中集合：  ", keys.size() + "");
                    if (keys.size() == list.size()) {
                        GoodSpecBean.GoodsSkuListBean goodsSkuListBean = getSpec(skuString);
//                        EventBus.getDefault().post(new MessageEventStock(goodsSkuListBean));
                        if (getStock != null) {
                            getStock.stockNum(goodsSkuListBean);
                        }
                    }
                    return false;
                }
            });
        }

    }


    @Override
    public int getItemCount() {
        return getSpecificsBean.getValue().getParentSpecList().size();
    }

    static class BeautyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TagFlowLayout ryGoodList;
        LinearLayout ll_specification;

        public BeautyViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            ryGoodList = itemView.findViewById(R.id.tag);
            ll_specification = itemView.findViewById(R.id.ll_specification);
        }
    }

    public GoodSpecBean.GoodsSkuListBean getSpec(String goodsName) {//获取下一个接口的请求参数
//        goodsName = "颜色:红色,尺码:M";
//goodsSkuList
        String[] specific = goodsName.split(",");
        GoodSpecBean.GoodsSkuListBean resultSku = null;
        for (int i = 0; i < getSpecificsBean.getValue().getGoodsSkuList().size(); i++) {
            GoodSpecBean.GoodsSkuListBean tmKindGoodsSkuInfo = getSpecificsBean.getValue().getGoodsSkuList().get(i);
            boolean flag = true;
            for (int j = 0; j < specific.length; j++) {
                if (tmKindGoodsSkuInfo.getSkuName().indexOf(specific[j]) == -1) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                resultSku = tmKindGoodsSkuInfo;
                break;
            }
        }
        return resultSku;
    }

    private List<String> getString() {//[颜色-红色;黄色, 尺码-M;L;XL]
        List<String> nameList = new ArrayList<>();
        for (GoodSpecBean.ParentSpecListBean parentSpec : getSpecificsBean.getValue().getParentSpecList()) {
            StringBuilder name = new StringBuilder();
            name.append(parentSpec.getName()).append("-");
            for (GoodSpecBean.ParentSpecListBean.ChildSpecListBean childSpec : parentSpec.getChildSpecList()) {
                name.append(childSpec.getName()).append(";");
            }
            nameList.add(name.substring(0, name.toString().length() - 1));
        }
        return nameList;
    }

    public interface GetStock {
        public void stockNum(GoodSpecBean.GoodsSkuListBean goodsSkuListBean);
    }
}
