package com.guidemachine.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.baidu.mapapi.model.LatLng;
import com.guidemachine.MainActivity;
import com.guidemachine.R;
import com.guidemachine.base.ui.BaseActivity;
import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.PersonCenterBean;
import com.guidemachine.service.entity.VisitHistoryBean;
import com.guidemachine.service.presenter.PersonCenterPresenter;
import com.guidemachine.service.presenter.ShopGoodListPresenter;
import com.guidemachine.service.presenter.VisitHistsoryPresenter;
import com.guidemachine.service.view.PersonCenterView;
import com.guidemachine.service.view.VisitHistoryView;
import com.guidemachine.ui.activity.order.OrdersActivity;
import com.guidemachine.ui.adapter.GridViewAdapter;
import com.guidemachine.ui.guide.GuideMainActivity;
import com.guidemachine.ui.guide.login.GuideLoginActivity;
import com.guidemachine.ui.superadmin.SuperAdminMainActivity;
import com.guidemachine.util.IntentUtils;
import com.guidemachine.util.IsLoginUtils;
import com.guidemachine.util.L;
import com.guidemachine.util.StatusBarUtils;
import com.guidemachine.util.ToastUtils;
import com.guidemachine.util.share.SPHelper;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * @author ChenLinWang
 * @email 422828518@qq.com
 * @create 2018/11/16 0016 15:27
 * description: https://blog.csdn.net/sinat_29924199/article/details/53019128
 * 个人中心
 */
public class PersonCenterActivity extends BaseActivity {

    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.gv_tour_history)
    GridView gvTourHistory;
    @BindView(R.id.mapview)
    MapView mapView;
    @BindView(R.id.tv_check_order)
    LinearLayout tvCheckOrder;
    @BindView(R.id.ll_main)
    LinearLayout llMain;
    @BindView(R.id.tv_go_tour)
    LinearLayout tvGoTour;
    private List<String> mDatas;
    private GridView mGridView;
    private GridViewAdapter adapter;
    private BaiduMap baiduMap;
    //景点访问记录
    VisitHistsoryPresenter visitHistsoryPresenter;
    private PersonCenterPresenter personCenterPresenter;
    private List<LocalMedia> selectList = new ArrayList();
    private File mFile;

    @Override
    protected int setRootViewId() {
        return R.layout.activity_person_center;
    }

    @Override
    protected boolean setIsFull() {
        return false;
    }

    @Override
    protected void InitialView() {
        StatusBarUtils.setWindowStatusBarColor(PersonCenterActivity.this, R.color.text_color4);
        // 不显示地图缩放控件（按钮控制栏）
        mapView.showZoomControls(false);
        baiduMap = mapView.getMap();
        mGridView = (GridView) findViewById(R.id.gv_tour_history);

        LatLng cenpt = new LatLng(Double.parseDouble(SPHelper.getInstance(PersonCenterActivity.this).getLatitude()),
                Double.parseDouble(SPHelper.getInstance(PersonCenterActivity.this).getLongitude()));   //①
        // 定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder().target(cenpt).zoom(18).build();  //②
        // 定义地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate =
                MapStatusUpdateFactory.newMapStatus(mMapStatus);   //③
        // BaiduMap对象改变地图状态
        baiduMap.setMapStatus(mMapStatusUpdate);    //④
        //定义Maker坐标点
        LatLng point = new LatLng(Double.parseDouble(SPHelper.getInstance(PersonCenterActivity.this).getLatitude()),
                Double.parseDouble(SPHelper.getInstance(PersonCenterActivity.this).getLongitude()));
//                    LatLng point = new LatLng(30.758348, 103.98694);
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.ic_person_location);
        // 构建MarkerOption， 用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
        // 在地图上添加Marker，并显示
        baiduMap.addOverlay(option);
        tvCheckOrder.setOnClickListener(new View.OnClickListener() {//查看订单
            @Override
            public void onClick(View view) {
                if (IsLoginUtils.getInstence().isLogin(PersonCenterActivity.this)==true){
                    IntentUtils.openActivity(PersonCenterActivity.this, OrdersActivity.class);
                }else {
                    IntentUtils.openActivity(PersonCenterActivity.this, LoginActivity.class,"type","1");
                    finish();
                }

            }
        });
        llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.openActivity(PersonCenterActivity.this, MainActivity.class);
            }
        });
        visitHistsoryPresenter = new VisitHistsoryPresenter(PersonCenterActivity.this);
        visitHistsoryPresenter.onCreate();
        visitHistsoryPresenter.attachView(visitHistoryView);
        JSONObject loginRequestData = new JSONObject();
        try {
            loginRequestData.put("imei", "956680617111243");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody loginRequestBody = RequestBody.create(MediaType.parse("application/json"), loginRequestData.toString());
//        visitHistsoryPresenter.getVisitHistory(loginRequestBody);
        tvGoTour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                IntentUtils.openActivity(PersonCenterActivity.this, SuperAdminMainActivity.class);
                IntentUtils.openActivity(PersonCenterActivity.this, GuideLoginActivity.class);
            }
        });

        personCenterPresenter = new PersonCenterPresenter(PersonCenterActivity.this);
        personCenterPresenter.onCreate();
        personCenterPresenter.attachView(personCenterView);
    }

    VisitHistoryView visitHistoryView = new VisitHistoryView() {
        @Override
        public void onSuccess(BaseBean<List<VisitHistoryBean>> mVisitHistoryBean) {
            initDatas(mVisitHistoryBean.getValue());
        }

        @Override
        public void onError(String result) {
            ToastUtils.msg(result);
        }
    };

    private void initDatas(List<VisitHistoryBean> list) {
        mDatas = new ArrayList<>();
        String sourceStr = list.get(0).getImageUrl();
        String[] sourceStrArray = sourceStr.split(",");
        for (int i = 0; i < sourceStrArray.length; i++) {
            System.out.println(sourceStrArray[i]);
            mDatas.add(sourceStrArray[i]);
        }

        adapter = new GridViewAdapter(PersonCenterActivity.this, mDatas);
        mGridView.setAdapter(adapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == parent.getChildCount() - 1) {
//                    mDatas.add(R.mipmap.ic_person_history_last_bg);
                    Toast.makeText(PersonCenterActivity.this, "您点击了添加", Toast.LENGTH_SHORT).show();
//                    adapter = new GridViewAdapter(PersonCenterActivity.this, mDatas);
//                    mGridView.setAdapter(adapter);
//                    adapter.notifyDataSetChanged();
                }

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        visitHistsoryPresenter.onStop();
    }

    @OnClick(R.id.rl_back)
    public void onClick() {
        finish();
    }

    @OnClick(R.id.person_head_image)
    public void onHeadClick(){
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
//                    .theme()
//                    .maxSelectNum(1)
//                    .minSelectNum(1)
                .selectionMode(PictureConfig.SINGLE)
                .previewImage(true)
                .isCamera(true)
                .enableCrop(true)
                .compress(true)
                .imageFormat(PictureMimeType.PNG)
                .cropCompressQuality(80)// 裁剪压缩质量 默认90 int
                .minimumCompressSize(200)// 小于200kb的图片不压缩
                .glideOverride(160, 160)
                .previewEggs(true)
                .openClickSound(false)
                .freeStyleCropEnabled(true)
                .circleDimmedLayer(false)
                .showCropFrame(false)
                .showCropGrid(false)
                .selectionMedia(selectList)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    L.gi().d("获取到路径==================");
//                    selectList = PictureSelector.obtainMultipleResult(data);
//                    L.gi().i(selectList.size()+"=="+selectList.toString());
//                    if (!selectList.isEmpty()) {
//                        mFile = new File(selectList.get(0).getCompressPath());
//                        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), mFile);
//                        MultipartBody.Part part = MultipartBody.Part.createFormData("file", mFile.getName(), requestFile);
//                        L.gi().d("文件路径:"+mFile.getPath());
//                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private PersonCenterView personCenterView = new PersonCenterView() {
        @Override
        public void onSuccess(BaseBean<PersonCenterBean> bean) {

        }

        @Override
        public void onError(String result) {

        }
    };
}
