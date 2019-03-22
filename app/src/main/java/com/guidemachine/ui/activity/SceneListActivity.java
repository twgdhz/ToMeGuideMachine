package com.guidemachine.ui.activity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
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
import com.guidemachine.event.MessageEventPlayComplete;
import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.ScenerySpotListBean;
import com.guidemachine.service.presenter.ScenerySpotPresenter;
import com.guidemachine.ui.activity.walknavi.WNaviGuideActivity;
import com.guidemachine.util.L;
import com.guidemachine.util.StatusBarUtils;
import com.guidemachine.util.ToastUtils;
import com.guidemachine.util.VoiceUtil;
import com.guidemachine.util.share.SPHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author ChenLinWang
 * @email 422828518@qq.com
 * @create 2018/11/13 0013 9:39
 * description: 景区列表
 */
public class SceneListActivity extends BaseActivity {
    //景点经纬度
    ScenerySpotPresenter scenerySpotPresenter;
    BaseBean<ScenerySpotListBean> scenerySpotListBean;
    BaseRecyclerAdapter adapter;
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_title_center)
    TextView tvTitleCenter;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.ry_scene_list)
    RecyclerView rySceneList;
    double userLongitude;
    double userLatitude;
    private AnimationDrawable drawable;
    //步行导航启动参数(引入esaeui下的jar包)
    WalkNaviLaunchParam walkParam;
    private LatLng startPt, endPt;
    private List<Boolean> isClicks = new ArrayList<>();//控件是否被点击,默认为false，如果被点击，改变值
    private ArrayMap<Integer,AnimationDrawable> mVoiceMap = new ArrayMap<>();
    @Override
    protected int setRootViewId() {
        return R.layout.activity_scene_list;
    }

    @Override
    protected boolean setIsFull() {
        return false;
    }

    @Override
    protected void InitialView() {
        EventBus.getDefault().register(this);
        StatusBarUtils.setWindowStatusBarColor(SceneListActivity.this, R.color.text_color4);
        userLongitude = Double.parseDouble(SPHelper.getInstance(SceneListActivity.this).getLongitude());
        userLatitude = Double.parseDouble(SPHelper.getInstance(SceneListActivity.this).getLatitude());
        scenerySpotListBean = (BaseBean<ScenerySpotListBean>) getIntent().getSerializableExtra("scenerySpotListBean");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SceneListActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rySceneList.setLayoutManager(linearLayoutManager);

        if (scenerySpotListBean == null) {
            //重新请求接口
        } else {
            adapter = new BaseRecyclerAdapter(SceneListActivity.this, scenerySpotListBean.getValue().getList(), R.layout.item_scene_list) {
                BaseViewHolder mHelper;
                @Override
                protected void convert(final BaseViewHolder helper, Object item, final int position) {
                    mHelper = helper;
                    for (int i = 0; i < scenerySpotListBean.getValue().getList().size(); i++) {
                        isClicks.add(false);
//                        mVoiceMap.put(i, (AnimationDrawable) helper.getView(R.id.img_play_voice).getBackground());
                    }
                    helper.setText(R.id.tv_scene_spot_name, scenerySpotListBean.getValue().getList().get(position).getName());
                    double sceneSpotLongitude = scenerySpotListBean.getValue().getList().get(position).getLng();
                    double sceneSpotLatitude = scenerySpotListBean.getValue().getList().get(position).getLat();

                    DecimalFormat df = new DecimalFormat("#.00");
                    double distance = getDistance(userLongitude, userLatitude, sceneSpotLongitude, sceneSpotLatitude);
                    if (distance >= 1000) {
                        String str = df.format(distance / 1000);
                        helper.setText(R.id.tv_scene_spot_distance, str + "千米");
                    } else {
                        String str = df.format(distance);
                        helper.setText(R.id.tv_scene_spot_distance, str + "米");
                    }
                    mVoiceMap.put(position, (AnimationDrawable) helper.getView(R.id.img_play_voice).getBackground());

//                    helper.setIsRecyclable(false);
                    drawable = (AnimationDrawable) helper.getView(R.id.img_play_voice).getBackground();
                    drawable.stop();
                    mVoiceMap.put(position, drawable);
                    L.gi().d(position+"=====1111===="+mVoiceMap.size());
                    helper.getView(R.id.img_play_voice).setOnClickListener(view -> {
//                        for (int i = 0; i < isClicks.size(); i++) {
//                            mVoiceMap.get(i).stop();
//                        }
                        for(AnimationDrawable drawable : mVoiceMap.values()){
                            drawable.stop();
                        }
//                        isClicks.set(position, true);
//                        notifyDataSetChanged();
//                        drawable = (AnimationDrawable) helper.getView(R.id.img_play_voice).getBackground();
                        drawable = mVoiceMap.get(position);
                        drawable.start();
//                        if (drawable != null&&!drawable.isRunning()) {
//                                drawable.start();
//                            }else if (drawable.isRunning()){
//                                drawable.stop();
//                            }
//                        drawable.stop();
//                        if (isClicks.get(position) == true) {
//                            L.gi().d(drawable.isRunning()+"=========");
//                            if (drawable != null&&!drawable.isRunning()) {
//                                drawable.start();
//                            }else if (drawable.isRunning()){
//                                drawable.stop();
//                            }
//                            VoiceUtil.getInstance().modeIndicater(SceneListActivity.this, scenerySpotListBean.getValue().getList().get(position).getScenerySpotId());
//                        }else {
//                            initImage();
//                            drawable.stop();
//                        }

                    });
                    helper.getView(R.id.rl_navi).setOnClickListener(view -> {
                        startPt = new LatLng(Double.parseDouble(SPHelper.getInstance(SceneListActivity.this).getLatitude()),
                                Double.parseDouble(SPHelper.getInstance(SceneListActivity.this).getLongitude()));
                        endPt = new LatLng(30.612961, 104.05325);
                        walkParam = new WalkNaviLaunchParam().stPt(startPt).endPt(endPt);
                        walkParam.extraNaviMode(0);
                        startWalkNavi();
                    });
                }
            };
        }
        rySceneList.setAdapter(adapter);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(MessageEventPlayComplete event) {
        if (event != null) {
            ToastUtils.msg("播放完成");
            if (drawable != null) {
                drawable.stop();
            }
        }
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
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
                intent.setClass(SceneListActivity.this, WNaviGuideActivity.class);
                startActivity(intent);
            }

            @Override
            public void onRoutePlanFail(WalkRoutePlanError error) {
                Log.d(TAG, "WalkNavi onRoutePlanFail" + error.name().toString());
            }

        });
    }

}
