package com.guidemachine.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.guidemachine.R;
import com.guidemachine.base.adapter.BaseRecyclerAdapter;
import com.guidemachine.base.adapter.BaseViewHolder;
import com.guidemachine.base.ui.BaseActivity;
import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.ShopListBean;
import com.guidemachine.service.entity.ShopMarkersBean;
import com.guidemachine.service.presenter.ShopListPresenter;
import com.guidemachine.service.view.ShopListView;
import com.guidemachine.ui.adapter.ShopListAdapter;
import com.guidemachine.util.Logger;
import com.guidemachine.util.StatusBarUtils;
import com.guidemachine.util.ToastUtils;
import com.guidemachine.util.share.SPHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * @author ChenLinWang
 * @email 422828518@qq.com
 * @create 2018/11/13 0013 10:23
 * description:
 */

public class ShopListActivity extends BaseActivity {

    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_title_center)
    TextView tvTitleCenter;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    ShopListPresenter shopListPresenter;
    double lat;
    double lon;

    @BindView(R.id.ry_shop_list)
    RecyclerView ryShopList;
    ShopListAdapter shopListAdapter;

    @Override
    protected int setRootViewId() {
        return R.layout.activity_shop_list;
    }

    @Override
    protected boolean setIsFull() {
        return false;
    }

    @Override
    protected void InitialView() {
        StatusBarUtils.setWindowStatusBarColor(ShopListActivity.this, R.color.text_color4);
        lat = Double.parseDouble(SPHelper.getInstance(ShopListActivity.this).getLatitude());
        lon = Double.parseDouble(SPHelper.getInstance(ShopListActivity.this).getLongitude());
//        llItem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                IntentUtils.openActivity(ShopListActivity.this, ShopDetailActivity.class);
//            }
//        });
        shopListPresenter = new ShopListPresenter(ShopListActivity.this);
        shopListPresenter.onCreate();
        shopListPresenter.attachView(shopListView);
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("lat", lat);
            requestData.put("lon", lon);
            requestData.put("pageNum", 1);
            requestData.put("pageSize", 15);
            requestData.put("sceneryId", 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestData.toString());
        shopListPresenter.selectShopList(requestBody);
        showProgressDialog();
    }

    LinearLayoutManager linearLayoutManager;
    ShopListView shopListView = new ShopListView() {
        @Override
        public void onSuccess(final BaseBean<ShopListBean> mBaseBean) {
            dismissProgressDialog();
            Logger.d("ShopList", mBaseBean.getValue().getList().toString());
            linearLayoutManager = new LinearLayoutManager(ShopListActivity.this);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            ryShopList.setLayoutManager(linearLayoutManager);
            shopListAdapter = new ShopListAdapter(mBaseBean.getValue().getList(), ShopListActivity.this);
            ryShopList.setAdapter(shopListAdapter);

        }

        @Override
        public void onError(String result) {
            dismissProgressDialog();
            ToastUtils.msg(result);
        }
    };

    @OnClick(R.id.rl_back)
    public void onClick() {
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        shopListPresenter.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
