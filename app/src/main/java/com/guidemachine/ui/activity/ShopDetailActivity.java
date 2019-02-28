package com.guidemachine.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.walknavi.WalkNavigateHelper;
import com.baidu.mapapi.walknavi.adapter.IWEngineInitListener;
import com.baidu.mapapi.walknavi.adapter.IWRoutePlanListener;
import com.baidu.mapapi.walknavi.model.WalkRoutePlanError;
import com.baidu.mapapi.walknavi.params.WalkNaviLaunchParam;
import com.bumptech.glide.Glide;
import com.guidemachine.R;
import com.guidemachine.base.adapter.BaseRecyclerAdapter;
import com.guidemachine.base.adapter.BaseViewHolder;
import com.guidemachine.base.adapter.Listener.OnRecyclerItemClickListener;
import com.guidemachine.base.ui.BaseActivity;
import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.ShopBasicInfoBean;
import com.guidemachine.service.entity.ShopGoodListBean;
import com.guidemachine.service.presenter.ShopBasicInfoPresenter;
import com.guidemachine.service.presenter.ShopGoodListPresenter;
import com.guidemachine.service.view.ShopBasicInfoView;
import com.guidemachine.service.view.ShopGoodListView;
import com.guidemachine.ui.activity.shop.ShopGoodsDetailActivity;
import com.guidemachine.ui.activity.walknavi.WNaviGuideActivity;
import com.guidemachine.util.IntentUtils;
import com.guidemachine.util.Logger;
import com.guidemachine.util.StatusBarUtils;
import com.guidemachine.util.ToastUtils;
import com.guidemachine.util.share.SPHelper;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * @author ChenLinWang
 * @email 422828518@qq.com
 * @create 2018/12/3 0003 16:38
 * description: 店铺详情
 */
public class ShopDetailActivity extends BaseActivity {

    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_title_center)
    TextView tvTitleCenter;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    String shopId;
    ShopBasicInfoPresenter shopBasicInfoPresenter;
    @BindView(R.id.img_shop_top_bg)
    ImageView imgShopTopBg;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_distance)
    TextView tvDistance;
    @BindView(R.id.tv_go_there)
    TextView tvGoThere;
    double lat;
    double lon;
    ShopGoodListPresenter shopGoodListPresenter;
    BaseRecyclerAdapter adapter;
    @BindView(R.id.ry_good_list)
    RecyclerView ryGoodList;
    //步行导航启动参数(引入esaeui下的jar包)
    WalkNaviLaunchParam walkParam;
    private LatLng startPt, endPt;
    @Override
    protected int setRootViewId() {
        return R.layout.activity_shop_detail;
    }


    @Override
    protected boolean setIsFull() {
        return false;
    }

    @Override
    protected void InitialView() {
        StatusBarUtils.setWindowStatusBarColor(ShopDetailActivity.this, R.color.text_color4);
        shopId = getIntent().getExtras().getString("shopId");
        lat = Double.parseDouble(SPHelper.getInstance(ShopDetailActivity.this).getLatitude());
        lon = Double.parseDouble(SPHelper.getInstance(ShopDetailActivity.this).getLongitude());
//        llItem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                IntentUtils.openActivity(ShopDetailActivity.this, ShopGoodsDetailActivity.class);
//            }
//        });
        shopBasicInfoPresenter = new ShopBasicInfoPresenter(ShopDetailActivity.this);
        shopBasicInfoPresenter.onCreate();
        shopBasicInfoPresenter.attachView(shopBasicInfoView);
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("shopId", shopId);
            requestData.put("lat", lat);
            requestData.put("lon", lon);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestData.toString());
        shopBasicInfoPresenter.selectShopBasicInfo(requestBody);

        shopGoodListPresenter = new ShopGoodListPresenter(ShopDetailActivity.this);
        shopGoodListPresenter.onCreate();
        shopGoodListPresenter.attachView(shopGoodListView);
        JSONObject requestGoodData = new JSONObject();
        try {
            requestGoodData.put("shopId", shopId);
//            requestGoodData.put("sceneryId", SPHelper.getInstance(ShopDetailActivity.this).getSceneryId());
            requestGoodData.put("pageNum", 1);
            requestGoodData.put("pageSize", 150);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestGoodBody = RequestBody.create(MediaType.parse("application/json"), requestGoodData.toString());
        shopGoodListPresenter.selectShopGoodsList(requestGoodBody);
        showProgressDialog();
        tvGoThere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPt = new LatLng(Double.parseDouble(SPHelper.getInstance(ShopDetailActivity.this).getLatitude()),
                        Double.parseDouble(SPHelper.getInstance(ShopDetailActivity.this).getLongitude()));
                endPt = new LatLng(30.612961, 104.05325);
                walkParam = new WalkNaviLaunchParam().stPt(startPt).endPt(endPt);
                walkParam.extraNaviMode(0);
                startWalkNavi();
            }
        });
    }

    ShopBasicInfoView shopBasicInfoView = new ShopBasicInfoView() {
        @Override
        public void onSuccess(BaseBean<ShopBasicInfoBean> mShopBasicInfoBean) {
            Glide.with(ShopDetailActivity.this).load(mShopBasicInfoBean.getValue().getImageUrlList().get(0)).into(imgShopTopBg);
            tvAddress.setText(mShopBasicInfoBean.getValue().getAddress());
            tvDistance.setText("距离您" + mShopBasicInfoBean.getValue().getDistanceFromTerminal() + "千米");
            tvTitleCenter.setText(mShopBasicInfoBean.getValue().getName());

        }

        @Override
        public void onError(String result) {
        }
    };
    LinearLayoutManager linearLayoutManager;
    ShopGoodListBean.ListBean bean;
    ShopGoodListView shopGoodListView = new ShopGoodListView() {
        @Override
        public void onSuccess(final BaseBean<ShopGoodListBean> mShopGoodListBean) {
            dismissProgressDialog();
            Logger.d("ShopGoodListBean", mShopGoodListBean.getValue().toString());
            SPHelper.getInstance(ShopDetailActivity.this).setSceneryId(mShopGoodListBean.getValue().getSceneryId());
           ;
            Logger.d("getSceneryId",  SPHelper.getInstance(ShopDetailActivity.this).getSceneryId());
            linearLayoutManager = new LinearLayoutManager(ShopDetailActivity.this);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            ryGoodList.setLayoutManager(linearLayoutManager);
            if (mShopGoodListBean.getValue().getTotal() > 0) {
                adapter = new BaseRecyclerAdapter(ShopDetailActivity.this, mShopGoodListBean.getValue().getList(), R.layout.item_good_list) {
                    @Override
                    protected void convert(BaseViewHolder helper, Object item, int position) {
                        bean = mShopGoodListBean.getValue().getList().get(position);
                        helper.setText(R.id.tv_good_name, bean.getGoodsName());
                        Glide.with(mContext).load(bean.getImageUrl()).into((ImageView) helper.getView(R.id.img_good_photo));
                        helper.setText(R.id.tv_score, bean.getGrade() + "分");
                        helper.setText(R.id.tv_comments, bean.getEvaluateNum() + "评价");
                        helper.setText(R.id.tv_sales, bean.getVolume() + "人已买");
                        helper.setText(R.id.tv_distance, "距离"+ bean.getSceneryName()+ bean.getDistanceFromScenery() + "千米");
                        helper.setText(R.id.tv_price, "" + bean.getMinPrice());
                    }
                };
                ryGoodList.setAdapter(adapter);
                adapter.setOnRecyclerItemClickListener(new OnRecyclerItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
//                        IntentUtils.openActivity(ShopDetailActivity.this, ShopGoodsDetailActivity.class,
//                                "goodsId", mShopGoodListBean.getValue().getList().get(position).getGoodsId()
//                        ,"sceneryId",mShopGoodListBean.getValue().getList().get(position).getSceneryId());
                        Intent  intent=new Intent(ShopDetailActivity.this, ShopGoodsDetailActivity.class);
                        intent.putExtra("goodsId",mShopGoodListBean.getValue().getList().get(position).getGoodsId());
                        intent.putExtra("sceneryId",mShopGoodListBean.getValue().getList().get(position).getSceneryId());
                        startActivity(intent);
                    }
                });
            }
        }

        @Override
        public void onError(String result) {
            dismissProgressDialog();
            ToastUtils.msg(result);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        shopGoodListPresenter.onStop();
        shopBasicInfoPresenter.onStop();
    }

    @OnClick(R.id.rl_back)
    public void onClick() {
        finish();
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
                intent.setClass(ShopDetailActivity.this, WNaviGuideActivity.class);
                startActivity(intent);
            }

            @Override
            public void onRoutePlanFail(WalkRoutePlanError error) {
                Log.d(TAG, "WalkNavi onRoutePlanFail" + error.name().toString());
            }

        });
    }

}
