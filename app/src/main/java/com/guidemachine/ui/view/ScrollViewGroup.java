package com.guidemachine.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 *  * 自定义ViewGroup
 *  * 在横向布局的基础上，增加啦滚动效果，但是没有边界限制
 *  
 */
public class ScrollViewGroup extends ViewGroup {
    private Scroller mScroller;
    private float mLastMotionX = 0;

    public ScrollViewGroup(Context context) {
        this(context, null);
    }

    public ScrollViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mScroller = new Scroller(context);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
// TODO Auto-generated method stub
        int action = event.getAction();
        float x = event.getX();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                mLastMotionX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                float delt = mLastMotionX - x;
                mLastMotionX = x;
                scrollBy((int) delt, 0);
                break;
            case MotionEvent.ACTION_UP:
                invalidate();
                break;

            default:
                break;
        }

        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//重新设置宽高
        this.setMeasuredDimension(measureWidth(widthMeasureSpec, heightMeasureSpec), measureHeight(widthMeasureSpec, heightMeasureSpec));
    }

    /**
     *  * 测量宽度
     *  
     */
    private int measureWidth(int widthMeasureSpec, int heightMeasureSpec) {
        // 宽度
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        //父控件的宽（wrap_content）
        int width = 0;
        int childCount = getChildCount();

        //重新测量子view的宽度，以及最大高度
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            width += childWidth;
        }
        return modeWidth == MeasureSpec.EXACTLY ? sizeWidth : width;
    }

    /**
     *  * 测量高度
     *  
     */
    private int measureHeight(int widthMeasureSpec, int heightMeasureSpec) {
//高度
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
//父控件的高（wrap_content）
        int height = 0;
        int childCount = getChildCount();

//重新测量子view的宽度，以及最大高度
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            height += childHeight;
        }
        height = height / childCount;
        return modeHeight == MeasureSpec.EXACTLY ? sizeHeight : height;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childLeft = 0;
        int childWidth;
        int height = getHeight();
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            child.layout(childLeft, 0, childLeft + childWidth, height);
            childLeft += childWidth;
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}
