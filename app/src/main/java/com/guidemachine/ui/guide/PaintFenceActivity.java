package com.guidemachine.ui.guide;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polygon;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.SpatialRelationUtil;
import com.guidemachine.MainActivity;
import com.guidemachine.R;
import com.guidemachine.base.ui.BaseActivity;
import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.FencInfoBean;
import com.guidemachine.service.presenter.GuideAddFencePresenter;
import com.guidemachine.service.view.BaseView;
import com.guidemachine.util.Logger;
import com.guidemachine.util.StatusBarUtils;
import com.guidemachine.util.ToastUtils;
import com.guidemachine.util.share.SPHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * @author ChenLinWang
 * @email 422828518@qq.com
 * @create 2018/12/25 0025 16:14
 * description: 添加围栏
 */
public class PaintFenceActivity extends BaseActivity implements View.OnClickListener,
        BaiduMap.OnMapClickListener, BaiduMap.OnMarkerClickListener, BaiduMap.OnMarkerDragListener {
    protected Button btnAdd;
    protected MapView map;
    protected ImageView location;
    protected Button btnCheck;
    private BaiduMap baidumap;
    private ImageView imgFenceEdit, imgFenceClear, imgDrawFence;
    private TextView tvRight;
    //定位相关
    private LocationClient mLocationClient;
    private MyLocationListener mLocationListener;
    //是否第一次定位，如果是第一次定位的话要将自己的位置显示在地图 中间
    private boolean isFirstLocation = true;

    //marker 相关
    private Marker marker;
    List<Marker> markers = new ArrayList<>();
    //算是map的索引，通过此id 来按顺序取出坐标点
    private List<String> ids = new ArrayList<>();
    //用来存储坐标点
    private Map<String, LatLng> latlngs = new HashMap<>();

    private InfoWindow mInfoWindow;
    //线
    private Polyline mPolyline;
    //多边形
    private Polygon polygon;
    //private List<Polygon> polygons = new ArrayList<>();
    private double latitude;
    private double longitude;
    private double la;
    private double lo;

    private int size;
    //根据别名来存储画好的多边形
    private Map<String, Polygon> polygonMap = new HashMap<>();
    //多边形的别名
    private List<String> aliasname = new ArrayList<>();
    //
    private boolean polygonContainsPoint;
    //用来存储一个点所在的所有的区域
    List<String> areas = new ArrayList<>();
    private String fenceType;
    private String scenerySpot;
    List<LatLng> pts = new ArrayList<LatLng>();
    GuideAddFencePresenter guideAddFencePresenter;

    @Override
    protected int setRootViewId() {
        return R.layout.activity_paint_fence;
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        super.setContentView(R.layout.activity_paint_fence);
//
//    }

    @Override
    protected void InitialView() {
        initView();
        //定位
        initLocation();
        StatusBarUtils.setWindowStatusBarColor(PaintFenceActivity.this, R.color.white);
//        btnAdd=findViewById(R.id.btn_pop);
//        btnAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AddFencePop addFencePop=new AddFencePop(PaintFenceActivity.this);
//                addFencePop.showPopupWindow(btnAdd);
//            }
//        });

        // 不显示地图缩放控件（按钮控制栏）
        map.showZoomControls(false);
        location();
        Log.d("手机imei", getIMEI(PaintFenceActivity.this));
        guideAddFencePresenter = new GuideAddFencePresenter(PaintFenceActivity.this);
        guideAddFencePresenter.onCreate();
        guideAddFencePresenter.attachView(baseView);
    }


    @Override
    public void onClick(View view) {
        //用来确定多变形
        if (view.getId() == R.id.tv_right) {
            //--------------------------确定多边形的大小和别名-----------------------------

            LatLng l = null;
            la = 0;
            lo = 0;
            size = ids.size();
            if (size <= 2) {
                Toast.makeText(this, "点数必须大于2", Toast.LENGTH_SHORT).show();
                return;
            }
            for (int i = 0; i < size; i++) {
                l = latlngs.get(ids.get(i));
                la = la + l.latitude;
                lo = lo + l.longitude;
            }

            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle("请输入名字：");
            View inflate = View.inflate(this, R.layout.dialog_aliasname, null);
            final EditText edt_alias = inflate.findViewById(R.id.et_fence_name);
            final Spinner spFenceType = inflate.findViewById(R.id.sp_fence_type);//围栏类型
            final Spinner spFenceSpot = inflate.findViewById(R.id.sp_scenery_spot);//所属景点
            final ImageView imgClose = inflate.findViewById(R.id.img_close);//关闭
            final EditText etInFence = inflate.findViewById(R.id.et_in_fence);
            final EditText etOutFence = inflate.findViewById(R.id.et_out_fence);
            final String[] arrFenceType = {"入围栏", "出围栏", "出入围栏"};
            final String[] arrFenceSpot = {"薛涛井", "望江楼", "天涯海角"};
            //此处有知识点：https://www.jianshu.com/p/ccb5930f4a16    android:spinnerMode="dialog"
            ArrayAdapter<String> arrFenceTypeAdapter = new ArrayAdapter<String>(PaintFenceActivity.this, android.R.layout.simple_spinner_dropdown_item, arrFenceType);
            arrFenceTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spFenceType.setAdapter(arrFenceTypeAdapter);
            spFenceType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    fenceType = arrFenceType[i];
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            final ArrayAdapter<String> arrFenceSpotAdapter = new ArrayAdapter<String>(PaintFenceActivity.this, android.R.layout.simple_spinner_dropdown_item, arrFenceSpot);
            arrFenceSpotAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spFenceSpot.setAdapter(arrFenceSpotAdapter);
            spFenceSpot.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    scenerySpot = arrFenceSpot[i];
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            builder.setView(inflate);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String trim = edt_alias.getText().toString().trim();
                    if (trim.equals("")) {
                        Toast.makeText(PaintFenceActivity.this, "名不能为空！", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    drawPolygon();
                    // 添加文字，求出多边形的中心点向中心点添加文字
                    LatLng llText = new LatLng(la / size, lo / size);
                    OverlayOptions ooText = new TextOptions()
                            .fontSize(24).fontColor(0xFFFF00FF).text(trim + "")
                            .position(llText);
                    //构建用户绘制多边形的Option对象
                    OverlayOptions polygonOption = new PolygonOptions()
                            .points(pts)
                            .stroke(new Stroke(2, Color.parseColor("#e53e42")))
                            .fillColor(Color.parseColor("#99ffffff"));
                    baidumap.addOverlay(ooText);
//                    baidumap.addOverlay(polygonOption);
                    polygonMap.put(trim, polygon);
                    aliasname.add(trim);
                    polygon = null;
                    Log.e("aaa", "多边形有几个：" + polygonMap.size());
                    Log.e("aaa", "别名有：" + aliasname.toString());
                    for (int j = 0; j < markers.size(); j++) {
                        markers.get(j).remove();
                    }
                    //polygons.add(polygon);
                    //polygon = null;
                    latlngs.clear();
                    ids.clear();


                    JSONObject requestData = new JSONObject();
                    try {
                        requestData.put("imei", getIMEI(PaintFenceActivity.this));
//                        requestData.put("sceneryId ", SPHelper.getInstance(PaintFenceActivity.this).getSceneryId());
                        requestData.put("sceneryId", "1");
                        requestData.put("scenerySpotId", "3");
                        requestData.put("name", trim);
                        requestData.put("type", 2);//电子围栏类型 报警方式(出围栏报警 = 0,进围栏报警 = 1,进出围栏报警 = 2
                        requestData.put("inHintWord", etInFence.getText().toString());
                        requestData.put("outHintWord", etOutFence.getText().toString());
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int s = 0; s < pts.size(); s++) {
                            stringBuilder.append(pts.get(s).longitude + "_" + pts.get(s).latitude).append(",");
                        }
                        requestData.put("coordinate", stringBuilder.toString());
                        Logger.d("coordinate   →", stringBuilder.toString());
                        showProgressDialog();
                        stringBuilder.delete(0, stringBuilder.length());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestData.toString());
                    guideAddFencePresenter.addfence(requestBody);
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            dismissProgressDialog();
//                            ToastUtils.msg("围栏创建成功");
//                            finish();
//                        }
//                    }, 2000);
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.create().show();
//            imgClose.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    builder.create().dismiss();
//                }
//            });

        } else if (view.getId() == R.id.img_clear_fence) {
            map.getMap().clear();
            latlngs.clear();
            ids.clear();
            markers.clear();
            areas.clear();
            //polygons.clear();
            //用来定位
        } else if (view.getId() == R.id.location) {
            //点击定位按钮，返回自己的位置
            isFirstLocation = true;
            showInfo("返回自己位置");
            location();
        } else if (view.getId() == R.id.btn_check) {

            String name = null;
            Polygon polygon = null;
            areas.clear();
            for (int i = 0; i < aliasname.size(); i++) {
                name = aliasname.get(i);
                Log.e("aaa", "检查的别名是：" + name);
                polygon = polygonMap.get(name);
                String s = polygon.getPoints().toString();
                Log.e("aaa", "sssss---->" + s);
                //判断一个点是否在多边形中
                polygonContainsPoint = SpatialRelationUtil.isPolygonContainsPoint(polygon.getPoints(), new LatLng(latitude, longitude));
                if (polygonContainsPoint) {
                    Toast.makeText(this, "该点在 " + name + " 区域内。", Toast.LENGTH_SHORT).show();
                    areas.add(name);
                }
            }
            Log.e("aaa", "areas" + areas.toString());
            if (areas.size() > 0) {
                String message = areas.toString();
                showDialog("所在的区域有：" + message);
            } else {
                showDialog("该点不在任何区域内。");
            }

        }
    }

    BaseView baseView = new BaseView() {
        @Override
        public void onSuccess(BaseBean mBaseBean) {
            dismissProgressDialog();
            ToastUtils.msg(mBaseBean.getResultStatus().getResultMessage().toString());
            finish();
        }

        @Override
        public void onError(String result) {
            dismissProgressDialog();
            ToastUtils.msg(result);
        }
    };

    public void location() {
        LatLng center = new LatLng(30.911094, 103.573897);   //①
        // 定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder().target(center).zoom(18).build();  //②
        // 定义地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate =
                MapStatusUpdateFactory.newMapStatus(mMapStatus);   //③
        // BaiduMap对象改变地图状态
        baidumap.setMapStatus(mMapStatusUpdate);    //④
        //MapView要改变-->用BaiduMap去操作-->根据MapStatus的状态改变-->
        //  BaiduMap看不懂状态MapStatus  需要MapStatusUpdate去描述-->BaiduMap通过描述去改变-->MapView改变
        //定义Maker坐标点
        LatLng point = new LatLng(30.911094, 103.573897);
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.ic_user_location);
        // 构建MarkerOption， 用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
        // 在地图上添加Marker，并显示
        baidumap.addOverlay(option);
    }

    private void showDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("查找结果");

        builder.setMessage(message);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }


    /**
     * 通过点击地图，来获取坐标
     *
     * @param latLng
     */
    @Override
    public void onMapClick(LatLng latLng) {
        Toast.makeText(this, "坐标是：" + latLng.latitude + "," + latLng.longitude, Toast.LENGTH_SHORT).show();
        Log.e("aaa", "ditu d zuobiao is -->" + latLng.latitude + "," + latLng.longitude);
        latitude = latLng.latitude;
        longitude = latLng.longitude;
        pts.add(latLng);

        //向地图添加marker
        addMarler(latitude, longitude);
        if (ids.size() >= 2) {
            drawLine();
        }
    }

    /**
     * 地图上marker的点击事件
     *
     * @param marker
     * @return
     */
    @Override
    public boolean onMarkerClick(final Marker marker) {
        Button button = new Button(getApplicationContext());
        button.setBackgroundResource(R.drawable.popup);
        button.setText("删除");
        button.setTextColor(Color.BLACK);
        //button.setWidth(300);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                marker.remove();
                String id1 = marker.getId();
                ids.remove(id1);
                latlngs.remove(id1);
                Log.e("aaa", "删除后map的size--》" + latlngs.size());
                baidumap.hideInfoWindow();
                if (ids.size() < 2) {
                    if (mPolyline != null) {
                        mPolyline.remove();
                    }
                    return;
                }
                drawLine();
            }
        });
        LatLng ll = marker.getPosition();
        mInfoWindow = new InfoWindow(button, ll, -50);
        baidumap.showInfoWindow(mInfoWindow);
        return true;
    }


    @Override
    public void onMarkerDragEnd(Marker marker) {
        String id = marker.getId();
        Log.e("aaa", "id-->" + id);
        double latitude1 = marker.getPosition().latitude;
        double longitude1 = marker.getPosition().longitude;
        //当拖拽完成后，需要把原来存储的坐标给替换掉
        latlngs.remove(id);
        latlngs.put(id, new LatLng(latitude1, longitude1));

        Toast.makeText(PaintFenceActivity.this, "拖拽结束，新位置：" + latitude1 + ", " + longitude1, Toast.LENGTH_LONG).show();

        Log.e("aaa", ids.size() + "---拖拽结束后map 的 " + latlngs.size());
       /* for (int i = 0; i < ids.size(); i++) {
            String s = ids.get(i);
            Log.e("aaa", "key= " + s + " and value= " + latlngs.get(s).toString());
        }*/
        //当拖拽完成后，重新画线
        drawLine();
    }

    @Override
    public boolean onMapPoiClick(MapPoi mapPoi) {
        return false;
    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }


    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    /**
     * 如果有大于两个点，就画多边形
     */
    private void drawPolygon() {
        if (polygon != null) {
            polygon.remove();
        }
        LatLng ll = null;
        List<LatLng> pts = new ArrayList<LatLng>();
        for (int i = 0; i < ids.size(); i++) {
            String s = ids.get(i);
            Log.e("aaa", "key= " + s + " and value= " + latlngs.get(s).toString());
            ll = latlngs.get(s);
            pts.add(ll);
        }
        OverlayOptions ooPolygon = new PolygonOptions().points(pts)
                .stroke(new Stroke(5, 0xAA00FF00)).fillColor(0xAAFFFF00);
        polygon = (Polygon) baidumap.addOverlay(ooPolygon);
    }

    /**
     * 如果此时有两个点，就画线
     */
    private void drawLine() {
        if (mPolyline != null) {
            mPolyline.remove();
        }
        List<LatLng> points = new ArrayList<LatLng>();
        LatLng l = null;
        for (int i = 0; i < ids.size(); i++) {
            l = latlngs.get(ids.get(i));
            points.add(l);
        }
        OverlayOptions ooPolyline = new PolylineOptions().width(10)
                .color(0xAAFF0000).points(points);
        mPolyline = (Polyline) baidumap.addOverlay(ooPolyline);
    }

    /**
     * 根据坐标来添加marker
     *
     * @param latitude
     * @param longitude
     */
    private void addMarler(double latitude, double longitude) {
        //定义Maker坐标点
        LatLng point = new LatLng(latitude, longitude);
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.point);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap)
                //.zIndex(9)
                .draggable(true);
        //在地图上添加Marker，并显示
        marker = (Marker) baidumap.addOverlay(option);
        markers.add(marker);
        String id = marker.getId();
        latlngs.put(id, new LatLng(latitude, longitude));
        ids.add(id);
    }

    private void initLocation() {
        //定位客户端的设置
        mLocationClient = new LocationClient(this);
        mLocationListener = new MyLocationListener();
        //注册监听
        mLocationClient.registerLocationListener(mLocationListener);
        //配置定位
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
        option.setScanSpan(1000);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setIsNeedLocationDescribe(true);//可选，设置是否需要地址描述
        option.setNeedDeviceDirect(false);//可选，设置是否需要设备方向结果
        option.setLocationNotify(false);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setIsNeedAltitude(false);//可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用
        mLocationClient.setLocOption(option);
    }


    private class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            Log.e("aaa", "位置：" + location.getLongitude());
            //将获取的location信息给百度map
            MyLocationData data = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(location.getDirection())
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude())
                    .build();
            baidumap.setMyLocationData(data);
            if (isFirstLocation) {

                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(15.0f);
                baidumap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

                isFirstLocation = false;
                showInfo("位置：" + location.getAddrStr());
                LatLng center = new LatLng(Double.parseDouble(SPHelper.getInstance(PaintFenceActivity.this).getLatitude()),
                        Double.parseDouble(SPHelper.getInstance(PaintFenceActivity.this).getLongitude()));   //①
                // 定义地图状态
                MapStatus mMapStatus = new MapStatus.Builder().target(center).zoom(16).build();  //②
                // 定义地图状态将要发生的变化
                MapStatusUpdate mMapStatusUpdate =
                        MapStatusUpdateFactory.newMapStatus(mMapStatus);   //③
                // BaiduMap对象改变地图状态
                baidumap.setMapStatus(mMapStatusUpdate);    //④
                //MapView要改变-->用BaiduMap去操作-->根据MapStatus的状态改变-->
                //  BaiduMap看不懂状态MapStatus  需要MapStatusUpdate去描述-->BaiduMap通过描述去改变-->MapView改变
                //定义Maker坐标点
                LatLng point = new LatLng(Double.parseDouble(SPHelper.getInstance(PaintFenceActivity.this).getLatitude()),
                        Double.parseDouble(SPHelper.getInstance(PaintFenceActivity.this).getLongitude()));
                //构建Marker图标
                BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.ic_user_location);
                // 构建MarkerOption， 用于在地图上添加Marker
                OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
                // 在地图上添加Marker，并显示
                baidumap.addOverlay(option);
            }
        }

    }


    //显示消息
    private void showInfo(String str) {
        Toast.makeText(PaintFenceActivity.this, str, Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onStart() {
        super.onStart();
        //开启定位
       /* baidumap.setMyLocationEnabled(true);
        if (!mLocationClient.isStarted()) {
            mLocationClient.start();
        }*/
    }

    @Override
    protected void onStop() {
        super.onStop();
        //关闭定位
       /* baidumap.setMyLocationEnabled(false);
        if (mLocationClient.isStarted()) {
            mLocationClient.stop();
        }*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 关闭定位图层
        baidumap.setMyLocationEnabled(false);
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        map.onDestroy();
        latlngs.clear();
        ids.clear();
        //polygons.clear();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        map.onResume();
    }

    @Override
    protected boolean setIsFull() {
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        map.onPause();
    }


    private void initView() {
        location = (ImageView) findViewById(R.id.location);
        imgFenceEdit = (ImageView) findViewById(R.id.img_fence_edit);
        imgFenceClear = (ImageView) findViewById(R.id.img_clear_fence);
        imgDrawFence = (ImageView) findViewById(R.id.img_draw_fence);
        tvRight = (TextView) findViewById(R.id.tv_right);
        tvRight.setOnClickListener(PaintFenceActivity.this);
        imgFenceClear.setOnClickListener(PaintFenceActivity.this);
        location.setOnClickListener(PaintFenceActivity.this);
        map = (MapView) findViewById(R.id.map);
        baidumap = map.getMap();
        //给marker设置点击事件，用来删除marker
        baidumap.setOnMarkerClickListener(this);
        //给map设置监听事件，用来拿到点击地图的点的坐标
        baidumap.setOnMapClickListener(this);
        //给marker设置拖拽监听事件，用来获取拖拽完成后的坐标
        baidumap.setOnMarkerDragListener(this);
        btnCheck = (Button) findViewById(R.id.btn_check);
        btnCheck.setOnClickListener(PaintFenceActivity.this);
    }

    /**
     * 获取手机IMEI号
     */
    public static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();
        return imei;
    }

    @OnClick(R.id.rl_back)
    public void onClick() {
        finish();
    }

}
