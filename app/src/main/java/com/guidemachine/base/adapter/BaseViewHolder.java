package com.guidemachine.base.adapter;

import android.support.annotation.DrawableRes;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import static android.support.v7.widget.RecyclerView.ViewHolder;

/**
 * baseHolder类
 */

public class BaseViewHolder extends ViewHolder {

    private View convertView;
    SparseArray<View> viewArray;

    public BaseViewHolder(View itemView) {
        super(itemView);
        this.convertView = itemView;
        viewArray = new SparseArray<>();
    }

    public View getConvertView() {
        return convertView;
    }

    public <T extends View> T getView(int viewId) {
        View childreView = viewArray.get(viewId);
        if (childreView == null) {
            childreView = itemView.findViewById(viewId);
            viewArray.put(viewId, childreView);
        }
        return (T) childreView;
    }

    /**
     * 设置文本资源
     *
     * @param viewId view id
     * @param s      字符
     */
    public TextView setText(int viewId, CharSequence s) {
        TextView view = getView(viewId);
        view.setText(s);
        return view;
    }

    /**
     * 设置文本颜色
     * @param viewId
     * @param s
     * @return
     */
    public TextView setTextColor(int viewId, int s) {
        TextView view = getView(viewId);
        view.setTextColor(s);
        return view;
    }



    /**
     * 设置图片资源
     *
     * @param viewId     view id
     * @param imageResId 图片资源id
     */
    public ImageView setImageResource(int viewId, @DrawableRes int imageResId) {
        ImageView view = getView(viewId);
        view.setImageResource(imageResId);
        return view;
    }


    //根据Item中的控件Id向控件添加事件监听
    public BaseViewHolder setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }


    /**
     * @param viewId
     * @param visible
     * @return
     */
    public BaseViewHolder setVisible(int viewId, boolean visible) {
        View view = getView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }
    public BaseViewHolder setCheck(int viewId, boolean check) {
        RadioButton view = getView(viewId);
        view.setChecked(check ? true : false);
        return this;
    }

}
