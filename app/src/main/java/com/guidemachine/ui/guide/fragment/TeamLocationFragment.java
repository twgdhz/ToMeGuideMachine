package com.guidemachine.ui.guide.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.guidemachine.R;
import com.guidemachine.base.ui.BaseFragment;
import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.TeamLocationBean;
import com.guidemachine.service.presenter.GuideTeamLocationPresenter;
import com.guidemachine.service.view.GuideTeamLocationView;
import com.guidemachine.ui.guide.GuideAlarmMessageActivity;
import com.guidemachine.ui.guide.GuideFenceListActivity;
import com.guidemachine.ui.guide.ReleaseTourJourneyActivity;
import com.guidemachine.ui.guide.SelectedFenceActivity;
import com.guidemachine.util.IntentUtils;
import com.guidemachine.util.Logger;
import com.guidemachine.util.ToastUtils;
import com.guidemachine.util.overlayutil.OverlayManager;
import com.guidemachine.util.share.SPHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * @author ChenLinWang
 * @email 422828518@qq.com
 * @create 2018/12/12 0012 9:45
 * description: 导游全团定位
 */
public class TeamLocationFragment extends BaseFragment implements View.OnClickListener {


    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_title_center)
    TextView tvTitleCenter;
    @BindView(R.id.rl_right)
    RelativeLayout rlRight;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.tv_online_device)
    TextView tvOnlineDevice;
    @BindView(R.id.view_online_device)
    View viewOnlineDevice;
    @BindView(R.id.ll_online_device)
    LinearLayout llOnlineDevice;
    @BindView(R.id.tv_offline_device)
    TextView tvOfflineDevice;
    @BindView(R.id.view_offline_device)
    View viewOfflineDevice;
    @BindView(R.id.ll_offline_device)
    LinearLayout llOfflineDevice;
    @BindView(R.id.tv_all_device)
    TextView tvAllDevice;
    @BindView(R.id.view_all_device)
    View viewAllDevice;
    @BindView(R.id.ll_all_device)
    LinearLayout llAllDevice;
    Unbinder unbinder;
    LinearLayout[] topTitleLinearLyaouts;
    TextView[] topTitleTextView;
    View[] topTitleViews;
    @BindView(R.id.map_guide)
    MapView mapGuide;
    @BindView(R.id.img_fence)
    ImageView imgFence;
    @BindView(R.id.img_refresh)
    ImageView imgRefresh;
    @BindView(R.id.img_location)
    ImageView imgLocation;
    @BindView(R.id.img_refresh_time)
    TextView imgRefreshTime;
    private BaiduMap baiduMap;
    GuideTeamLocationPresenter guideTeamLocationPresenter;

    //旅游团信息实体类
    TeamLocationBean teamLocationBean;
    //设备标志位 1：在线 0：离线 2：总设备
    public int deviceType = 1;

    @Override
    protected int setRootViewId() {
        return R.layout.fragment_team_location;
    }

    @Override
    protected void initView(View view, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        llAllDevice.setOnClickListener(this);
        llOfflineDevice.setOnClickListener(this);
        llOnlineDevice.setOnClickListener(this);
        rlRight.setOnClickListener(this);
        rlBack.setOnClickListener(this);
        imgFence.setOnClickListener(this);
        imgRefresh.setOnClickListener(this);
        imgLocation.setOnClickListener(this);
        topTitleLinearLyaouts = new LinearLayout[]{llOnlineDevice, llOfflineDevice, llAllDevice};
        topTitleTextView = new TextView[]{tvOnlineDevice, tvOfflineDevice, tvAllDevice};
        topTitleViews = new View[]{viewOnlineDevice, viewOfflineDevice, viewAllDevice};
        baiduMap = mapGuide.getMap();
        // 不显示地图缩放控件（按钮控制栏）
        mapGuide.showZoomControls(false);
        //更改指南针位置
        baiduMap.setCompassPosition(new Point(600, 80));
        guideTeamLocationPresenter = new GuideTeamLocationPresenter(getContext());
        guideTeamLocationPresenter.onCreate();
        guideTeamLocationPresenter.attachView(guideTeamLocationView);
        getDeviceList();

        //marker点击
        baiduMap.setOnMarkerClickListener(new OverlayManager(baiduMap) {
            @Override
            public List<OverlayOptions> getOverlayOptions() {
                return null;
            }

            @Override
            public boolean onMarkerClick(final Marker marker) {

                LayoutInflater inflater = LayoutInflater.from(getContext());
                View view = null;
                final TeamLocationBean.TerminalInfoListBean terminalInfoBean = (TeamLocationBean.TerminalInfoListBean) marker.getExtraInfo().get("TerminalInfoBean");
                final TeamLocationBean.CoordinateListBean coordinateListBean = (TeamLocationBean.CoordinateListBean) marker.getExtraInfo().get("CoordinateListBean");
                view = inflater.inflate(R.layout.device_infowindow, null);
                TextView tvDeviceCode = view.findViewById(R.id.tv_device_code);
                TextView tvDeviceStatus = view.findViewById(R.id.tv_device_status);
                TextView tvBattery = view.findViewById(R.id.tv_battery);
                TextView tvOnlineTime = view.findViewById(R.id.tv_online_time);
                TextView tvLocationTiem = view.findViewById(R.id.tv_location_time);
                TextView tvAddress = view.findViewById(R.id.tv_address);
                ImageView imgDial = view.findViewById(R.id.img_dial);
                ImageView imgMessage = view.findViewById(R.id.img_message);
                tvDeviceCode.setText(terminalInfoBean.getCodeMachine());
                if (terminalInfoBean.getIsOnline() == 1) {
                    tvDeviceStatus.setText("(在线)");
                } else if (terminalInfoBean.getIsOnline() == 0) {
                    tvDeviceStatus.setText("(离线)");
                } else {
                    tvDeviceStatus.setText("(未知)");
                }
                tvBattery.setText(terminalInfoBean.getBattery());
                tvOnlineTime.setText(terminalInfoBean.getOnLineTime());
                tvLocationTiem.setText(terminalInfoBean.getPositionTime());
                tvAddress.setText(terminalInfoBean.getPositionAddress());
                imgDial.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        callPhone(terminalInfoBean.getTelephone());
//                        callPhone("15828472427");
                    }
                });
                imgMessage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        IntentUtils.openActivity(getContext(), ReleaseTourJourneyActivity.class, "imei", terminalInfoBean.getImei());
                    }
                });
                //定义用于显示该InfoWindow的坐标点
                LatLng pt = new LatLng(coordinateListBean.getLat(), coordinateListBean.getLon());
                //创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
                InfoWindow mInfoWindow = new InfoWindow(view, pt, -50);
                //显示InfoWindow
                baiduMap.showInfoWindow(mInfoWindow);
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_online_device://选择在线
                deviceType = 1;
                setSelect(0);
                baiduMap.clear();
                if (teamLocationBean != null) {
                    for (int i = 0; i < teamLocationBean.getCoordinateList().size(); i++) {
                        if (teamLocationBean.getTerminalInfoList().get(i).getIsOnline() == 1) {
                            final LatLng latLng = new LatLng(teamLocationBean.getCoordinateList().get(i).getLat(),
                                    teamLocationBean.getCoordinateList().get(i).getLon());
                            final Bundle bundle = new Bundle();
                            bundle.putSerializable("TerminalInfoBean", teamLocationBean.getTerminalInfoList().get(i));
                            bundle.putSerializable("CoordinateListBean", teamLocationBean.getCoordinateList().get(i));
                            setMark(latLng, teamLocationBean.getTerminalInfoList().get(i).getIsOnline(), deviceType).setExtraInfo(bundle);
                        }
                    }
                }

                break;
            case R.id.ll_offline_device://选择离线
                deviceType = 0;
                setSelect(1);
                baiduMap.clear();
                if (teamLocationBean != null) {
                    for (int i = 0; i < teamLocationBean.getCoordinateList().size(); i++) {
                        if (teamLocationBean.getTerminalInfoList().get(i).getIsOnline() == 0) {
                            final LatLng latLng = new LatLng(teamLocationBean.getCoordinateList().get(i).getLat(),
                                    teamLocationBean.getCoordinateList().get(i).getLon());
                            final Bundle bundle = new Bundle();
                            bundle.putSerializable("TerminalInfoBean", teamLocationBean.getTerminalInfoList().get(i));
                            bundle.putSerializable("CoordinateListBean", teamLocationBean.getCoordinateList().get(i));
                            setMark(latLng, teamLocationBean.getTerminalInfoList().get(i).getIsOnline(), deviceType).setExtraInfo(bundle);
                        }
                    }
                }
                break;
            case R.id.ll_all_device://选择总设备
                deviceType = 2;
                setSelect(2);
                baiduMap.clear();
                if (teamLocationBean != null) {
                    for (int i = 0; i < teamLocationBean.getCoordinateList().size(); i++) {
                        final LatLng latLng = new LatLng(teamLocationBean.getCoordinateList().get(i).getLat(),
                                teamLocationBean.getCoordinateList().get(i).getLon());
                        final Bundle bundle = new Bundle();
                        bundle.putSerializable("TerminalInfoBean", teamLocationBean.getTerminalInfoList().get(i));
                        bundle.putSerializable("CoordinateListBean", teamLocationBean.getCoordinateList().get(i));
                        setMark(latLng, teamLocationBean.getTerminalInfoList().get(i).getIsOnline(), deviceType).setExtraInfo(bundle);
                    }
                }
                break;
            case R.id.rl_right:
                IntentUtils.openActivity(getContext(), GuideAlarmMessageActivity.class);
                break;
            case R.id.img_fence:
                IntentUtils.openActivity(getContext(), SelectedFenceActivity.class);
                break;
            case R.id.img_refresh:
//                IntentUtils.openActivity(getContext(), GuideAlarmMessageActivity.class);
                getDeviceList();
                break;
            case R.id.img_location:
                baiduMap.clear();
                //定义Maker坐标点
                LatLng center = new LatLng(Double.parseDouble(SPHelper.getInstance(getContext()).getLatitude()),
                        Double.parseDouble(SPHelper.getInstance(getContext()).getLongitude()));   //①
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
                LatLng point = new LatLng(Double.parseDouble(SPHelper.getInstance(getContext()).getLatitude()),
                        Double.parseDouble(SPHelper.getInstance(getContext()).getLongitude()));
                //构建Marker图标
                BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.ic_user_location);
                // 构建MarkerOption， 用于在地图上添加Marker
                OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
                // 在地图上添加Marker，并显示
                baiduMap.addOverlay(option);
                break;
            case R.id.rl_back:
                getActivity().finish();
                break;
        }
    }

    public void getDeviceList() {
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("sceneryId", SPHelper.getInstance(getContext()).getSceneryId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestData.toString());
        guideTeamLocationPresenter.selectHomeData(requestBody);
        showProgressDialog();
    }

    GuideTeamLocationView guideTeamLocationView = new GuideTeamLocationView() {
        @Override
        public void onSuccess(BaseBean<TeamLocationBean> bean) {
            teamLocationBean = bean.getValue();
            dismissProgressDialog();
            Logger.d("TeamLocationBean", bean.toString());
            for (int i = 0; i < bean.getValue().getCoordinateList().size(); i++) {
                if (deviceType == 1 && bean.getValue().getTerminalInfoList().get(i).getIsOnline() == 1) {
                    final LatLng latLng = new LatLng(bean.getValue().getCoordinateList().get(i).getLat(), bean.getValue().getCoordinateList().get(i).getLon());
                    final Bundle bundle = new Bundle();
                    bundle.putSerializable("TerminalInfoBean", bean.getValue().getTerminalInfoList().get(i));
                    bundle.putSerializable("CoordinateListBean", bean.getValue().getCoordinateList().get(i));
                    setMark(latLng, bean.getValue().getTerminalInfoList().get(i).getIsOnline(), deviceType).setExtraInfo(bundle);
                }

            }
        }

        @Override
        public void onError(String result) {
            dismissProgressDialog();
            ToastUtils.msg(result.toString());
        }
    };
    List<OverlayOptions> overlayOptionsList = new ArrayList<>();
    List<Overlay> overlays = new ArrayList<>();

    /**
     * 添加marker
     */
    public Marker setMark(final LatLng latLng, int isOnline, int deviceType) {

        baiduMap.hideInfoWindow();
        Log.v("pcw", "setMarker : lat : " + latLng.latitude + " lon : " + latLng.longitude);
        Bitmap bitmap = null;

        if (isOnline == 1) {//在线
            bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_guide_online_marker);
        } else if (isOnline == 0) {//离线
            bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_guide_offline_marker);
        }

//        else if (isOnline == 4) {
//            bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_toilet);
////            bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_goods_location);
//        }
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
        LatLng point = new LatLng(Double.parseDouble(SPHelper.getInstance(getContext()).getLatitude()),
                Double.parseDouble(SPHelper.getInstance(getContext()).getLongitude()));
        //构建Marker图标
        BitmapDescriptor userBitmap = BitmapDescriptorFactory.fromResource(R.mipmap.ic_user_location);
        // 构建MarkerOption， 用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions().position(point).icon(userBitmap);
        // 在地图上添加Marker，并显示
        baiduMap.addOverlay(option);

        return (Marker) overlay;
    }

    public void setSelect(int position) {//设置被选中的状态
        for (int i = 0; i < topTitleLinearLyaouts.length; i++) {
            if (i == position) {
                topTitleTextView[i].setTextColor(getResources().getColor(R.color.title_tour_color));
                topTitleViews[i].setVisibility(View.VISIBLE);

            } else {
                topTitleTextView[i].setTextColor(getResources().getColor(R.color.text_color51));
                topTitleViews[i].setVisibility(View.INVISIBLE);
            }
        }
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        guideTeamLocationPresenter.onStop();
    }

    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param phoneNum 电话号码
     */
//    public void callPhone(String phoneNum) {
//        Intent intent = new Intent(Intent.ACTION_CALL);
//        Uri data = Uri.parse("tel:" + phoneNum);
//        intent.setData(data);
//        startActivity(intent);
//
//    }
    /**
     * 拨打电话（直接拨打电话）
     *
     * @param phoneNum 电话号码
     */
    public void callPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }
}