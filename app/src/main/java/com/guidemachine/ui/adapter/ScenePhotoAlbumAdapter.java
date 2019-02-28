package com.guidemachine.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.guidemachine.R;

import java.util.List;
/**
* @author ChenLinWang
* @email 422828518@qq.com
* @create 2019/1/25 0025 14:23
* description: 作废
*/
public class ScenePhotoAlbumAdapter extends RecyclerView.Adapter<ScenePhotoAlbumAdapter.BeautyViewHolder> {
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 数据集合
     */
    private List<String> data;

    public ScenePhotoAlbumAdapter(List<String> data, Context context) {
        this.data = data;
        this.mContext = context;
    }

    @Override
    public BeautyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //加载item 布局文件
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_scene_photo_album, parent, false);
        return new BeautyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BeautyViewHolder holder, int position) {
        //将数据设置到item上
        Glide.with(mContext).load(data.get(position)).into(holder.imgPhoto);
//        holder.imgPhoto.setImageResource(data.get(position));
    }

    @Override
    public void onBindViewHolder(@NonNull BeautyViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class BeautyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPhoto;
        public BeautyViewHolder(View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.img_photo);
        }
    }
}
