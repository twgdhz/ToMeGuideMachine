package com.guidemachine.ui.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.App;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.walknavi.WalkNavigateHelper;
import com.baidu.mapapi.walknavi.adapter.IWEngineInitListener;
import com.baidu.mapapi.walknavi.adapter.IWRoutePlanListener;
import com.baidu.mapapi.walknavi.model.WalkRoutePlanError;
import com.baidu.mapapi.walknavi.params.WalkNaviLaunchParam;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.Trace;
import com.baidu.trace.api.fence.AlarmPoint;
import com.baidu.trace.api.fence.CircleFence;
import com.baidu.trace.api.fence.CreateFenceRequest;
import com.baidu.trace.api.fence.CreateFenceResponse;
import com.baidu.trace.api.fence.DeleteFenceResponse;
import com.baidu.trace.api.fence.DistrictFence;
import com.baidu.trace.api.fence.FenceAlarmInfo;
import com.baidu.trace.api.fence.FenceAlarmPushInfo;
import com.baidu.trace.api.fence.FenceInfo;
import com.baidu.trace.api.fence.FenceListResponse;
import com.baidu.trace.api.fence.HistoryAlarmResponse;
import com.baidu.trace.api.fence.MonitoredAction;
import com.baidu.trace.api.fence.MonitoredStatus;
import com.baidu.trace.api.fence.MonitoredStatusByLocationResponse;
import com.baidu.trace.api.fence.MonitoredStatusInfo;
import com.baidu.trace.api.fence.MonitoredStatusResponse;
import com.baidu.trace.api.fence.OnFenceListener;
import com.baidu.trace.api.fence.PolygonFence;
import com.baidu.trace.api.fence.PolylineFence;
import com.baidu.trace.api.fence.UpdateFenceResponse;
import com.baidu.trace.model.CoordType;
import com.baidu.trace.model.OnTraceListener;
import com.baidu.trace.model.PushMessage;
import com.baidu.trace.model.StatusCodes;
import com.guidemachine.R;
import com.guidemachine.base.ui.BaseActivity;
import com.guidemachine.constant.Constants;
import com.guidemachine.event.MessageEventPlayComplete;
import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.FenceBean;
import com.guidemachine.service.entity.LocalLocationBean;
import com.guidemachine.service.entity.ScenerySpotListBean;
import com.guidemachine.service.entity.ShopMarkersBean;
import com.guidemachine.service.entity.WeatherBean;
import com.guidemachine.service.presenter.FencePresenter;
import com.guidemachine.service.presenter.ScenerySpotPresenter;
import com.guidemachine.service.presenter.ShopMarkersListPresenter;
import com.guidemachine.service.presenter.WeatherPresenter;
import com.guidemachine.service.view.FenceView;
import com.guidemachine.service.view.ScenerySpotView;
import com.guidemachine.service.view.ShopMarkersView;
import com.guidemachine.service.view.WeatherView;
import com.guidemachine.ui.activity.chat.ChatActivity;
import com.guidemachine.ui.activity.chat.domain.DemoHelper;
import com.guidemachine.ui.activity.video.VideoPlayerActivity;
import com.guidemachine.ui.activity.walknavi.WNaviGuideActivity;
import com.guidemachine.ui.guide.PaintFenceActivity;
import com.guidemachine.util.BadgeView;
import com.guidemachine.util.FileUtil;
import com.guidemachine.util.IntentUtils;
import com.guidemachine.util.IsLoginUtils;
import com.guidemachine.util.L;
import com.guidemachine.util.LatUtil;
import com.guidemachine.util.LocationService;
import com.guidemachine.util.LogUtils;
import com.guidemachine.util.Logger;
import com.guidemachine.util.MediaPlayerUtils;
import com.guidemachine.util.MobileInfoUtil;
import com.guidemachine.util.SoundManager;
import com.guidemachine.util.StatusBarUtils;
import com.guidemachine.util.ToastUtils;
import com.guidemachine.util.VoiceUtil;
import com.guidemachine.util.overlayutil.OverlayManager;
import com.guidemachine.util.serialPort.SerialPortUtils;
import com.guidemachine.util.share.SPHelper;
import com.hyphenate.EMCallBack;
import com.hyphenate.easeui.EaseConstant;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import android_serialport_api.SerialPort;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;


/**
 * @author ChenLinWang
 * @email 422828518@qq.com
 * @create 2018/12/19 0019 17:32
 * description: 导游机地图页面
 */
public class MapActivity extends BaseActivity {
    @BindView(R.id.mapView)
    MapView mapView;
    @BindView(R.id.img_location)
    ImageView imgLocation;
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_title_center)
    TextView tvTitleCenter;
    @BindView(R.id.tv_temperature)
    TextView tvTemperature;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.img_sos)
    ImageView imgSos;
    @BindView(R.id.rb_line)
    RadioButton rbLine;
    @BindView(R.id.rb_scene)
    RadioButton rbScene;
    @BindView(R.id.rb_shopping)
    RadioButton rbShopping;
    @BindView(R.id.rb_toilet)
    RadioButton rbToilet;
    @BindView(R.id.tb_rg)
    RadioGroup tbRg;
    @BindView(R.id.rl_right)
    RelativeLayout rlRight;
    @BindView(R.id.rl_notice)
    RelativeLayout rlNotice;
    @BindView(R.id.ll_message)
    RelativeLayout llMessage;
    @BindView(R.id.rl_wave_communicate)
    RelativeLayout rlWaveCommunicate;
    private LocationService locationService;
    private static final String TAG = "lzy";
    private BaiduMap baiduMap;
    double longitude;
    double latitude;
    /***
     * 是否是第一次定位
     */
    private volatile boolean isFristLocation = true;
    // 有被点击过的marker
    boolean checked;
    // 已点过的marker
    Marker previousMarker = null;
    //景点经纬度
    ScenerySpotPresenter scenerySpotPresenter;
    //存放景点的实体类
    BaseBean<ScenerySpotListBean> scenerySpotListBean;
    int markerType = 1;//底部菜单栏选择标志位
    //天气
    WeatherPresenter weatherPresenter;
    //商铺
    ShopMarkersListPresenter shopMarkersListPresenter;
    //获取当前绑定的电子围栏
    FencePresenter fencePresenter;
    List<String> locationFenceList = new ArrayList<>();
    LBSTraceClient mClient;
    //小红点提示
    BadgeView badgeView;
    public boolean isPlay = false;
    TextView imgPlayVoice;

    //步行导航启动参数(引入esaeui下的jar包)
    WalkNaviLaunchParam walkParam;
    private LatLng startPt, endPt;
    private BDLocation mCurrentLocation;
    private String entityName;
    private SoundManager mSoundManager;
    private int mTag = 0, mCurrentId = 1, mIsPlayId = 0;
    //围栏半径
    private double radius = 20;

    //去燥
    private int denoise = 200;

    private OverlayOptions overlayOptions;
    private Overlay currentOverlay;
    private List<com.baidu.mapapi.model.LatLng> circleCenterList;

    BitmapDescriptor mCurrentMarker;
    private SerialPortUtils serialPortUtils;
    private MediaPlayer player;
    private String mTempDate, mSigal;
    private boolean isPlaying = true;
    private MediaPlayerUtils mediaPlayerUtils;
    @Override
    protected int setRootViewId() {
        return R.layout.activity_map;
    }

    @Override
    protected boolean setIsFull() {
        return false;
    }

    @Override
    protected void InitialView() {
//        Add(this);
        mediaPlayerUtils = MediaPlayerUtils.getInstance();
        //打开串口
        serialPortUtils = SerialPortUtils.getInstance();
        serialPortUtils.openSerialPort();
        if (serialPortUtils.isConnect()) {
            //设置身份
            com.guidemachine.util.serialPort.Message message = new com.guidemachine.util.serialPort.Message();
            message.setCommandType(Constants.VISE_COMMAND_HEAD_FLAG_01);
            message.setCommandData(Constants.VISE_COMMAND_HEAD_FLAG_01);
            message.setData(new byte[]{message.getCommandType(), message.getCommandData()});
            SerialPortUtils.getInstance().sendHex(SerialPortUtils.getInstance().getHexResult(message));
            L.gi().d("串口打开成功");
        }else{
            Toast.makeText(MapActivity.this, "串口打开失败", Toast.LENGTH_SHORT).show();
        }
        L.gi().d("获取imei："+Constants.mImei);
        mCurrentMarker = BitmapDescriptorFactory.fromResource(R.mipmap.ic_user_location);
        mSoundManager = SoundManager.getInstence();
        mSoundManager.initMusic(getApplicationContext());
        EventBus.getDefault().register(this);
        StatusBarUtils.setWindowStatusBarColor(MapActivity.this, R.color.text_color4);
        entityName = MobileInfoUtil.getIMEI(MapActivity.this);

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
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);

        //注册监听
        locationService.setLocationOption(locationService.getDefaultLocationClientOption(MapActivity.this));
        locationService.start();

        rlRight.setOnClickListener(view -> {
            if (markerType == 1) {
                if (scenerySpotListBean != null) {
                    Intent intent = new Intent(MapActivity.this, SceneListActivity.class);
                    intent.putExtra("scenerySpotListBean", scenerySpotListBean);
                    startActivity(intent);

                } else {
                    IntentUtils.openActivity(MapActivity.this, SceneListActivity.class);
                }
            } else if (markerType == 2) {

            } else if (markerType == 3) {
                if (IsLoginUtils.getInstence().isLogin(MapActivity.this) == true) {
                    IntentUtils.openActivity(MapActivity.this, ShopListActivity.class);
                } else {
                    IntentUtils.openActivity(MapActivity.this, LoginActivity.class, "type", "1");
                }
            } else if (markerType == 4) {
                if (scenerySpotListBean != null) {
                    Intent intent = new Intent(MapActivity.this, ToiletListActivity.class);
                    intent.putExtra("scenerySpotListBean", scenerySpotListBean);
                    startActivity(intent);
                } else {
                    IntentUtils.openActivity(MapActivity.this, ToiletListActivity.class);
                }
            }

        });
        //串口数据监听事件 接收到RFID
        serialPortUtils.setOnDataReceiveListener(new SerialPortUtils.OnDataReceiveListener() {
            @Override
            public void onDataReceive(String data, String sigal) {
                L.gi().d("获取的data数据：" + data);
                switch (data) {
                    case "1234567890ABCDEF1234567890123456":
                        L.gi().d("开始播报第一个RFID" + data);
                        mSigal = sigal;
                        handler.sendEmptyMessage(100);
//                        Toast.makeText(MapActivity.this,"获取到信号强度："+sigal,Toast.LENGTH_SHORT).show();
//                        if (!data.equals(mTempDate)) {
//                            if (player != null) {
//                                player.reset();
//                            }
//                            player = MediaPlayer.create(MapActivity.this, R.raw.voice1);
//                            player.start();
//                        }
//                        mTempDate = data;
                        break;
                }
                L.gi().d("进入数据监听事件中。。。");
//                mBuffer = buffer;
//               App.getMyApplication().mHandler.post(runnable);

            }

            //开线程更新UI
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    L.gi().d("开始播放111。。。");
                    if (mSoundManager != null) {
                        L.gi().d("开始播放2222。。。");
                        mIsPlayId = mSoundManager.StartMusic(18);
                    }
//                    mReciveText.setText("接收到数据："+changeTool.ByteArrToHex(mBuffer).trim()+" ");
////                    mReciveText.setText("接收到数据："+" "+Arrays.toString(mBuffer));
//                    textView_status.setText("size："+ String.valueOf(mBuffer.length)+"数据监听："+ new String(mBuffer));
                }
            };
        });
        imgSos.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putDouble("latitude", latitude);
            bundle.putDouble("longitude", longitude);
            bundle.putFloat("radius", mCurrentLocation.getRadius());
            bundle.putFloat("direction", mCurrentLocation.getDirection());

            IntentUtils.openActivity(MapActivity.this, SOSActivity.class, bundle);

        });
//        imgSos.setOnClickListener(view ->
//                IntentUtils.openActivity(MapActivity.this, FenceDemoAct.class)
//        );
        rlNotice.setOnClickListener(new View.OnClickListener() {//消息通知
            @Override
            public void onClick(View view) {
                IntentUtils.openActivity(MapActivity.this, NoticeActivity.class, "temperature", tvTemperature.getText().toString());
            }
        });
        llMessage.setOnClickListener(view -> {
//                IntentUtils.openActivity(MapActivity.this, PaintFenceActivity.class);
            if (DemoHelper.getInstance().isLoggedIn()) {
                startActivity(new Intent(MapActivity.this, ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID,
                        SPHelper.getInstance(MapActivity.this).getPhone()));
//                    finish();
            } else {
                IntentUtils.openActivity(MapActivity.this, LoginActivity.class, "type", "2");
            }
        });
        rlWaveCommunicate.setOnClickListener(view -> {
//            ToastUtils.msg("功能暂未开放");
            //对讲机
            IntentUtils.openActivity(MapActivity.this, WalkieTalkieAct.class);
        });
        rlBack.setOnClickListener(view -> IntentUtils.openActivity(MapActivity.this, PersonCenterActivity.class));

        fencePresenter = new FencePresenter(MapActivity.this);
        fencePresenter.onCreate();
        fencePresenter.attachView(fenceView);
//        JSONObject requestFenceData = new JSONObject();
//        try {
//            requestFenceData.put("imei", getIMEI(MapActivity.this));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        RequestBody requestFenceBody = RequestBody.create(MediaType.parse("application/json"), requestFenceData.toString());
        //获取电子围栏接口
//        fencePresenter.getFence(requestFenceBody);
        fencePresenter.getFence2(getIMEI(MapActivity.this));

        tbRg.setOnCheckedChangeListener((radioGroup, checkedId) -> {
            scenerySpotPresenter = new ScenerySpotPresenter(MapActivity.this);
            scenerySpotPresenter.onCreate();
            scenerySpotPresenter.attachView(scenerySpotView);
            rbLine.setEnabled(false);
            rbScene.setEnabled(false);
            rbShopping.setEnabled(false);
            rbToilet.setEnabled(false);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    rbLine.setEnabled(true);
                    rbScene.setEnabled(true);
                    rbShopping.setEnabled(true);
                    rbToilet.setEnabled(true);
                }
            }, 1000);
            if (checkedId == R.id.rb_scene) {//1
                markerType = 1;
                baiduMap.clear();
                JSONObject requestData = new JSONObject();
                try {
                    requestData.put("sceneryId", SPHelper.getInstance(MapActivity.this).getSceneryId());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestData.toString());
                scenerySpotPresenter.getSceneryList(requestBody, 1);
                fencePresenter.getFence2(getIMEI(MapActivity.this));

                showProgressDialog();
            } else if (checkedId == R.id.rb_line) {
                markerType = 2;
                baiduMap.clear();
                //构建折线点坐标
                LatLng p1 = new LatLng(30.903577, 103.579111);
                LatLng p2 = new LatLng(30.905037, 103.574);
                LatLng p3 = new LatLng(30.908453, 103.56719);

                LatLng p4 = new LatLng(30.911509, 103.56437);
                LatLng p5 = new LatLng(30.916423, 103.569463);
                LatLng p6 = new LatLng(30.914192, 103.567078);

                LatLng p7 = new LatLng(30.916431, 103.56945);
                LatLng p8 = new LatLng(30.907426, 103.57492);
                LatLng p9 = new LatLng(30.906253, 103.575006);
                LatLng p10 = new LatLng(30.903584, 103.57908);
                List<LatLng> points = new ArrayList<LatLng>();
                points.add(p1);
                points.add(p2);
                points.add(p3);
                points.add(p4);
                points.add(p5);
                points.add(p6);
                points.add(p7);
                points.add(p8);
                points.add(p9);
                points.add(p10);

                //设置折线的属性
                OverlayOptions mOverlayOptions = new PolylineOptions()
                        .width(10)
                        .color(0xAAFF0000)
                        .points(points);
                //在地图上绘制折线
                //mPloyline 折线对象
                Overlay mPolyline = baiduMap.addOverlay(mOverlayOptions);

                List<Integer> bitmaps = new ArrayList<Integer>();
                bitmaps.add(R.mipmap.marker1);
                bitmaps.add(R.mipmap.marker2);
                bitmaps.add(R.mipmap.marker3);
                bitmaps.add(R.mipmap.marker4);
                bitmaps.add(R.mipmap.marker5);

                bitmaps.add(R.mipmap.marker6);
                bitmaps.add(R.mipmap.marker7);
                bitmaps.add(R.mipmap.marker8);
                bitmaps.add(R.mipmap.marker9);
                bitmaps.add(R.mipmap.marker10);

                for (int i = 0; i < points.size(); i++) {
                    Bitmap bitmap = null;
                    bitmap = BitmapFactory.decodeResource(getResources(), bitmaps.get(i));
                    BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);
                    OverlayOptions overlayOptions;
                    overlayOptions = new MarkerOptions().position(new LatLng(points.get(i).latitude, points.get(i).longitude))
                            .anchor(100, 100)
                            .icon(bitmapDescriptor)
                            .zIndex(5);
                    overlayOptionsList.add(overlayOptions);
                    Overlay overlay = baiduMap.addOverlay(overlayOptions);
                    overlays.add(overlay);
                }
                zoomToSpan();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        zoomToSpan();
                    }
                }, 800);
            } else if (checkedId == R.id.rb_shopping) {//购物
                markerType = 3;
                baiduMap.clear();
                shopMarkersListPresenter = new ShopMarkersListPresenter(MapActivity.this);
                shopMarkersListPresenter.onCreate();
                shopMarkersListPresenter.attachView(shopMarkersView);
                shopMarkersListPresenter.getShopList("1");
                showProgressDialog();
            } else if (checkedId == R.id.rb_toilet) {//4
                markerType = 4;
                baiduMap.clear();
                JSONObject requestData = new JSONObject();
                try {
                    requestData.put("sceneryId", 1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestData.toString());
                scenerySpotPresenter.getSceneryList(requestBody, 4);
                showProgressDialog();

            }
        });

        //鹰眼相关
        mClient = new LBSTraceClient(MapActivity.this);
        //初始化鹰眼服务端，serviceId：服务端唯一id，entityName：监控的对象，可以是手机设备唯一标识
        Trace mTrace = new Trace(Constants.serviceId, entityName);
        mClient.startTrace(mTrace, traceListener);//开始服务
        mClient.startGather(traceListener);//开启采集


        //小红点
        badgeView = new BadgeView(MapActivity.this);
        badgeView.setTargetView(rlNotice);
        badgeView.setBackground(14, getResources().getColor(R.color.red));
        badgeView.setBadgeCount(0);
//        badgeView.setBackgroundResource(R.mipmap.ic_red_dot);
        badgeView.setTextSize(10);
        badgeView.setBadgeGravity(Gravity.TOP | Gravity.RIGHT);

        //marker点击
        baiduMap.setOnMarkerClickListener(new OverlayManager(baiduMap) {
            @Override
            public List<OverlayOptions> getOverlayOptions() {
                return null;
            }

            @Override
            public boolean onMarkerClick(final Marker marker) {
                LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                View view = null;

                if (marker.getExtraInfo() == null) {//判空

                    return false;
                }
                if (markerType == 1) {
                    final ScenerySpotListBean.ListBean listBean = (ScenerySpotListBean.ListBean) marker.getExtraInfo().get("findCarBeen");
                    Logger.d("com.guidemachine", "按下" + listBean.toString());
//                    Bitmap bitmap = null;
//                    bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_toilet_location_select);
//                    BitmapDescriptor bitmapDescriptor1 = BitmapDescriptorFactory.fromBitmap(bitmap);
//                    marker.setIcon(bitmapDescriptor1);

                    /**
                     如果有已经点过的marker，就把以前的marker还原，checked设为false
                     https://blog.csdn.net/baidu_29512909/article/details/50481169
                     */
                    if (checked) {
                        previousMarker.setIcon(BitmapDescriptorFactory
                                .fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_scene_point)));
                        checked = false;
                    }
                    view = inflater.inflate(R.layout.scene_infowindow, null);
                    LinearLayout contentLayout = view.findViewById(R.id.content_layout);
                    //跳转到音频播放界面
                    contentLayout.setOnClickListener(v -> {
                        Intent intent = new Intent(MapActivity.this, VoiceExplainAct.class);
                        intent.putExtra("imageUrl", listBean.getImageUrl().toString());
                        intent.putExtra("title", listBean.getName().toString());
                        intent.putExtra("flag", listBean.getScenerySpotId() + "");

                        startActivity(intent);
//                        IntentUtils.openActivity(MapActivity.this,VoiceExplainAct.class);
                    });
                    TextView tvName = view.findViewById(R.id.tv_name);
                    tvName.setText(listBean.getName());
                    imgPlayVoice = view.findViewById(R.id.img_play_voice);
                    imgPlayVoice.setBackgroundResource(R.mipmap.ic_scenery_play);
                    imgPlayVoice.setText("播放");
                    isPlay = false;
                    //先提前释放资源
                    if (mIsPlayId != 0) {
                        mSoundManager.stopMusic(mIsPlayId);
                    }
                    imgPlayVoice.setOnClickListener(view1 -> {
//                            mediaPlayer = new MediaPlayer();
//                            try {
//                                mediaPlayer.setDataSource("/mnt/sdcard/a3.mp3");
//                                mediaPlayer.prepare();
//                                mediaPlayer.start();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
                        // 此marker已经点过，作为上一次点击的marker
//                            checked = true;
//                            previousMarker = marker;
                        VoiceUtil.getInstance().modeIndicater(MapActivity.this, listBean.getScenerySpotId());

                        Logger.d("com.guidemachine", "点击获取的ID" + listBean.getScenerySpotId());

//                        if (listBean.getScenerySpotId()==46){
//                            mCurrentId = 1;
//                        }else if (listBean.getScenerySpotId()==45){
//                            mCurrentId = 2;
//                        }if (listBean.getScenerySpotId()==18){
//                            mCurrentId = 3;
//                        }if (listBean.getScenerySpotId()==17){
//                            mCurrentId = 4;
//                        }if (listBean.getScenerySpotId()==3){
//                            mCurrentId = 5;
//                        }
                        if (isPlay == false) {
                            isPlay = true;
                            imgPlayVoice.setBackgroundResource(R.mipmap.ic_scenery_pause);
                            imgPlayVoice.setText("暂停");
                            if (listBean.getScenerySpotId() != mTag) {
                                mIsPlayId = mSoundManager.StartMusic(listBean.getScenerySpotId());
                            } else {
                                mSoundManager.resume(mIsPlayId);
                            }
                        } else if (isPlay == true) {
                            isPlay = false;
                            imgPlayVoice.setBackgroundResource(R.mipmap.ic_scenery_play);
                            imgPlayVoice.setText("播放");
                            if (mIsPlayId != 0) {
                                mSoundManager.pauseMusic(mIsPlayId);
                            }
                        }
                        mTag = listBean.getScenerySpotId();
                    });
                    TextView tvSeePic = view.findViewById(R.id.tv_see_pic);
                    tvSeePic.setOnClickListener(view12 -> {
                        if (listBean.getImageUrl() != null) {
                            Intent intent = new Intent(MapActivity.this, SceneAlbumActivity.class);
                            intent.putExtra("imageUrl", listBean.getImageUrl().toString());
                            intent.putExtra("title", listBean.getName().toString());
                            startActivity(intent);
                        } else {
                            ToastUtils.msg("该景点没有图片！");
                        }
                    });
                    TextView tvPlayVideo = view.findViewById(R.id.tv_play_video);
                    tvPlayVideo.setOnClickListener(view13 -> IntentUtils.openActivity(MapActivity.this, VideoPlayerActivity.class, "id", listBean.getScenerySpotId() + ""));
                    //定义用于显示该InfoWindow的坐标点
                    LatLng pt = new LatLng(listBean.getLat(), listBean.getLng());
                    //创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
                    InfoWindow mInfoWindow = new InfoWindow(view, pt, -50);
                    num++;
                    Logger.d("mapActivity", "弹框:  " + num);
                    //显示InfoWindow
                    baiduMap.showInfoWindow(mInfoWindow);
                } else if (markerType == 3) {//购物
                    final ShopMarkersBean.ShopInfoBean shopInfoBean = (ShopMarkersBean.ShopInfoBean) marker.getExtraInfo().get("findCarBeen");
                    view = inflater.inflate(R.layout.shop_infowindow, null);
                    TextView tvName = view.findViewById(R.id.tv_name);
                    ImageView imgGoThere = view.findViewById(R.id.img_go_there);
                    tvName.setText(shopInfoBean.getName());
                    imgGoThere.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (IsLoginUtils.getInstence().isLogin(MapActivity.this) == true) {
                                IntentUtils.openActivity(MapActivity.this, ShopListActivity.class);
                            } else {
                                IntentUtils.openActivity(MapActivity.this, LoginActivity.class, "type", "1");
                            }
                        }
                    });
                    //定义用于显示该InfoWindow的坐标点
                    LatLng pt = new LatLng(shopInfoBean.getLat(), shopInfoBean.getLon());
                    //创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
                    InfoWindow mInfoWindow = new InfoWindow(view, pt, -50);
                    //显示InfoWindow
                    baiduMap.showInfoWindow(mInfoWindow);
                } else if (markerType == 4) {
                    final ScenerySpotListBean.ListBean listBean = (ScenerySpotListBean.ListBean) marker.getExtraInfo().get("findCarBeen");
                    view = inflater.inflate(R.layout.toilet_infowindow, null);
//                view.setMinimumHeight(400); //高度以为400为例
                    TextView tvName = view.findViewById(R.id.tv_name);
                    tvName.setText(listBean.getName());
                    TextView tvGoThere = view.findViewById(R.id.tv_go_there);
                    tvGoThere.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startPt = new LatLng(Double.parseDouble(SPHelper.getInstance(MapActivity.this).getLatitude()),
                                    Double.parseDouble(SPHelper.getInstance(MapActivity.this).getLongitude()));

                            endPt = new LatLng(30.612961, 104.05325);
                            walkParam = new WalkNaviLaunchParam().stPt(startPt).endPt(endPt);
                            walkParam.extraNaviMode(0);
                            startWalkNavi();
//                            IntentUtils.openActivity(MapActivity.this, ToiletListActivity.class);
                        }
                    });

//                //定义用于显示该InfoWindow的坐标点
                    LatLng pt = new LatLng(listBean.getLat(), listBean.getLng());
                    //创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
                    InfoWindow mInfoWindow = new InfoWindow(view, pt, -50);
                    //显示InfoWindow
                    baiduMap.showInfoWindow(mInfoWindow);
                }


                return true;
            }

            @Override
            public boolean onPolylineClick(Polyline polyline) {
                return false;
            }
        });
        baiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                baiduMap.hideInfoWindow();
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });
        baiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus, int i) {

            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                baiduMap.hideInfoWindow();
            }
        });
        circleCenterList = new ArrayList<>();
        circleCenterList.add(new LatLng(30.614245, 104.06958));
        circleCenterList.add(new LatLng(30.612188, 104.072501));
        circleCenterList.add(new LatLng(30.613617, 104.075304));

        circleCenterList.add(new LatLng(30.61455, 104.069249));
        circleCenterList.add(new LatLng(30.614627, 104.067434));
        circleCenterList.add(new LatLng(30.6146, 104.067987));


        creatFence(points);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(MessageEventPlayComplete event) {
        if (event != null) {
            ToastUtils.msg("播放完成");
            imgPlayVoice.setBackgroundResource(R.mipmap.ic_scenery_play);
            imgPlayVoice.setText("播放");
            isPlay = false;
        }
    }

    /**
     * 获取手机IMEI号
     */
    public static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();
        return imei;
    }

    /***
     * Stop location service
     */
    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
        if (shopMarkersListPresenter != null) {
            shopMarkersListPresenter.onStop();
        }

        if (weatherPresenter != null) {
            weatherPresenter.onStop();
        }
        if (scenerySpotPresenter != null) {
            scenerySpotPresenter.onStop();
        }

        super.onStop();
    }

    /*****
     *
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     *
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(final BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                Log.i(TAG, "获取成功的定位" + location.getLatitude() + " " + location.getLongitude());
                longitude = location.getLongitude();
                latitude = location.getLatitude();
                mCurrentLocation = location;

                StringBuffer sb = new StringBuffer(256);
                sb.append("time : ");
                /**
                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 */
                sb.append(location.getTime());
                sb.append("\nlocType : ");// 定位类型
                sb.append(location.getLocType());
                sb.append("\nlocType description : ");// *****对应的定位类型说明*****
                sb.append(location.getLocTypeDescription());
                sb.append("\nlatitude : ");// 纬度
                sb.append(location.getLatitude());
                sb.append("\nlontitude : ");// 经度
                sb.append(location.getLongitude());
                sb.append("\nradius : ");// 半径
                sb.append(location.getRadius());
                sb.append("\nCountryCode : ");// 国家码
                sb.append(location.getCountryCode());
                sb.append("\nCountry : ");// 国家名称
                sb.append(location.getCountry());
                sb.append("\ncitycode : ");// 城市编码
                sb.append(location.getCityCode());
                sb.append("\ncity : ");// 城市
                String city = location.getCity();
                sb.append(location.getCity());
                sb.append("\nDistrict : ");// 区
                sb.append(location.getDistrict());
                sb.append("\nStreet : ");// 街道
                sb.append(location.getStreet());
                sb.append("\naddr : ");// 地址信息
                sb.append(location.getAddrStr());
                sb.append("\nUserIndoorState: ");// *****返回用户室内外判断结果*****
                sb.append(location.getUserIndoorState());
                sb.append("\nDirection(not all devices have value): ");
                sb.append(location.getDirection());// 方向
                sb.append("\nlocationdescribe: ");
                sb.append(location.getLocationDescribe());// 位置语义化信息
                sb.append("\nPoi: ");// POI信息
                if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
                    for (int i = 0; i < location.getPoiList().size(); i++) {
                        Poi poi = (Poi) location.getPoiList().get(i);
                        sb.append(poi.getName() + ";");
                    }
                }
                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                    sb.append("\nspeed : ");
                    sb.append(location.getSpeed());// 速度 单位：km/h
                    sb.append("\nsatellite : ");
                    sb.append(location.getSatelliteNumber());// 卫星数目
                    sb.append("\nheight : ");
                    sb.append(location.getAltitude());// 海拔高度 单位：米
                    sb.append("\ngps status : ");
                    sb.append(location.getGpsAccuracyStatus());// *****gps质量判断*****
                    sb.append("\ndescribe : ");
                    sb.append("gps定位成功");
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                    // 运营商信息
                    if (location.hasAltitude()) {// *****如果有海拔高度*****
                        sb.append("\nheight : ");
                        sb.append(location.getAltitude());// 单位：米
                    }
                    sb.append("\noperationers : ");// 运营商信息
                    sb.append(location.getOperators());
                    sb.append("\ndescribe : ");
                    sb.append("网络定位成功");
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    sb.append("\ndescribe : ");
                    sb.append("离线定位成功，离线定位结果也是有效的");
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    sb.append("\ndescribe : ");
                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    sb.append("\ndescribe : ");
                    sb.append("网络不同导致定位失败，请检查网络是否通畅");
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    sb.append("\ndescribe : ");
                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                }
//                logMsg(city);
                LogUtils.d("定位", sb.toString());
                Log.d("com.guidemachine", "获取到的海拔" + location.getAltitude());
                //定义Maker坐标点
//                LatLng point = new LatLng(location.getLatitude(), location.getLongitude());
//                //构建Marker图标
//                BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.ic_user_location);
//                // 构建MarkerOption， 用于在地图上添加Marker
//                OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
//                // 在地图上添加Marker，并显示
//                baiduMap.addOverlay(option);
//                List<LocalLocationBean> latLngs = getLocalLocations();
//                for (int i = 0; i < latLngs.size(); i++) {
//                    if (getDistance(latLngs.get(i).longitude, latLngs.get(i).latitude, location.getLongitude(), location.getLatitude()) < Double.parseDouble(latLngs.get(i).distance)) {
//                        ToastUtils.msg("位置版触发了：" + latLngs.get(i).getVoiceId());
//                    }
//                }
//                ToastUtils.msg("定位到当前坐标："+location.getLatitude()+"=="+location.getLongitude());

                MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius())
                        .direction(location.getDirection()).latitude(location.getLatitude()).longitude(location.getLongitude()).build();

                baiduMap.setMyLocationData(locData);

                /**
                 * 位置板触发
                 */
//                for (int i = 0; i < circleCenterList.size(); i++) {
//                    if (LatUtil.getDistance(circleCenterList.get(i).longitude, circleCenterList.get(i).latitude, location.getLongitude(), location.getLatitude()) < radius) {
//                        ToastUtils.msg("位置版触发，开始播放第" + i+"段音频讲解");
//                    }
//                }

                //声明LocationClient类实例并配置定位参数

                if (isFristLocation) {
                    isFristLocation = false;
                    LatLng cenpt = new LatLng(location.getLatitude(), location.getLongitude());   //①
                    // 定义地图状态
                    MapStatus mMapStatus = new MapStatus.Builder().target(cenpt).zoom(18).build();  //②
                    // 定义地图状态将要发生的变化
                    MapStatusUpdate mMapStatusUpdate =
                            MapStatusUpdateFactory.newMapStatus(mMapStatus);   //③
                    // BaiduMap对象改变地图状态
                    baiduMap.setMapStatus(mMapStatusUpdate);    //④

                    //MapView要改变-->用BaiduMap去操作-->根据MapStatus的状态改变-->
                    //  BaiduMap看不懂状态MapStatus  需要MapStatusUpdate去描述-->BaiduMap通过描述去改变-->MapView改变

                    //普通地图
                    //  baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                    //卫星地图
                    // mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                    //开启交通图
                    //  baiduMap.setTrafficEnabled(true);
                    //定义Maker坐标点
//                    LatLng point1 = new LatLng(location.getLatitude(), location.getLongitude());
//                    LatLng point = new LatLng(30.758348, 103.98694);
                    //构建Marker图标
//                    BitmapDescriptor bitmap1 = BitmapDescriptorFactory.fromResource(R.mipmap.ic_user_location);
//                    // 构建MarkerOption， 用于在地图上添加Marker
//                    OverlayOptions option1 = new MarkerOptions().position(point1).icon(bitmap1);
//                    // 在地图上添加Marker，并显示
//                    baiduMap.addOverlay(option1);

                    SPHelper.getInstance(MapActivity.this).setLatitude(location.getLatitude() + "");
                    SPHelper.getInstance(MapActivity.this).setLongitude(location.getLongitude() + "");
                    SPHelper.getInstance(MapActivity.this).setCityName(location.getAddrStr());
                    //定位成功请求天气
                    weatherPresenter = new WeatherPresenter(MapActivity.this);
                    weatherPresenter.onCreate();
                    weatherPresenter.attachView(weatherView);
                    weatherPresenter.getWeather(location.getLongitude() + "", location.getLatitude() + "");

                    //定位成功之后请求景点列表
                    scenerySpotPresenter = new ScenerySpotPresenter(MapActivity.this);
                    scenerySpotPresenter.onCreate();
                    scenerySpotPresenter.attachView(scenerySpotView);
                    JSONObject requestData = new JSONObject();
                    try {
                        requestData.put("sceneryId", 1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestData.toString());
                    scenerySpotPresenter.getSceneryList(requestBody, 1);
                    showProgressDialog();
                }
                imgLocation.setOnClickListener(v -> {
                    isFristLocation = false;
                    baiduMap.clear();
                    LatLng center = new LatLng(location.getLatitude(), location.getLongitude());   //①
                    // 定义地图状态
                    MapStatus mMapStatus = new MapStatus.Builder().target(center).zoom(18).build();  //②
                    // 定义地图状态将要发生的变化
                    MapStatusUpdate mMapStatusUpdate =
                            MapStatusUpdateFactory.newMapStatus(mMapStatus);   //③
                    // BaiduMap对象改变地图状态
                    baiduMap.setMapStatus(mMapStatusUpdate);    //④

//                    MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius())
//                            .direction(location.getDirection()).latitude(location.getLatitude()).longitude(location.getLongitude()).build();
//                    baiduMap.setMyLocationData(locData);

                    //MapView要改变-->用BaiduMap去操作-->根据MapStatus的状态改变-->
                    //  BaiduMap看不懂状态MapStatus  需要MapStatusUpdate去描述-->BaiduMap通过描述去改变-->MapView改变

                    //普通地图
                    //baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                    //卫星地图
                    // mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                    //开启交通图
                    //baiduMap.setTrafficEnabled(true);
                    //定义Maker坐标点
//                    LatLng point = new LatLng(location.getLatitude(), location.getLongitude());
//                    //构建Marker图标
//                    BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.ic_user_location);
//                    // 构建MarkerOption， 用于在地图上添加Marker
//                    OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
//                    // 在地图上添加Marker，并显示
//                    baiduMap.addOverlay(option);


                    JSONObject requestFenceData = new JSONObject();
                    try {
                        requestFenceData.put("imei", getIMEI(MapActivity.this));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    RequestBody requestFenceBody = RequestBody.create(MediaType.parse("application/json"), requestFenceData.toString());
                    fencePresenter.getFence(requestFenceBody);
                });

            }
        }

    };

    /**
     * 定位SDK监听函数
     */
    @Override
    protected void onPause() {
        super.onPause();
        // activity 暂停时同时暂停地图控件
//        mapView.onPause();
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
        // activity 销毁时同时销毁地图控件
//        MapView.setMapCustomEnable(false);
//        mapView.onDestroy();
//        locationService.stop();
        isPlaying = false;
        EventBus.getDefault().unregister(this);
        mSoundManager.release();
        L.gi().d("进入onDestroy播放监听==========");
        if (serialPortUtils != null){
            serialPortUtils.closeSerialPort();
        }
        if (player != null) {
            L.gi().d("进入停止播放监听==========");
            player.stop();
            player.reset();
            player = null;
        }
        handler.removeMessages(100);


    }

    /**
     * 计算两经纬度距离
     *
     * @param long1 经度1
     * @param lat1  维度1
     * @param long2 经度2
     * @param lat2  纬度2
     * @return
     */
    public double getDistance(double long1, double lat1, double long2,
                              double lat2) {
        double a, b, R;
        R = 6378137; // 地球半径
        lat1 = lat1 * Math.PI / 180.0;
        lat2 = lat2 * Math.PI / 180.0;
        a = lat1 - lat2;
        b = (long1 - long2) * Math.PI / 180.0;
        double d;
        double sa2, sb2;
        sa2 = Math.sin(a / 2.0);
        sb2 = Math.sin(b / 2.0);
        d = 2 * R * Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(lat1) * Math.cos(lat2) * sb2 * sb2));
        return d;
    }


    List<OverlayOptions> overlayOptionsList = new ArrayList<>();
    List<Overlay> overlays = new ArrayList<>();

    int num = 0;

    /**
     * 添加marker
     */
    public synchronized Marker setMark(final LatLng latLng, final int id) {

        baiduMap.hideInfoWindow();
        L.gi().d("setMarker : lat : " + latLng.latitude + " lon : " + latLng.longitude);
        Bitmap bitmap = null;
        if (id == 1) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_scenery_marker1);
        } else if (id == 3) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_goods_default);
        } else if (id == 4) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_toile_default);
//            bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_goods_location);
        }
        zoomToSpan();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                zoomToSpan();
            }
        }, 800);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);
        OverlayOptions overlayOptions;
        overlayOptions = new MarkerOptions().position(latLng)
                .anchor(100, 100)
                .icon(bitmapDescriptor)
                .zIndex(5);
        overlayOptionsList.add(overlayOptions);
        Overlay overlay = baiduMap.addOverlay(overlayOptions);
        overlays.add(overlay);


        //定义Maker坐标点
//        LatLng point = new LatLng(Double.parseDouble(SPHelper.getInstance(MapActivity.this).getLatitude()), Double.parseDouble(SPHelper.getInstance(MapActivity.this).getLongitude()));
//        //构建Marker图标
//        BitmapDescriptor userBitmap = BitmapDescriptorFactory.fromResource(R.mipmap.ic_user_location);
//        // 构建MarkerOption， 用于在地图上添加Marker
//        OverlayOptions option = new MarkerOptions().position(point).icon(userBitmap);
//        // 在地图上添加Marker，并显示
//        baiduMap.addOverlay(option);

        return (Marker) overlay;
    }

    ScenerySpotView scenerySpotView = new ScenerySpotView() {
        @Override
        public void onSuccess(BaseBean<ScenerySpotListBean> mScenerySpotListBean) {
            dismissProgressDialog();
            scenerySpotListBean = mScenerySpotListBean;
            Logger.d("ScenerySpotListBean", mScenerySpotListBean.getValue().getList().toString());
            for (int i = 0; i < mScenerySpotListBean.getValue().getList().size(); i++) {
                final LatLng latLng = new LatLng(mScenerySpotListBean.getValue().getList().get(i).getLat(), mScenerySpotListBean.getValue().getList().get(i).getLng());
                final Bundle bundle = new Bundle();
                bundle.putSerializable("findCarBeen", mScenerySpotListBean.getValue().getList().get(i));
                setMark(latLng, markerType).setExtraInfo(bundle);
            }
        }

        @Override
        public void onError(String result) {
            dismissProgressDialog();
        }
    };
    WeatherView weatherView = new WeatherView() {
        @Override
        public void onSuccess(BaseBean<WeatherBean> mWeatherBean) {
            Log.d("com.guidemachine", "获取天气=======================");
            Logger.d("ScenerySpotListBean", mWeatherBean.getValue().toString());
            tvTemperature.setText(mWeatherBean.getValue().getTemperature());
        }

        @Override
        public void onError(String result) {
//            ToastUtils.msg(result);
        }
    };
    ShopMarkersView shopMarkersView = new ShopMarkersView() {
        @Override
        public void onSuccess(BaseBean<ShopMarkersBean> mShopMarkersBean) {
            dismissProgressDialog();
            Logger.d("mShopMarkersBean", mShopMarkersBean.getValue().getShopInfo().toString());
            for (int i = 0; i < mShopMarkersBean.getValue().getShopInfo().size(); i++) {
                final LatLng latLng = new LatLng(mShopMarkersBean.getValue().getShopInfo().get(i).getLat(), mShopMarkersBean.getValue().getShopInfo().get(i).getLon());
                final Bundle bundle = new Bundle();
                bundle.putSerializable("findCarBeen", mShopMarkersBean.getValue().getShopInfo().get(i));
                setMark(latLng, markerType).setExtraInfo(bundle);
            }
        }

        @Override
        public void onError(String result) {
            ToastUtils.msg(result);
        }
    };
    Polyline mPolyline;
    List<LatLng> points = new ArrayList<LatLng>();
    FenceView fenceView = new FenceView() {
        @Override
        public void onSuccess(BaseBean<List<FenceBean>> mFenceBean) {
            Log.d("com.guidemachine获取的电子围栏", mFenceBean.getValue() + "");
            if (mFenceBean.getValue().size() == 0) {
                return;
            }
            for (FenceBean i : mFenceBean.getValue()) {
                locationFenceList.add(i.getCoordinate());
            }
            //构建折线点坐标
            String[] splitstr = locationFenceList.get(0).split(",");
            for (String res : splitstr) {
                Logger.d("电子围栏", res);
//                locationFenceList1.add(res);
                String[] strings = res.split("_");
                Log.d("com.guidemachine获取的电子围栏", res);
                points.add(new LatLng(Double.parseDouble(strings[1]), Double.parseDouble(strings[0])));
            }
            //再次添加经纬度第一个点作为最后一个点，实现首尾相接
            points.add(new LatLng(Double.parseDouble(splitstr[0].split("_")[1]), Double.parseDouble(splitstr[0].split("_")[0])));
            //绘制折线
            OverlayOptions ooPolyline = new PolylineOptions()
                    .width(10)
                    .color(Color.parseColor("#f82328")).points(points);
            mPolyline = (Polyline) baiduMap.addOverlay(ooPolyline);
            creatFence(points);
        }

        @Override
        public void onError(String result) {
            ToastUtils.msg(result);
        }
    };
    // 第一次按下返回键的事件
    private long firstPressedTime;

    // System.currentTimeMillis() 当前系统的时间
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - firstPressedTime < 2000) {
            super.onBackPressed();
            logout();
            SPHelper.getInstance(MapActivity.this).setToken("");
        } else {
            Toast.makeText(MapActivity.this, "再按一次退出", Toast.LENGTH_SHORT).show();
            firstPressedTime = System.currentTimeMillis();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    public void zoomToSpan() {//将地图上显示的所有marker显示在视野里（此处的问题是只调用一次并不起效果，所以延时再调用一次）

        if (overlays.size() > 0) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (Overlay overlay : overlays) {
                // polyline 中的点可能太多，只按marker 缩放
                if (overlay instanceof Marker) {
                    builder.include(((Marker) overlay).getPosition());
                }
            }
            baiduMap.setMapStatus(MapStatusUpdateFactory
                    .newLatLngBounds(builder.build()));
        }
    }

    void logout() {//退出环信
        final ProgressDialog pd = new ProgressDialog(MapActivity.this);
        String st = getResources().getString(R.string.Are_logged_out);
        pd.setMessage(st);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        DemoHelper.getInstance().logout(true, new EMCallBack() {
            @Override
            public void onSuccess() {
                runOnUiThread(() -> {
                    pd.dismiss();
                    // show login screen
                    Log.d("TalkFragment", "退出环信成功");
                });
            }

            @Override
            public void onProgress(int progress, String status) {
                if (progress == 100) {
                    System.exit(0);
                    Log.d("TalkFragment", "退出环信成功" + progress);
                }
            }

            @Override
            public void onError(int code, String message) {
                runOnUiThread(() -> {
                    // TODO Auto-generated method stub
                    pd.dismiss();
                    Toast.makeText(MapActivity.this, "unbind devicetokens failed", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    //轨迹服务监听器
    private OnTraceListener traceListener = new OnTraceListener() {
        @Override
        public void onBindServiceCallback(int i, String s) {

        }

        @Override
        public void onStartTraceCallback(int i, String s) {

        }

        @Override
        public void onStopTraceCallback(int i, String s) {

        }

        @Override
        public void onStartGatherCallback(int i, String s) {

        }

        @Override
        public void onStopGatherCallback(int i, String s) {

        }

        @Override
        public void onPushCallback(byte messageType, PushMessage pushMessage) {
            if (messageType < 0x03 || messageType > 0x04) {
                return;
            }
            /**
             * 获取报警推送消息
             */
            FenceAlarmPushInfo alarmPushInfo = pushMessage.getFenceAlarmPushInfo();
            alarmPushInfo.getFenceId();//获取围栏id
            alarmPushInfo.getMonitoredPerson();//获取监控对象标识
            alarmPushInfo.getFenceName();//获取围栏名称
            alarmPushInfo.getPrePoint();//获取上一个点经度信息
            AlarmPoint alarmPoin = alarmPushInfo.getCurrentPoint();//获取报警点经纬度等信息
            alarmPoin.getCreateTime();//获取此位置上传到服务端时间
            alarmPoin.getLocTime();//获取定位产生的原始时间

            if (alarmPushInfo.getMonitoredAction() == MonitoredAction.enter) {//动作类型
                //进入围栏
                ToastUtils.msg("进入围栏");
                Vibrator vibrator = (Vibrator) MapActivity.this.getSystemService(MapActivity.this.VIBRATOR_SERVICE);
                vibrator.vibrate(1000);
            } else if (alarmPushInfo.getMonitoredAction() == MonitoredAction.exit) {
                //离开围栏
                ToastUtils.msg("离开围栏");
                Vibrator vibrator = (Vibrator) MapActivity.this.getSystemService(MapActivity.this.VIBRATOR_SERVICE);
                vibrator.vibrate(1000);
            }
        }

        @Override
        public void onInitBOSCallback(int i, String s) {

        }
    };

    public void creatFence(List<LatLng> points) {
        /**
         * 创建客户端圆形围栏，注:目前客户端围栏只支持圆形围栏
         * tag - 这次请求的唯一标识
         * serviceId -轨迹服务ID
         * fenceName - 围栏名称
         * entityName- 监控对象唯一标识，如果是手机，请用手机的唯一id
         * center - 围栏圆心
         * radius - 围栏半径
         * denoise - 围栏去噪精度，gps定位和网络定位都存在精度误差，会造成围栏误报警；
         如设置denoise=30，则定位精度大于30米的轨迹点都不会参与围栏计算。
         平均精度参考：GPS定位精度均值为15米，WIFI定位精度均值为40米，基站定位精度均值为300米。
         * coordType - 坐标类型
         */
//        CreateFenceRequest request = CreateFenceRequest.buildLocalCircleRequest(1, Constants.serviceId, "tome",
//                entityName, new com.baidu.trace.model.LatLng(30.614226, 104.069596), 10, 30, CoordType.bd09ll);

        /**
         * 创建服务端圆形围栏
         * 参数与客户端圆形围栏一样
         */

        /**
         * 创建位置板
         */
        CreateFenceRequest request = null;
//        //当前麦田中心
//        request = CreateFenceRequest.buildLocalCircleRequest(1, Constants.serviceId, "test",
//                "myTrace", new com.baidu.trace.model.LatLng(30.614192, 104.069563), radius, denoise, CoordType.bd09ll);
//        mClient.createFence(request, fenceListener);
//        //桂溪南站加油站
//        request = CreateFenceRequest.buildLocalCircleRequest(2, Constants.serviceId, "test2",
//                "myTrace2", new com.baidu.trace.model.LatLng(30.614507, 104.066662), radius, denoise, CoordType.bd09ll);
//        mClient.createFence(request, fenceListener);
//        //凯宴美湖
//        request = CreateFenceRequest.buildLocalCircleRequest(3, Constants.serviceId, "test3",
//                "myTrace3", new com.baidu.trace.model.LatLng(30.614468, 104.068364), radius, denoise, CoordType.bd09ll);
//        mClient.createFence(request, fenceListener);
//
//        //凯宴美湖
//        request = CreateFenceRequest.buildLocalCircleRequest(3, Constants.serviceId, "test4",
//                "myTrace4", new com.baidu.trace.model.LatLng(30.61455, 104.069249), radius, denoise, CoordType.bd09ll);
//        mClient.createFence(request, fenceListener);
//        //凯宴美湖
//        request = CreateFenceRequest.buildLocalCircleRequest(3, Constants.serviceId, "test5",
//                "myTrace5", new com.baidu.trace.model.LatLng(30.614627, 104.067434), radius, denoise, CoordType.bd09ll);
//        mClient.createFence(request, fenceListener);
//        //凯宴美湖
//        request = CreateFenceRequest.buildLocalCircleRequest(3, Constants.serviceId, "test6",
//                "myTrace6", new com.baidu.trace.model.LatLng(30.6146, 104.067987), radius, denoise, CoordType.bd09ll);
//        mClient.createFence(request, fenceListener);


//        CreateFenceRequest  request1 = CreateFenceRequest.buildServerCircleRequest(1, Constants.serviceId, "tome",
//                entityName, new com.baidu.trace.model.LatLng(30.614226, 104.069596), 10, 30, CoordType.bd09ll);

        //构建折线点坐标
//        com.baidu.trace.model.LatLng p1 = new com.baidu.trace.model.LatLng(40.0581750000, 116.3067370000);
//        com.baidu.trace.model.LatLng p2 = new com.baidu.trace.model.LatLng(40.0583410000, 116.3079580000);
//        com.baidu.trace.model.LatLng p3 = new com.baidu.trace.model.LatLng(40.0554970000, 116.3093600000);
//
//        com.baidu.trace.model.LatLng p4 = new com.baidu.trace.model.LatLng(40.0554140000, 116.3078150000);
//        com.baidu.trace.model.LatLng p5 = new com.baidu.trace.model.LatLng(30.613741632601716, 104.06923229443208);

        List<com.baidu.trace.model.LatLng> vertexes = new ArrayList<com.baidu.trace.model.LatLng>();
        vertexes.clear();
        for (LatLng latLng : points) {
            vertexes.add(new com.baidu.trace.model.LatLng(latLng.latitude, latLng.longitude));
        }
//        vertexes.add(p1);
//        vertexes.add(p2);
//        vertexes.add(p3);
//        vertexes.add(p4);
//      vertexes.add(p5);
        /**
         * 创建服务端多边形围栏
         * vertexes - 顶点坐标集合，List<com.baidu.trace.model.LatLng>
         * 顶点可设置地图点击监听获取，mBaiduMap.setOnMapClickListener
         * 围栏的创建不是所有地区都会成功，服务端会判断当前那个位置是不是时候生成围栏（保证围栏的精度也是可靠的），你可以选一个周边的点再试试
         */
        request = CreateFenceRequest.buildServerPolygonRequest(11, Constants.serviceId, "tome",
                entityName, vertexes, 30, CoordType.bd09ll);
        //发起创建围栏请求
        mClient.createFence(request, fenceListener);
        /**
         * 创建服务端线形围栏
         * vertexes - 顶点坐标集合，List<com.baidu.trace.model.LatLng>
         * offset - 偏离路线的垂直距离，偏离大于offset 报警
         */
//        CreateFenceRequest   request3 = CreateFenceRequest.buildServerPolylineRequest(1,Constants.serviceId, "tome",
//                entityName, vertexes, offset, 30, CoordType.bd09ll);

        /**
         * 创建服务端行政区围栏
         * district:行政区名称，注意写全，如北京市西城区
         */
//        CreateFenceRequest   request4 = CreateFenceRequest.buildServerDistrictRequest(1,Constants.serviceId, "tome", entityName,
//                district, 30);
//        mClient.clear();//停止轨迹服务
//        DeleteFenceRequest deleteFenceRequest = new DeleteFenceRequest(11, Constants.serviceId, "tome",
//                vertexes,"服务端围栏");
//        mClient.deleteFence(deleteFenceRequest,fenceListener);//创建之前先删除一下


    }

    OnFenceListener fenceListener = new OnFenceListener() {

        @Override
        public void onCreateFenceCallback(CreateFenceResponse response) {
            //创建围栏响应结果,能获取围栏的一些信息
            if (StatusCodes.SUCCESS != response.getStatus()) {
//                ToastUtils.msg("地理围栏:" + response.message);
                return;
            }
            response.getTag();//请求标识
            response.getDistrict();//获取行政区名称
            response.getFenceId();//创建的围栏id
            response.getFenceShape();//围栏形状
            response.getFenceType();//围栏类型（本地围栏、服务端围栏）
            //...方法不一一列举了，比较简单
//            ToastUtils.msg("地理围栏:" + response.message);

            Stroke stroke;
            stroke = new Stroke(5, Color.rgb(0x23, 0x19, 0xDC));
            for (com.baidu.mapapi.model.LatLng latLng : circleCenterList) {
                overlayOptions = new CircleOptions().fillColor(0x000000FF).center(latLng)
                        .stroke(stroke).zIndex(1).radius((int) radius);
                currentOverlay = baiduMap.addOverlay(overlayOptions);//画圆
            }

            //画圆，主要是这里
//            OverlayOptions ooCircle = new CircleOptions().fillColor(0x384d73b3)
//                    .center(new LatLng(30.614226, 104.069596)).stroke(new Stroke(3, 0x784d73b3))
//                    .radius(10);
//            baiduMap.addOverlay(ooCircle);


//            LatLng p1 = new LatLng(30.61428553231252, 104.06944788775448);
//            LatLng p2 = new LatLng(30.61428553231252, 104.06966348107687);
//            LatLng p3 = new LatLng(30.614184522599555, 104.06965449802178);
//            LatLng p4 = new LatLng(30.614200062562322, 104.06944788775448);
//            LatLng p5 = new LatLng(30.61428553231252, 104.06944788775448);
//
//            List<LatLng> vertexes = new ArrayList<LatLng>();
//            vertexes.add(p1);
//            vertexes.add(p2);
//            vertexes.add(p3);
//            vertexes.add(p4);
//            vertexes.add(p5);
//            //设置折线的属性
//            OverlayOptions mOverlayOptions = new PolylineOptions()
//                    .width(10)
//                    .color(0xAAFF0000)
//                    .points(vertexes);
//            //在地图上绘制折线
//            //mPloyline 折线对象
//            Overlay mPolyline = baiduMap.addOverlay(mOverlayOptions);
        }

        @Override
        public void onUpdateFenceCallback(UpdateFenceResponse response) {
            //更新围栏响应结果
            //...
        }

        @Override
        public void onDeleteFenceCallback(DeleteFenceResponse response) {
            //删除围栏响应结果
            response.getFenceIds();//获取删除的围栏id
            //...
        }

        @Override
        public void onFenceListCallback(FenceListResponse response) {
            //获取围栏列表响应结果
            response.getSize();//围栏个数
            List<FenceInfo> fenceInfos = response.getFenceInfos();//获取围栏信息列表
            for (FenceInfo fenceInfo : fenceInfos) {
                switch (fenceInfo.getFenceShape()) {//判断围栏形状
                    case circle://圆形
                        CircleFence circleFence = fenceInfo.getCircleFence();
                        circleFence.getFenceId();
                        circleFence.getCenter();
                        circleFence.getRadius();
                        circleFence.getDenoise();//去噪精度
                        circleFence.getMonitoredPerson();//监控设备的唯一标识
                        //...获取圆心和半径就可以在地图上画圆形图层

                        break;
                    case polygon://多边形
                        PolygonFence polygonFence = fenceInfo.getPolygonFence();
                        //获取多边形顶点集合
                        List<com.baidu.trace.model.LatLng> polygonVertexes = polygonFence.getVertexes();
                        //...获取顶点坐标可以在地图上画多边形图层
                        Logger.d("多边形顶点集合", polygonVertexes.toString());
                        break;
                    case polyline://线形
                        PolylineFence polylineFence = fenceInfo.getPolylineFence();
                        //获取线形顶点集合
                        List<com.baidu.trace.model.LatLng> polylineVertexes = polylineFence.getVertexes();
                        //...
                        break;
                    case district:
                        DistrictFence districtFence = fenceInfo.getDistrictFence();
                        districtFence.getDistrict();//获取行政区名称
                        //...注：行政区围栏并能像多边形一样返回定点集合，行政区范围很大，点很多...，
                        //如果想获取行政区的边界点坐标结合，请使用baidumapapi_search_v4_3_1.jar中DistrictSearch类
                        break;
                }
            }
        }

        @Override
        public void onMonitoredStatusCallback(MonitoredStatusResponse response) {
            //查询监控对象状态响应结果
            List<MonitoredStatusInfo> monitoredStatusInfos = response.getMonitoredStatusInfos();
            for (MonitoredStatusInfo monitoredStatusInfo : monitoredStatusInfos) {
                monitoredStatusInfo.getFenceId();
                MonitoredStatus status = monitoredStatusInfo.getMonitoredStatus();//获取状态
                switch (status) {
                    case in:
                        //监控的设备在围栏内
                        ToastUtils.msg("监控的设备在围栏内");
                        break;
                    case out:
                        //监控的设备在围栏外
                        ToastUtils.msg("监控的设备在围栏外");
                        break;
                    case unknown:
                        //监控的设备状态未知
                        break;
                }
            }
        }

        @Override
        public void onMonitoredStatusByLocationCallback(MonitoredStatusByLocationResponse response) {
            //查询监控对象在指定位置的状态响应结果，api同onMonitoredStatusCallback
        }

        @Override
        public void onHistoryAlarmCallback(HistoryAlarmResponse response) {
            //查询围栏历史报警信息响应结果
            //获取报警信息列表，FenceAlarmInfo继承FenceAlarmPushInfo
            List<FenceAlarmInfo> fenceAlarmInfos = response.getFenceAlarmInfos();
        }
    };

    /**
     * 开始步行导航
     */
    private void startWalkNavi() {
        Log.d(TAG, "startBikeNavi");
        try {
            WalkNavigateHelper.getInstance().initNaviEngine(this, new IWEngineInitListener() {
                @Override
                public void engineInitSuccess() {
                    Log.d(TAG, "WalkNavi engineInitSuccess");
                    routePlanWithWalkParam();
                }

                @Override
                public void engineInitFail() {
                    Log.d(TAG, "WalkNavi engineInitFail");
                }
            });
        } catch (Exception e) {
            Log.d(TAG, "startBikeNavi Exception");
            e.printStackTrace();
        }
    }

    /**
     * 发起步行导航算路
     */
    private void routePlanWithWalkParam() {
        WalkNavigateHelper.getInstance().routePlanWithParams(walkParam, new IWRoutePlanListener() {
            @Override
            public void onRoutePlanStart() {
                Log.d(TAG, "WalkNavi onRoutePlanStart");
            }

            @Override
            public void onRoutePlanSuccess() {
                Log.d("View", "onRoutePlanSuccess");
                Intent intent = new Intent();
                intent.setClass(MapActivity.this, WNaviGuideActivity.class);
                startActivity(intent);
            }

            @Override
            public void onRoutePlanFail(WalkRoutePlanError error) {
                Log.d(TAG, "WalkNavi onRoutePlanFail" + error.name().toString());
            }

        });
    }

    public List<LocalLocationBean> getLocalLocations() {//获取位置版触发经纬度
        com.alibaba.fastjson.JSONObject jsonData =
                com.alibaba.fastjson.JSONObject.parseObject(FileUtil.readAssetsTxt(MapActivity.this, "SpeechFile"));
        List<String> list = new ArrayList<>();
        list.add(jsonData.get("content").toString());
        Logger.d("jsonData", list.toString());
        String[] list1 = list.get(0).split(";");
//        List<LatLng> lanLngsLists = new ArrayList<>();
        List<LocalLocationBean> locationLists = new ArrayList<>();
//        lanLngsLists.add(new LatLng())
        for (String res : list1) {
            Logger.d("位置版触发", res);
            String[] strings = res.split(",");
//            lanLngsLists.add(new LatLng(Double.parseDouble(strings[1]), Double.parseDouble(strings[0])));
            LocalLocationBean bean = new LocalLocationBean();
            bean.setLongitude(Double.parseDouble(strings[0]));
            bean.setLatitude(Double.parseDouble(strings[1]));
            bean.setDistance(strings[2]);
            bean.setVoiceId(strings[3]);
            locationLists.add(bean);
        }
        Logger.d("位置版触发", locationLists.toString());

        return locationLists;
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 100:
//                    Toast.makeText(MapActivity.this, "获取到信号强度：" + mSigal, Toast.LENGTH_SHORT).show();
                    if(isPlaying){
                        if (player != null) {
                            if(!player.isPlaying()){
                                player.start();
                            }
                            L.gi().d(player.isPlaying() + "==================");
                        }else{
                            player = MediaPlayer.create(MapActivity.this, R.raw.voice1);
                            player.start();
                        }
                    }
                    break;
            }
        }
    };

}
