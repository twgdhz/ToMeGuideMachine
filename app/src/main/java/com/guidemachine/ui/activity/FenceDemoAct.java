package com.guidemachine.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.App;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.Trace;
import com.baidu.trace.api.fence.AlarmPoint;
import com.baidu.trace.api.fence.CreateFenceRequest;
import com.baidu.trace.api.fence.CreateFenceResponse;
import com.baidu.trace.api.fence.DeleteFenceResponse;
import com.baidu.trace.api.fence.FenceAlarmPushInfo;
import com.baidu.trace.api.fence.FenceListRequest;
import com.baidu.trace.api.fence.FenceListResponse;
import com.baidu.trace.api.fence.FenceType;
import com.baidu.trace.api.fence.HistoryAlarmResponse;
import com.baidu.trace.api.fence.MonitoredAction;
import com.baidu.trace.api.fence.MonitoredStatusByLocationResponse;
import com.baidu.trace.api.fence.MonitoredStatusResponse;
import com.baidu.trace.api.fence.OnFenceListener;
import com.baidu.trace.api.fence.UpdateFenceResponse;
import com.baidu.trace.model.CoordType;
import com.baidu.trace.model.OnTraceListener;
import com.baidu.trace.model.PushMessage;
import com.baidu.trace.model.StatusCodes;
import com.guidemachine.R;
import com.guidemachine.base.ui.BaseActivity;
import com.guidemachine.constant.Constants;
import com.guidemachine.service.IGetMessageCallBack;
import com.guidemachine.service.MqttService;
import com.guidemachine.service.MyServiceConnection;
import com.guidemachine.service.entity.LocalLocationBean;
import com.guidemachine.service.presenter.FencePresenter;
import com.guidemachine.util.L;
import com.guidemachine.util.LatUtil;
import com.guidemachine.util.LocationService;
import com.guidemachine.util.LogUtils;
import com.guidemachine.util.MobileInfoUtil;
import com.guidemachine.util.ToastUtils;
import com.guidemachine.util.share.SPHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FenceDemoAct extends BaseActivity implements BaiduMap.OnMapLoadedCallback {

    private LocationService locationService;
    private static final String TAG = "lzy";
    private BaiduMap baiduMap;
    double longitude;
    double latitude;
    @BindView(R.id.mapView)
    MapView mapView;
    @BindView(R.id.img_location)
    ImageView imgLocation;
    //是否是第一次定位
    private volatile boolean isFristLocation = true;

    //获取当前绑定的电子围栏
    FencePresenter fencePresenter;
    List<String> locationFenceList = new ArrayList<>();
    LBSTraceClient mClient;
    private String entityName;
    /**
     * 围栏监听器
     */
    private OnFenceListener fenceListener = null;
    /**
     * 操作围栏时，正在操作的围栏标识，格式：fenceType_fenceId
     */
    private String fenceKey;
    //围栏半径
    private double radius = 5;

    //去燥
    private int denoise = 200;
    /**
     * 圆形围栏中心点坐标（地图坐标类型）
     */
//    private com.baidu.mapapi.model.LatLng circleCenter = new LatLng(39.900829, 116.329616);
    private List<com.baidu.mapapi.model.LatLng> circleCenterList;
    private List<com.baidu.trace.model.LatLng> circleTraceCenterList;
    private OverlayOptions overlayOptions;
    private Overlay currentOverlay;
    private MyServiceConnection serviceConnection;
    private MqttService mqttService;
    BitmapDescriptor mCurrentMarker;

    @Override
    protected void InitialView() {

        circleCenterList = new ArrayList<>();
        circleCenterList.add(new LatLng(30.614245, 104.06958));
        circleCenterList.add(new LatLng(30.612188, 104.072501));
        circleCenterList.add(new LatLng(30.613617, 104.075304));
        circleTraceCenterList = new ArrayList<>();

        initListener();
        baiduMap = mapView.getMap();
        // 不显示地图缩放控件（按钮控制栏）
        mapView.showZoomControls(false);
        baiduMap.setMyLocationEnabled(true);
        //更改指南针位置
        baiduMap.setCompassPosition(new Point(650, 80));
//        baiduMap.clear();//先清除一下图层
        mCurrentMarker = BitmapDescriptorFactory.fromResource(R.mipmap.ic_user_location);
        baiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.NORMAL, true, mCurrentMarker));
        // 设置地图加载监听
        baiduMap.setOnMapLoadedCallback(this);
        // -----------location config ------------
        locationService = ((App) getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        //注册监听
        //声明LocationClient类实例并配置定位参数
        LocationClientOption locationOption = new LocationClientOption();
        locationOption.setScanSpan(1000);
        //高精度模式
        locationOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //开启gps定位
        locationOption.setOpenGps(true);
//        locationService.setLocationOption(locationService.getDefaultLocationClientOption(this));
        locationService.setLocationOption(locationOption);


        locationService.start();
        entityName = MobileInfoUtil.getIMEI(FenceDemoAct.this);
        //鹰眼相关
        mClient = new LBSTraceClient(FenceDemoAct.this);
        //初始化鹰眼服务端，serviceId：服务端唯一id，entityName：监控的对象，可以是手机设备唯一标识
        Trace mTrace = new Trace(Constants.serviceId, entityName);
        mClient.startTrace(mTrace, traceListener);//开始服务
        mClient.startGather(traceListener);//开启采集

        createFence();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    protected int setRootViewId() {
        return R.layout.fence_demo_layout;
    }

    @Override
    protected boolean setIsFull() {
        return false;
    }

    private void createFence() {
        CreateFenceRequest request = null;
        request = CreateFenceRequest.buildLocalCircleRequest(1, Constants.serviceId, "test",
                "myTrace", new com.baidu.trace.model.LatLng(30.614207, 104.069568), radius, denoise, CoordType.bd09ll);
        mClient.createFence(request, fenceListener);
        request = CreateFenceRequest.buildLocalCircleRequest(2, Constants.serviceId, "test2",
                "myTrace2", new com.baidu.trace.model.LatLng(30.612188, 104.072501), radius, denoise, CoordType.bd09ll);
        mClient.createFence(request, fenceListener);
        request = CreateFenceRequest.buildLocalCircleRequest(3, Constants.serviceId, "test3",
                "myTrace3", new com.baidu.trace.model.LatLng(30.613617, 104.075304), radius, denoise, CoordType.bd09ll);
        mClient.createFence(request, fenceListener);
    }

    private void initListener() {

        fenceListener = new OnFenceListener() {
            @Override
            public void onCreateFenceCallback(CreateFenceResponse response) {
//                Log.d("com.baidu.track","onCreateFenceCallback=========");
                //创建围栏响应结果,能获取围栏的一些信息
                if (StatusCodes.SUCCESS != response.getStatus()) {
                    return;
                }
                response.getTag();//请求标识
                response.getDistrict();//获取行政区名称
                response.getFenceId();//创建的围栏id
                response.getFenceShape();//围栏形状
                response.getFenceType();//围栏类型（本地围栏、服务端围栏）
                //...方法不一一列举了，比较简单
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(response.getTag() + "=" + response.getDistrict() + "=" + response.getFenceId() + "=" + response.getFenceType());
                Log.d("com.guidemachine", "onCreateFenceCallback2==" + stringBuilder.toString());

                //围栏的key
                fenceKey = response.getFenceType() + "_" + response.getFenceId();//

                Stroke stroke;
                stroke = new Stroke(5, Color.rgb(0x23, 0x19, 0xDC));

                for (com.baidu.mapapi.model.LatLng latLng : circleCenterList) {
                    overlayOptions = new CircleOptions().fillColor(0x000000FF).center(latLng)
                            .stroke(stroke).zIndex(1).radius((int) radius);
                    currentOverlay = baiduMap.addOverlay(overlayOptions);//画圆
                }

            }

            @Override
            public void onUpdateFenceCallback(UpdateFenceResponse response) {
                Log.d("com.baidu.track", "onUpdateFenceCallback=========");
            }

            @Override
            public void onDeleteFenceCallback(DeleteFenceResponse response) {
                Log.d("com.baidu.track", "onDeleteFenceCallback=========");

            }

            @Override
            public void onFenceListCallback(FenceListResponse response) {
                //获取围栏列表响应结果
                Log.d("com.baidu.track", "onFenceListCallback===获取到围栏监听======");
                ToastUtils.msg("===========获取到了围栏回调===============");
            }

            @Override
            public void onMonitoredStatusCallback(MonitoredStatusResponse response) {
                Log.d("com.baidu.track", "onMonitoredStatusCallback=========");

            }

            @Override
            public void onMonitoredStatusByLocationCallback(MonitoredStatusByLocationResponse response) {
                Log.d("com.baidu.track", "onMonitoredStatusByLocationCallback=========");

            }

            @Override
            public void onHistoryAlarmCallback(HistoryAlarmResponse response) {
                Log.d("com.baidu.track", "onHistoryAlarmCallback=========");

            }
        };
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
            Log.d("com.baidu.track", "onFenceListCallback===获取到围栏报警监听======");
            ToastUtils.msg("===========监听到围栏报警===============");
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
                Vibrator vibrator = (Vibrator) FenceDemoAct.this.getSystemService(FenceDemoAct.this.VIBRATOR_SERVICE);
                vibrator.vibrate(1000);
            } else if (alarmPushInfo.getMonitoredAction() == MonitoredAction.exit) {
                //离开围栏
                ToastUtils.msg("离开围栏");
                Vibrator vibrator = (Vibrator) FenceDemoAct.this.getSystemService(FenceDemoAct.this.VIBRATOR_SERVICE);
                vibrator.vibrate(1000);
            }
        }

        @Override
        public void onInitBOSCallback(int i, String s) {

        }
    };
    @Override
    public void onMapLoaded() {
        Log.d("com.baidu.track", "onMapLoaded===地图监听变化======");
        queryFenceList(FenceType.local);

    }
    private void queryFenceList(FenceType fenceType) {
        FenceListRequest request = null;
        switch (fenceType) {
            case local:
                request = FenceListRequest.buildLocalRequest(1,
                        Constants.serviceId, "myTrace", null);
                break;

            default:
                break;
        }

        mClient.queryFenceList(request, fenceListener);
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
                Log.i("com.guidemachine", "获取当前的定位" + location.getLatitude() + " " + location.getLongitude());
                longitude = location.getLongitude();
                latitude = location.getLatitude();


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

                for (int i = 0; i < circleCenterList.size(); i++) {
                    if (LatUtil.getDistance(circleCenterList.get(i).longitude, circleCenterList.get(i).latitude, location.getLongitude(), location.getLatitude()) < 20) {
//                        ToastUtils.msg("位置版触发了：" + i);
                    }
                }
                MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius())
                        .direction(location.getDirection()).latitude(30.910529).longitude(103.570938).build();

                baiduMap.setMyLocationData(locData);

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

//                    MapStatus.Builder builder = new MapStatus.Builder();
//                    //设置缩放中心点；缩放比例；
//                    builder.target(cenpt).zoom(18.0f);
//                    //给地图设置状态
//                    baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));


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
////                    LatLng point = new LatLng(30.758348, 103.98694);
//                    //构建Marker图标
//                    BitmapDescriptor bitmap1 = BitmapDescriptorFactory.fromResource(R.mipmap.ic_user_location);
//                    // 构建MarkerOption， 用于在地图上添加Marker
//                    OverlayOptions option1 = new MarkerOptions().position(point1).icon(bitmap1);
//                    // 在地图上添加Marker，并显示
//                    baiduMap.addOverlay(option1);

                    SPHelper.getInstance(FenceDemoAct.this).setLatitude(location.getLatitude() + "");
                    SPHelper.getInstance(FenceDemoAct.this).setLongitude(location.getLongitude() + "");
                    SPHelper.getInstance(FenceDemoAct.this).setCityName(location.getAddrStr());
                    //定位成功请求天气
//                    weatherPresenter = new WeatherPresenter(FenceDemoAct.this);
//                    weatherPresenter.onCreate();
//                    weatherPresenter.attachView(weatherView);
//                    weatherPresenter.getWeather(location.getLongitude() + "", location.getLatitude() + "");
//
//                    //定位成功之后请求景点列表
//                    scenerySpotPresenter = new ScenerySpotPresenter(FenceDemoAct.this);
//                    scenerySpotPresenter.onCreate();
//                    scenerySpotPresenter.attachView(scenerySpotView);
//                    JSONObject requestData = new JSONObject();
//                    try {
//                        requestData.put("sceneryId", 1);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestData.toString());
//                    scenerySpotPresenter.getSceneryList(requestBody, 1);
//                    showProgressDialog();
                }
                imgLocation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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
                        //MapView要改变-->用BaiduMap去操作-->根据MapStatus的状态改变-->
                        //  BaiduMap看不懂状态MapStatus  需要MapStatusUpdate去描述-->BaiduMap通过描述去改变-->MapView改变

                        //普通地图
                        //baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                        //卫星地图
                        // mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                        //开启交通图
                        //baiduMap.setTrafficEnabled(true);
                        //定义Maker坐标点
                        LatLng point = new LatLng(location.getLatitude(), location.getLongitude());
                        //构建Marker图标
                        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.ic_user_location);
                        // 构建MarkerOption， 用于在地图上添加Marker
                        OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
                        // 在地图上添加Marker，并显示
                        baiduMap.addOverlay(option);


//                        JSONObject requestFenceData = new JSONObject();
//                        try {
//                            requestFenceData.put("imei", getIMEI(MapActivity.this));
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        RequestBody requestFenceBody = RequestBody.create(MediaType.parse("application/json"), requestFenceData.toString());
//                        fencePresenter.getFence(requestFenceBody);
                    }
                });

            }
        }
    };

}
