package com.guidemachine.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.App;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.trace.LBSTraceClient;
import com.guidemachine.R;
import com.guidemachine.base.ui.BaseActivity;
import com.guidemachine.constant.Constants;
import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.presenter.SOSLogPresenter;
import com.guidemachine.service.view.BaseView;
import com.guidemachine.service.view.SosView;
import com.guidemachine.ui.view.CompletedView;
import com.guidemachine.util.L;
import com.guidemachine.util.LocationService;
import com.guidemachine.util.Logger;
import com.guidemachine.util.ToastUtils;
import com.guidemachine.util.share.SPHelper;
import com.skyfishjy.library.RippleBackground;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class SOSActivity extends BaseActivity implements View.OnClickListener,SosView{

    @BindView(R.id.tasks_view)
    CompletedView tasksView;
    @BindView(R.id.tv_sos)
    TextView tvSos;
    private int mTotalProgress = 100;
    private int mCurrentProgress = 0;
    //进度条
    private CompletedView mTasksView;
    //发送sos
    SOSLogPresenter sosLogPresenter;

    @BindView(R.id.title_text)
    TextView mBackText;

    @BindView(R.id.sos_mapView)
    MapView mapView;
    //是否是第一次定位
    private volatile boolean isFristLocation = true;
    LBSTraceClient mClient;
    private BaiduMap baiduMap;
//    double longitude;
//    double latitude;
    BitmapDescriptor mCurrentMarker;
    private LocationService locationService;

    private BDLocation mCurrentLocation;
    private Bundle mBundle;
    private Double latitude,longitude;
    private Float radius,direction;
    @BindView(R.id.sos_text)
    TextView sos_text;
    @BindView(R.id.sos_text_layout)
    RelativeLayout mTextContentLayout;
    @BindView(R.id.sos_layout)
    RelativeLayout mSosLayout;
    @BindView(R.id.phone_layout)
    RelativeLayout mPhoneLayout;
    @BindView(R.id.back_view)
    View mBackgroundView;
    @BindView(R.id.signal_image)
    ImageView mSignalImage;
    @BindView(R.id.ripple_ground)
    RippleBackground rippleBackground;
    private String mPhone;

    @Override
    protected int setRootViewId() {
        return R.layout.activity_sos;
    }

    @Override
    protected boolean setIsFull() {
        return false;
    }

    @Override
    protected void InitialView() {//遇到紧急事件长按“SOS”键3秒，即可呼救我们将及时赶往您的位置提供帮助
        String content = "遇到" + "<font color='#FF0000'>" + "紧急事件";
        sos_text.setText(Html.fromHtml(content));
        mBackText.setText("一键求救");
        mBundle = getIntent().getExtras();
        if(mBundle!=null){
            latitude = mBundle.getDouble("latitude");
            longitude = mBundle.getDouble("longitude");
            radius = mBundle.getFloat("radius");
            direction = mBundle.getFloat("direction");
        }

        L.gi().d("获取当前坐标"+latitude);
        mTasksView = findViewById(R.id.tasks_view);
        tvSos.setVisibility(View.VISIBLE);
        sosLogPresenter = new SOSLogPresenter(SOSActivity.this);
        sosLogPresenter.onCreate();

        sosLogPresenter.attachView(this);

        sosLogPresenter.querylinkman(Constants.mImei);
        mTasksView.setOnLongClickListener(view -> {
            tvSos.setVisibility(View.GONE);
            new Thread(new ProgressRunable()).start();
            return false;
        });
        baiduMap = mapView.getMap();
        // 不显示地图缩放控件（按钮控制栏）
        mapView.showZoomControls(false);
        //开启定位图层
        baiduMap.setMyLocationEnabled(true);

        //更改指南针位置
        baiduMap.setCompassPosition(new Point(650, 80));
        baiduMap.clear();//先清除一下图层
//        MyLocationConfiguration myLocationConfiguration = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING, true,BitmapDescriptorFactory.fromResource(R.mipmap.ic_user_location));

        baiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.NORMAL, true, mCurrentMarker));

        // -----------location config ------------
        locationService = ((App) getApplication()).locationService;

        //注册监听
//        locationService.setLocationOption(locationService.getDefaultLocationClientOption(SOSActivity.this));
//        locationService.start();


        LatLng cenpt = new LatLng(latitude, longitude);   //①
        // 定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder().target(cenpt).zoom(18).build();  //②
        // 定义地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate =
                MapStatusUpdateFactory.newMapStatus(mMapStatus);   //③
        // BaiduMap对象改变地图状态
        baiduMap.setMapStatus(mMapStatusUpdate);    //④
        MyLocationData locData = new MyLocationData.Builder().accuracy(radius)
                .direction(direction).latitude(latitude).longitude(longitude).build();
        baiduMap.setMyLocationData(locData);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    public void onSuccess(String msg,String flag) {
        switch (flag){
            case "reportSos":
//                ToastUtils.msg(msg);
                ToastUtils.msg("求救成功");
                callPhone("13541065873");
                finish();
                break;
            case "getPhone":
                mPhone = msg;
                L.gi().d("获取到的手机号码："+msg);
                break;
        }
    }

    @Override
    public void onError(String result) {
        ToastUtils.msg(result);
    }

    class ProgressRunable implements Runnable {
        @Override
        public void run() {
            while (mCurrentProgress < mTotalProgress) {

                mCurrentProgress += 1;
                mTasksView.setProgress(mCurrentProgress);
                runOnUiThread(() -> {
                    if (mCurrentProgress == 100) {
                        mTextContentLayout.setVisibility(View.GONE);
                        mBackgroundView.setVisibility(View.GONE);
                        mSosLayout.setVisibility(View.GONE);
                        mPhoneLayout.setVisibility(View.VISIBLE);
                        rippleBackground.setVisibility(View.VISIBLE);
                        rippleBackground.startRippleAnimation();
                        L.gi().d("开启SOS功能===============");
                        Vibrator vibrator = (Vibrator) SOSActivity.this.getSystemService(SOSActivity.this.VIBRATOR_SERVICE);
                        vibrator.vibrate(1000);
//                        JSONObject loginRequestData = new JSONObject();
//                        try {
//                            loginRequestData.put("address", SPHelper.getInstance(SOSActivity.this).getCityName());
////                                loginRequestData.put("imei", MobileInfoUtil.getIMEI(SOSActivity.this));
//                            loginRequestData.put("imei", Constants.mImei);
//                            loginRequestData.put("lon", latitude);
//                            loginRequestData.put("lat", longitude);
//                            loginRequestData.put("sceneryId", 1);
//                            loginRequestData.put("createTime", System.currentTimeMillis());
//                            Logger.d("时间戳", System.currentTimeMillis() + "");
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        RequestBody loginRequestBody = RequestBody.create(MediaType.parse("application/json"), loginRequestData.toString());

//                        sosLogPresenter.getSOSLog(loginRequestBody);
                        sosLogPresenter.getSOSLog2(latitude,longitude,SPHelper.getInstance(SOSActivity.this).getCityName());
                    }
                });
                try {
                    Thread.sleep(25);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

//    BaseView baseView = new BaseView() {
//        @Override
//        public void onSuccess(BaseBean mBaseBean,String flag) {
//            switch (flag){
//                case "reportSos":
//                    ToastUtils.msg(mBaseBean.getResultStatus().getResultMessage().toString());
//                    ToastUtils.msg("求救成功");
////            callPhone("15828472427");
//                    finish();
//                    break;
//                case "getPhone":
//
//                    break;
//            }
//        }
//
//        @Override
//        public void onError(String result) {
//            ToastUtils.msg(result);
//        }
//    };

    /**
     * 拨打电话（直接拨打电话）
     *
     * @param phoneNum 电话号码
     */
    @SuppressLint("MissingPermission")
    public void callPhone(String phoneNum) {
        try{
            Intent intent = new Intent(Intent.ACTION_CALL);
            Uri data = Uri.parse("tel:" + phoneNum);
            intent.setData(data);
            startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 定位SDK监听函数
     */
    @Override
    protected void onPause() {
        super.onPause();
        // activity 暂停时同时暂停地图控件
        mapView.onPause();

    }
    @Override
    protected void onResume() {
        super.onResume();
        // activity 恢复时同时恢复地图控件
        mapView.onResume();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        sosLogPresenter.onStop();
        MapView.setMapCustomEnable(false);
        mapView.onDestroy();
        locationService.stop();
    }


    @OnClick({R.id.back_image})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_image:
                finish();
                break;
        }
    }
}
