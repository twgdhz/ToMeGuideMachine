package com.guidemachine.base.ui;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.guidemachine.R;
import com.guidemachine.service.observer.HomeKeyObserver;
import com.guidemachine.service.observer.PowerKeyObserver;
import com.guidemachine.ui.view.CustomDialog;
import com.guidemachine.util.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity基础页面
 */
public abstract class BaseActivity extends CommonActivity implements BackHandlerImp {

    protected ProgressDialog progressDialog;
    private static List<BaseActivity> activityList = new ArrayList<>();
    private BaseFragment baseFragment;
    CustomDialog logDialog;
    private PowerKeyObserver mPowerKeyObserver;
    private HomeKeyObserver mHomeKeyObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setWindowStatusBarColor(BaseActivity.this, R.color.text_color4);
        progressDialog = new ProgressDialog(this);
        InitialView();
//         configuration();//此方法导致状态栏无法沉浸
        initBroadCast();
    }

    public void setTranslucentStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//安卓版本大于4.4
            Window window = getWindow();
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.flags |= WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            window.setAttributes(layoutParams);
        }
    }

    //下面是加载框
    public void showProgressDialog() {
        if (logDialog == null) {
            logDialog = new CustomDialog(this, "加载中...");
        }
        logDialog.show();
    }

    /**
     * 对话框
     */
    protected void dismissProgressDialog() {
        if (logDialog == null) {
            return;
        }
        logDialog.dismiss();
    }

    protected abstract void InitialView();
//    @MultiStateView.State
//    protected int getDefaultState() {
//        return MultiStateView.STATE_LOADING;
//    }
//    @DrawableRes
//    protected int getErrorIcon() {
//        return R.drawable.ic_exception;
//    }
//
//
//    protected String getErrorTitle() {
//        return getString(R.string.label_error_network_is_bad);
//    }
//
//    protected String getErrorButton() {
//        return getString(R.string.label_click_button_to_retry);
//    }
//    protected String getEmptyButton() {
//        return getString(R.string.clicktoclose);
//    }
//
//
//    @DrawableRes
//    protected int getEmptyIcon() {
//        return R.drawable.ic_empty;
//    }
//
//    protected String getEmptyTitle() {
//        return getString(R.string.label_empty_data);
//    }

    protected static void Add(BaseActivity activity) {
        if (!activityList.contains(activity)) {
            activityList.add(activity);
        }
    }

    protected static void Remove(BaseActivity activity) {
        activityList.remove(activity);
    }

    protected static void RemoveAll() {
        activityList.clear();
    }

    protected void FinishAll() {
        for (BaseActivity activity : activityList) {

            if (activity != null) {
                activity.finish();
            }
        }

        activityList.clear();
    }


    @Override
    public void setSelectedFragment(BaseFragment baseFragment) {
        this.baseFragment = baseFragment;
    }


    /**
     * Back返回键处理
     */
    @Override
    public void onBackPressed() {
        if (baseFragment == null || getSupportFragmentManager().getBackStackEntryCount() == 0) {
            super.onBackPressed();
        } else {
            getSupportFragmentManager().popBackStack();
            baseFragment.onDestroy();
        }

    }


    private void configuration() {

        if (isShowBar) {
            ViewGroup contentFrameLayout = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
            View parentView = contentFrameLayout.getChildAt(0);
            int currentApiVersion = Build.VERSION.SDK_INT;
            if (Build.VERSION_CODES.KITKAT >= 14) {
                //parentView.setBackgroundColor(Color.BLACK);
                parentView.setFitsSystemWindows(true);
            }
            //View statusBarView = new View(this);
        }
    }

    /**
     * 设置沉浸式状态栏
     */
    public void setStatus() {
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.flags |= WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        window.setAttributes(layoutParams);
    }
    private void initBroadCast(){
        mPowerKeyObserver = new PowerKeyObserver(this);
        mPowerKeyObserver.setPowerKeyListener(new PowerKeyObserver.OnPowerKeyListener() {
            @Override
            public void onPowerKeyPressed() {
                Toast.makeText(getApplicationContext(),"通过广播监听到了电源键",Toast.LENGTH_SHORT).show();
            }
        });
        mPowerKeyObserver.startListen();

        mHomeKeyObserver = new HomeKeyObserver(this);
        mHomeKeyObserver.setHomeKeyListener(new HomeKeyObserver.OnHomeKeyListener() {
            @Override
            public void onHomeKeyPressed() {
                Toast.makeText(getApplicationContext(),"通过广播监听到了Home键",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onHomeKeyLongPressed() {
                Toast.makeText(getApplicationContext(),"长按Home键",Toast.LENGTH_SHORT).show();
            }
        });
        mHomeKeyObserver.startListen();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_VOLUME_UP:
                Toast.makeText(this,"通过手指点击了音量增大键",Toast.LENGTH_SHORT).show();
                Log.d("com.guidemachine","通过手指点击了音量增大键");
                break;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                Toast.makeText(this,"通过手指点击了音量降低键",Toast.LENGTH_SHORT).show();
                Log.d("com.guidemachine","通过手指点击了音量降低键");
                break;
            case KeyEvent.KEYCODE_POWER:
                Toast.makeText(this,"通过手指点击了电源锁屏键",Toast.LENGTH_SHORT).show();
                Log.d("com.guidemachine","通过手指点击了电源锁屏键");
                break;
            case KeyEvent.KEYCODE_F1:
                Toast.makeText(this,"通过手指点击了F1键==启动SOS功能",Toast.LENGTH_SHORT).show();
                Log.d("com.guidemachine","通过手指点击了F1键==启动SOS功能");
                break;


        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPowerKeyObserver.stopListen();
        mHomeKeyObserver.stopListen();
    }

}
