package com.guidemachine.ui.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.guidemachine.R;

public class GridViewAdapter extends BaseAdapter {
    private Context context;
    private List<String> list;
    LayoutInflater layoutInflater;
    private ImageView mImageView;
    private TextView tvMore;

    public GridViewAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size() + 1;//注意此处
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = layoutInflater.inflate(R.layout.item_tour_history, null);
        mImageView = (ImageView) convertView.findViewById(R.id.item);
        tvMore = convertView.findViewById(R.id.tv_more);
        if (position < list.size()) {
            Glide.with(context).load(list.get(position)).into(mImageView);
            tvMore.setVisibility(View.GONE);
        } else {
            mImageView.setBackgroundResource(R.mipmap.ic_person_history_last_bg);//最后一个显示加号图片
            tvMore.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

}

