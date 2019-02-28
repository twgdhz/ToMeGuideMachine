package com.guidemachine.ui.guide;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.guidemachine.R;
import com.guidemachine.base.ui.BaseActivity;
import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.FenceBean;
import com.guidemachine.service.presenter.FencePresenter;
import com.guidemachine.service.view.FenceView;
import com.guidemachine.util.IntentUtils;
import com.guidemachine.util.Logger;
import com.guidemachine.util.MobileInfoUtil;
import com.guidemachine.util.StatusBarUtils;
import com.guidemachine.util.ToastUtils;
import com.guidemachine.util.share.SPHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * @author ChenLinWang
 * @email 422828518@qq.com
 * @create 2019/1/16 0016 15:17
 * description: 当前已选电子围栏
 */
public class SelectedFenceActivity extends BaseActivity {

    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_title_center)
    TextView tvTitleCenter;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.ll_modify_fence)
    LinearLayout llModifyFence;
    @BindView(R.id.map)
    MapView map;
    @BindView(R.id.tv_fence_name)
    TextView tvFenceName;
    private BaiduMap baidumap;
    double lat;
    double lon;
    //通过imei查询已绑定的电子围栏；
    FencePresenter fencePresenter;
    List<String> locationFenceList = new ArrayList<>();

    @Override
    protected int setRootViewId() {
        return R.layout.activity_selected_fence;
    }


    @Override
    protected boolean setIsFull() {
        return false;
    }

    @Override
    protected void InitialView() {
        StatusBarUtils.setWindowStatusBarColor(SelectedFenceActivity.this, R.color.white);
        baidumap = map.getMap();
        baidumap.clear();
        location();

        llModifyFence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.openActivity(SelectedFenceActivity.this, GuideFenceListActivity.class);
            }
        });
        lat = Double.parseDouble(SPHelper.getInstance(SelectedFenceActivity.this).getLatitude());
        lon = Double.parseDouble(SPHelper.getInstance(SelectedFenceActivity.this).getLongitude());
        fencePresenter = new FencePresenter(SelectedFenceActivity.this);
        fencePresenter.onCreate();
        fencePresenter.attachView(fenceView);
        JSONObject requestData = new JSONObject();
        try {
//         requestData.put("sceneryId ", SPHelper.getInstance(PaintFenceActivity.this).getSceneryId());
            requestData.put("imei", MobileInfoUtil.getIMEI(SelectedFenceActivity.this));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestData.toString());
        fencePresenter.getFence(requestBody);
        showProgressDialog();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        baidumap.clear();
        location();
        JSONObject requestData = new JSONObject();
        try {
//         requestData.put("sceneryId ", SPHelper.getInstance(PaintFenceActivity.this).getSceneryId());
            requestData.put("imei", MobileInfoUtil.getIMEI(SelectedFenceActivity.this));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestData.toString());
        fencePresenter.getFence(requestBody);
        showProgressDialog();
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        location();
//    }

    Polyline mPolyline;
    List<LatLng> points = new ArrayList<LatLng>();
    FenceView fenceView = new FenceView() {
        @Override
        public void onSuccess(BaseBean<List<FenceBean>> mFenceBean) {
            dismissProgressDialog();
            if (mFenceBean.getValue().size()==0){
                return;
            }
//            ToastUtils.msg(mFenceBean.getValue().toString());
            locationFenceList.clear();
            baidumap.clear();
            points.clear();
            Logger.d("FenceBean 请求成功→"+mFenceBean.getValue().toString());

            for (FenceBean i : mFenceBean.getValue()) {
                locationFenceList.add(i.getCoordinate());
            }
            tvFenceName.setText( mFenceBean.getValue().get(0).getName());
            //构建折线点坐标
            String[] splitstr = locationFenceList.get(0).split(",");
            for (String res : splitstr) {
                Logger.d("电子围栏", res);
//                locationFenceList1.add(res);
                String[] strings = res.split("_");
                points.add(new LatLng(Double.parseDouble(strings[1]), Double.parseDouble(strings[0])));
            }
            points.add(new LatLng(Double.parseDouble(splitstr[0].split("_")[1]), Double.parseDouble(splitstr[0].split("_")[0])));
            //绘制折线
            OverlayOptions ooPolyline = new PolylineOptions()
                    .width(10)
                    .color(Color.parseColor("#f82328")).points(points);
            mPolyline = (Polyline) baidumap.addOverlay(ooPolyline);
        }

        @Override
        public void onError(String result) {
            dismissProgressDialog();
            ToastUtils.msg(result);
        }
    };

    public void location() {
//        LatLng center = new LatLng(lat, lon);   //①
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
        LatLng point = new LatLng(lat, lon);
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.ic_user_location);
        // 构建MarkerOption， 用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions().position(center).icon(bitmap);
        // 在地图上添加Marker，并显示
        baidumap.addOverlay(option);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.rl_back)
    public void onClick() {
        finish();
    }

}
