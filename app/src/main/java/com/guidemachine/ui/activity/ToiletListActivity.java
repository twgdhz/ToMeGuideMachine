package com.guidemachine.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.walknavi.WalkNavigateHelper;
import com.baidu.mapapi.walknavi.adapter.IWEngineInitListener;
import com.baidu.mapapi.walknavi.adapter.IWRoutePlanListener;
import com.baidu.mapapi.walknavi.model.WalkRoutePlanError;
import com.baidu.mapapi.walknavi.params.WalkNaviLaunchParam;
import com.guidemachine.R;
import com.guidemachine.base.adapter.BaseRecyclerAdapter;
import com.guidemachine.base.adapter.BaseViewHolder;
import com.guidemachine.base.ui.BaseActivity;
import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.ScenerySpotListBean;
import com.guidemachine.service.presenter.ScenerySpotPresenter;
import com.guidemachine.ui.activity.walknavi.WNaviGuideActivity;
import com.guidemachine.util.StatusBarUtils;
import com.guidemachine.util.VoiceUtil;
import com.guidemachine.util.share.SPHelper;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author ChenLinWang
 * @email 422828518@qq.com
 * @create 2018/11/13 0013 11:16
 * description: 厕所列表
 */
public class ToiletListActivity extends BaseActivity {
    double userLongitude;
    double userLatitude;
    //厕所
    ScenerySpotPresenter scenerySpotPresenter;
    BaseBean<ScenerySpotListBean> scenerySpotListBean;
    BaseRecyclerAdapter adapter;
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_title_center)
    TextView tvTitleCenter;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.ry_toilet_list)
    RecyclerView ryToiletList;
    //步行导航启动参数(引入esaeui下的jar包)
    WalkNaviLaunchParam walkParam;
    private LatLng startPt, endPt;
    @Override
    protected int setRootViewId() {
        return R.layout.activity_toilet_list;
    }


    @Override
    protected boolean setIsFull() {
        return false;
    }

    @Override
    protected void InitialView() {
        StatusBarUtils.setWindowStatusBarColor(ToiletListActivity.this, R.color.text_color4);
        userLongitude = Double.parseDouble(SPHelper.getInstance(ToiletListActivity.this).getLongitude());
        userLatitude = Double.parseDouble(SPHelper.getInstance(ToiletListActivity.this).getLatitude());
        scenerySpotListBean = (BaseBean<ScenerySpotListBean>) getIntent().getSerializableExtra("scenerySpotListBean");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ToiletListActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ryToiletList.setLayoutManager(linearLayoutManager);
        if (scenerySpotListBean == null) {
            //重新请求接口
        } else {
            adapter = new BaseRecyclerAdapter(ToiletListActivity.this, scenerySpotListBean.getValue().getList(), R.layout.item_toilet_list) {
                @Override
                protected void convert(BaseViewHolder helper, Object item, final int position) {
                    helper.setText(R.id.tv_toilet_name, scenerySpotListBean.getValue().getList().get(position).getName());
                    double sceneSpotLongitude = scenerySpotListBean.getValue().getList().get(position).getLng();
                    double sceneSpotLatitude = scenerySpotListBean.getValue().getList().get(position).getLat();
                    DecimalFormat df = new DecimalFormat("#.00");
                    double distance = getDistance(userLongitude, userLatitude, sceneSpotLongitude, sceneSpotLatitude);
                    if (distance>=1000){
                        String str = df.format(distance/1000);
                        helper.setText(R.id.tv_toilet_distance, str + "千米");
                    }else {
                        String str = df.format(distance);
                        helper.setText(R.id.tv_toilet_distance, str + "米");
                    }
                    helper.getView(R.id.rl_navi).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startPt = new LatLng(Double.parseDouble(SPHelper.getInstance(ToiletListActivity.this).getLatitude()),
                                    Double.parseDouble(SPHelper.getInstance(ToiletListActivity.this).getLongitude()));
                            endPt = new LatLng(30.612961,104.05325);
                            walkParam = new WalkNaviLaunchParam().stPt(startPt).endPt(endPt);
                            walkParam.extraNaviMode(0);
                            startWalkNavi();
                        }
                    });
                }
            };
        }
        ryToiletList.setAdapter(adapter);
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

    @OnClick(R.id.rl_back)
    public void onClick() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

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
                intent.setClass(ToiletListActivity.this, WNaviGuideActivity.class);
                startActivity(intent);
            }

            @Override
            public void onRoutePlanFail(WalkRoutePlanError error) {
                Log.d(TAG, "WalkNavi onRoutePlanFail" + error.name().toString());
            }

        });
    }

}
