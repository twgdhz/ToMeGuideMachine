package com.guidemachine.ui.guide;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.guidemachine.R;
import com.guidemachine.base.ui.BaseActivity;
import com.guidemachine.service.entity.FenceBean;
import com.guidemachine.util.Logger;
import com.guidemachine.util.StatusBarUtils;

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
 * @create 2019/1/17 0017 10:04
 * description: 查看围栏
 */
public class CheckFenceDetailActivity extends BaseActivity {

    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_title_center)
    TextView tvTitleCenter;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.map)
    MapView map;
    //用来存储一个点所在的所有的区域
    List<String> areas = new ArrayList<>();
    private String fenceType;
    private String scenerySpot;
    List<LatLng> pts = new ArrayList<LatLng>();
    BaiduMap baiduMap;
    Polyline mPolyline;
    List<LatLng> points = new ArrayList<LatLng>();
    List<String> locationFenceList = new ArrayList<>();
    @Override
    protected int setRootViewId() {
        return R.layout.activity_check_fence_detail;
    }

    @Override
    protected boolean setIsFull() {
        return false;
    }

    @Override
    protected void InitialView() {
        StatusBarUtils.setWindowStatusBarColor(CheckFenceDetailActivity.this, R.color.white);
        FenceBean fenceBean = (FenceBean) getIntent().getSerializableExtra("fence");
        baiduMap=map.getMap();
        location();
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View inflate = View.inflate(this, R.layout.dialog_check_fence, null);
        final EditText edt_alias = inflate.findViewById(R.id.et_fence_name);
        final EditText spFenceType = inflate.findViewById(R.id.sp_fence_type);//围栏类型
        final EditText spFenceSpot = inflate.findViewById(R.id.sp_scenery_spot);//所属景点
        final ImageView imgClose = inflate.findViewById(R.id.img_close);//关闭
        final EditText etInFence = inflate.findViewById(R.id.et_in_fence);
        final EditText etOutFence = inflate.findViewById(R.id.et_out_fence);
        edt_alias.setText(fenceBean.getName());
        if (fenceBean.getType() == 0) {//电子围栏类型 报警方式(出围栏报警 = 0,进围栏报警 = 1,进出围栏报警 = 2
            spFenceType.setText("出围栏报警");
        } else if (fenceBean.getType() == 1) {
            spFenceType.setText("进围栏报警");
        } else if (fenceBean.getType() == 2) {
            spFenceType.setText("进出围栏报警");
        }
        spFenceSpot.setText(fenceBean.getScenerySpotName());
        etInFence.setText(fenceBean.getInHintWord());
        etOutFence.setText(fenceBean.getOutHintWord());
        builder.setView(inflate);
//        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                String trim = edt_alias.getText().toString().trim();
//                if (trim.equals("")) {
//                    Toast.makeText(CheckFenceDetailActivity.this, "名不能为空！", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//            }
//        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.create().dismiss();
            }
        });

            locationFenceList.add(fenceBean.getCoordinate());
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
        mPolyline = (Polyline) baiduMap.addOverlay(ooPolyline);
    }

    public void location() {
//        LatLng center = new LatLng(lat, lon);   //①
        LatLng center = new LatLng(30.911094, 103.573897);   //①
        // 定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder().target(center).zoom(18).build();  //②
        // 定义地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate =
                MapStatusUpdateFactory.newMapStatus(mMapStatus);   //③
        // BaiduMap对象改变地图状态
        baiduMap.setMapStatus(mMapStatusUpdate);    //④
        //MapView要改变-->用BaiduMap去操作-->根据MapStatus的状态改变-->
        //  BaiduMap看不懂状态MapStatus  需要MapStatusUpdate去描述-->BaiduMap通过描述去改变-->MapView改变
        //定义Maker坐标点
//        LatLng point = new LatLng(lat, lon);
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.ic_user_location);
        // 构建MarkerOption， 用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions().position(center).icon(bitmap);
        // 在地图上添加Marker，并显示
        baiduMap.addOverlay(option);
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
