package com.guidemachine.ui.activity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import com.guidemachine.R;
import com.guidemachine.base.ui.BaseActivity;
import com.guidemachine.util.IntentUtils;
import com.guidemachine.util.Logger;
import com.guidemachine.util.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;


public class GuideActivity extends BaseActivity {

    @BindView(R.id.img_switcher)
    ImageSwitcher imgSwitcher;
    private int[] image = {R.mipmap.ic_guide5, R.mipmap.ic_guide4, R.mipmap.ic_guide3, R.mipmap.ic_guide2};
    int index = 0;
    boolean indexIsZero = false;

    @Override
    protected int setRootViewId() {
        return R.layout.activity_guide;
    }


    @Override
    protected boolean setIsFull() {
        return false;
    }

    @Override
    protected void InitialView() {
        setTranslucentStatus();
        imgSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView iv = new ImageView(GuideActivity.this);
                iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                iv.setImageResource(R.mipmap.ic_guide1);
                return iv;
            }
        });
        imgSwitcher.setOnTouchListener(new View.OnTouchListener() {
            //触摸事件
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    return true;//按下去
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (indexIsZero == true) {
                        IntentUtils.openActivity(GuideActivity.this, MapActivity.class);
                        finish();
                        return false;
                    }
                    index = index> 0 ? --index : image.length - 1;
                    imgSwitcher.setImageResource(image[index]);
                    imgSwitcher.setInAnimation(GuideActivity.this, android.R.anim.fade_in);
                    imgSwitcher.setOutAnimation(GuideActivity.this, android.R.anim.fade_out);
                    Logger.d("imageIndex:" + index);
                    if (index == 0) {
                        indexIsZero = true;

                    }
                }
                return false;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
