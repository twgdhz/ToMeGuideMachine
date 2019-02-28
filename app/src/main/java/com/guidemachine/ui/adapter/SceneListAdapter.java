//package com.guidemachine.ui.adapter;
//
//import android.content.Context;
//import android.graphics.drawable.AnimationDrawable;
//import android.support.annotation.NonNull;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//import com.guidemachine.R;
//import com.guidemachine.service.entity.BaseBean;
//import com.guidemachine.service.entity.ScenerySpotListBean;
//import com.guidemachine.ui.activity.SceneListActivity;
//import com.guidemachine.util.VoiceUtil;
//import com.guidemachine.util.share.SPHelper;
//
//import java.text.DecimalFormat;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @author ChenLinWang
// * @email 422828518@qq.com
// * @create 2019/1/25 0025 14:24
// * description: 景点列表
// */
//public class SceneListAdapter extends RecyclerView.Adapter<SceneListAdapter.BeautyViewHolder> {
//    /**
//     * 上下文
//     */
//    private Context mContext;
//    /**
//     * 数据集合
//     */
//    private BaseBean<ScenerySpotListBean> data;
//    double userLongitude;
//    double userLatitude;
//    private AnimationDrawable drawable;
//    private List<Boolean> isClicks = new ArrayList<>();//控件是否被点击,默认为false，如果被点击，改变值
//
//    public SceneListAdapter(BaseBean<ScenerySpotListBean> data, Context context) {
//
//        this.data = data;
//        this.mContext = context;
//        for(int i = 0;i<data.getValue().getList().size();i++){
//            isClicks.add(false);
//        }
//    }
//
//    @Override
//    public BeautyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        //加载item 布局文件
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_scene_list, parent, false);
//        return new BeautyViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(final BeautyViewHolder holder, final int position) {
//        holder.tvSpotName.setText(data.getValue().getList().get(position).getName());
//        double sceneSpotLongitude = data.getValue().getList().get(position).getLng();
//        double sceneSpotLatitude = data.getValue().getList().get(position).getLat();
//
//        userLongitude = Double.parseDouble(SPHelper.getInstance(mContext).getLongitude());
//        userLatitude = Double.parseDouble(SPHelper.getInstance(mContext).getLatitude());
//        DecimalFormat df = new DecimalFormat("#.00");
//        double distance = getDistance(userLongitude, userLatitude, sceneSpotLongitude, sceneSpotLatitude);
//        if (distance >= 1000) {
//            String str = df.format(distance / 1000);
//            holder.tvDistance.setText(str + "千米");
//        } else {
//            String str = df.format(distance);
//            holder.tvDistance.setText(str + "米");
//        }
//
//        holder.imgPlayVoice.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                for(int i = 0; i <isClicks.size();i++){
//                    isClicks.set(i,false);
//                }
//                isClicks.set(position,true);
//                notifyDataSetChanged();
//                if(isClicks.get(position)==true){
//                    drawable = (AnimationDrawable) holder.imgPlayVoice.getBackground();
//                    if (drawable != null) {
//                        drawable.start();
//                    }
//                    VoiceUtil.getInstance().modeIndicater(mContext, data.getValue().getList().get(position).getScenerySpotId());
//                }else{
//
//                }
//            }
//        });
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull BeautyViewHolder holder, int position, @NonNull List<Object> payloads) {
//        super.onBindViewHolder(holder, position, payloads);
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return data.getValue().getList().size();
//    }
//
//    static class BeautyViewHolder extends RecyclerView.ViewHolder {
//        ImageView imgPhoto, imgPlayVoice;
//        RelativeLayout rlNavi;
//        TextView tvDistance, tvSpotName;
//
//        public BeautyViewHolder(View itemView) {
//            super(itemView);
//            imgPhoto = itemView.findViewById(R.id.img_photo);
//            imgPlayVoice = itemView.findViewById(R.id.img_play_voice);
//            rlNavi = itemView.findViewById(R.id.rl_navi);
//            tvDistance = itemView.findViewById(R.id.tv_scene_spot_distance);
//            tvSpotName = itemView.findViewById(R.id.tv_scene_spot_name);
//        }
//    }
//
//    /**
//     * 计算两经纬度距离
//     *
//     * @param long1 经度1
//     * @param lat1  维度1
//     * @param long2 经度2
//     * @param lat2  纬度2
//     * @return
//     */
//    public double getDistance(double long1, double lat1, double long2,
//                              double lat2) {
//        double a, b, R;
//        R = 6378137; // 地球半径
//        lat1 = lat1 * Math.PI / 180.0;
//        lat2 = lat2 * Math.PI / 180.0;
//        a = lat1 - lat2;
//        b = (long1 - long2) * Math.PI / 180.0;
//        double d;
//        double sa2, sb2;
//        sa2 = Math.sin(a / 2.0);
//        sb2 = Math.sin(b / 2.0);
//        d = 2 * R * Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(lat1) * Math.cos(lat2) * sb2 * sb2));
//        return d;
//    }
//}
