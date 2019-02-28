package com.guidemachine.base.adapter.Listener;

import android.support.v7.widget.RecyclerView;


public interface OnDragAndSwipeListener {

    boolean onItemDrag(int fromPosition, int toPosition);

    void onItemSwipe(int position);

    void onItemSelected(RecyclerView.ViewHolder viewHolder);

    void onItemClear(RecyclerView.ViewHolder viewHolder);

    /**
     * @param viewHolder
     * @param dX         Side slip distance
     */
    void onItemSwipeAlpha(RecyclerView.ViewHolder viewHolder, float dX);

}
