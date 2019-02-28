package com;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.os.Vibrator;
import android.support.multidex.MultiDexApplication;
import android.util.DisplayMetrics;

import com.baidu.idl.face.platform.LivenessTypeEnum;
import com.baidu.mapapi.SDKInitializer;
import com.guidemachine.ui.activity.chat.domain.DemoHelper;
import com.guidemachine.util.LocationService;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseUI;
import com.uuzuche.lib_zxing.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

public class App extends MultiDexApplication {
    private static App myApplication;
    public LocationService locationService;
    public Vibrator mVibrator;
    public static List<LivenessTypeEnum> livenessList = new ArrayList<LivenessTypeEnum>();
    public static boolean isLivenessRandom = false;
    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        /***
         * 初始化定位sdk，建议在Application中创建
         */
        locationService = new LocationService(getApplicationContext());
        mVibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(getApplicationContext());
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
//        SDKInitializer.setCoordType(CoordType.BD09LL);

        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        EaseUI.getInstance().init(this, options);
        //init demo helper
        DemoHelper.getInstance().init(this);
        /**
         * 初始化尺寸工具类
         */
        initDisplayOpinion();


    }
    public static App getMyApplication() {
        return myApplication;
    }

    public static Context getAppContext() {
        return myApplication.getApplicationContext();
    }


    private void initDisplayOpinion() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        DisplayUtil.density = dm.density;
        DisplayUtil.densityDPI = dm.densityDpi;
        DisplayUtil.screenWidthPx = dm.widthPixels;
        DisplayUtil.screenhightPx = dm.heightPixels;
        DisplayUtil.screenWidthDip = DisplayUtil.px2dip(getApplicationContext(), dm.widthPixels);
        DisplayUtil.screenHightDip = DisplayUtil.px2dip(getApplicationContext(), dm.heightPixels);
    }
}
