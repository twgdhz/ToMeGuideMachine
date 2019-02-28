package com.guidemachine.base.ui;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.Arrays;

import butterknife.ButterKnife;

public abstract class CommonActivity extends AppCompatActivity {

    protected final String TAG = this.getClass().getSimpleName();
    protected boolean isShowBar=true;

    protected abstract int setRootViewId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (setIsFull()) {
            isShowBar=false;
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(setRootViewId());
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
    /*** 设置为竖屏  */
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        super.onResume();
    }

    protected abstract boolean setIsFull();


    protected void loadData() {

    }


    protected void logi(String msg) {
        if (msg == null) {
            Log.i(TAG, "msg = null");
        } else {
            Log.i(TAG, msg);
        }
    }

    protected void logi(String... msg) {
        Log.i(TAG, Arrays.toString(msg));
    }

    protected void loge(Throwable e) {
        Log.e(TAG, "onError", e);
    }

    protected void loge(String msg) {
        if (msg == null) {
            Log.e(TAG, "msg = null");
        } else {
            Log.e(TAG, msg);
        }
    }

    protected void loge(String... msg) {
        Log.e(TAG, Arrays.toString(msg));
    }

    public void msg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    protected void longMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }



}
