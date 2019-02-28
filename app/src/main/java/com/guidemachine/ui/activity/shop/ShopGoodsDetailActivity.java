package com.guidemachine.ui.activity.shop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bumptech.glide.Glide;
import com.guidemachine.R;
import com.guidemachine.base.adapter.BaseRecyclerAdapter;
import com.guidemachine.base.adapter.BaseViewHolder;
import com.guidemachine.base.ui.BaseActivity;
import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.BuyAgainBean;
import com.guidemachine.service.entity.GoodSpecBean;
import com.guidemachine.service.entity.GoodsInfoDetailBean;
import com.guidemachine.service.presenter.GoodSpecPresenter;
import com.guidemachine.service.presenter.GuideDetailGoodsListPresenter;
import com.guidemachine.service.presenter.SelectGoodInfoPresenter;
import com.guidemachine.service.view.GoodSpecInfoView;
import com.guidemachine.service.view.GuideDetailGoodsListView;
import com.guidemachine.service.view.SelectGoodInfoView;
import com.guidemachine.ui.activity.ShopDetailActivity;
import com.guidemachine.ui.view.AddShoppingTrolleyPopupWindow;
import com.guidemachine.ui.view.CustomScollView;
import com.guidemachine.ui.view.ExpandTextView;
import com.guidemachine.util.BadgeView;
import com.guidemachine.util.Logger;
import com.guidemachine.util.NetworkImageHolderView;
import com.guidemachine.util.ScreenUtil;
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

import static com.bigkoo.convenientbanner.ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL;

/**
 * @author ChenLinWang
 * @email 422828518@qq.com
 * @create 2018/11/20 0020 11:29
 * description:商品详情
 */
public class ShopGoodsDetailActivity extends BaseActivity implements View.OnClickListener {
    SelectGoodInfoPresenter selectGoodInfoPresenter;
    String goodsId;
    double lat;
    double lon;
    @BindView(R.id.banner)
    ConvenientBanner banner;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_sale_num)
    TextView tvSaleNum;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.tv_distance)
    TextView tvDistance;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.img_dial_phone)
    ImageView imgDialPhone;
    @BindView(R.id.tb_get_self)
    TextView tbGetSelf;
    @BindView(R.id.rb_get_distribution)
    TextView rbGetDistribution;
    @BindView(R.id.rb_get_distribution_self)
    TextView rbGetDistributionSelf;
    @BindView(R.id.rg_distribution_way)
    LinearLayout rgDistributionWay;
    @BindView(R.id.tv_good_detail)
    TextView tvGoodDetail;
    @BindView(R.id.view_good_detail)
    View viewGoodDetail;
    @BindView(R.id.ll_good_detail)
    LinearLayout llGoodDetail;
    @BindView(R.id.tv_shop_notice)
    TextView tvShopNotice;
    @BindView(R.id.view_shop_notice)
    View viewShopNotice;
    @BindView(R.id.ll_shop_notice)
    LinearLayout llShopNotice;
    @BindView(R.id.tv_expanded_good_detail)
    ExpandTextView tvExpandedGoodDetail;
    @BindView(R.id.tv_expanded_shop_notice)
    ExpandTextView tvExpandedShopNotice;
    @BindView(R.id.ry_buy_again)
    RecyclerView ryBuyAgain;
    @BindView(R.id.scrollView)
    CustomScollView scrollView;
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_title_center)
    TextView tvTitleCenter;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.img_shopping_trolley)
    RelativeLayout imgShoppingTrolley;
    @BindView(R.id.ll_shopping_cart)
    LinearLayout llShoppingCart;
    @BindView(R.id.tv_add_shopping_trolley)
    TextView tvAddShoppingTrolley;
    @BindView(R.id.tv_pay_immediately)
    TextView tvPayImmediately;
    private List<String> networkImages;
    LinearLayout[] titleLinearLyaouts;
    TextView[] titleTextView;
    View[] titleViews;
    BadgeView badgeView;
    GoodSpecPresenter goodSpecPresenter;
    AddShoppingTrolleyPopupWindow popupWindow;
    //商品详情页‘买过该品还买过’模块商品展示(逛了又逛)
    GuideDetailGoodsListPresenter guideDetailGoodsListPresenter;
    BaseRecyclerAdapter adapter;
    String sceneryId;
    @Override
    protected int setRootViewId() {
        return R.layout.activity_shop_goods_detail;
    }


    @Override
    protected boolean setIsFull() {
        return false;
    }

    @Override
    protected void InitialView() {
        Add(this);
        StatusBarUtils.setWindowStatusBarColor(ShopGoodsDetailActivity.this, R.color.text_color4);
        setTranslucentStatus();
        goodsId = getIntent().getExtras().getString("goodsId");
        sceneryId = getIntent().getExtras().getString("sceneryId");
        lat = Double.parseDouble(SPHelper.getInstance(ShopGoodsDetailActivity.this).getLatitude());
        lon = Double.parseDouble(SPHelper.getInstance(ShopGoodsDetailActivity.this).getLongitude());
        selectGoodInfoPresenter = new SelectGoodInfoPresenter(ShopGoodsDetailActivity.this);
        selectGoodInfoPresenter.onCreate();
        selectGoodInfoPresenter.attachView(selectGoodInfoView);
        titleLinearLyaouts = new LinearLayout[]{llGoodDetail, llShopNotice};
        titleTextView = new TextView[]{tvGoodDetail, tvShopNotice};
        titleViews = new View[]{viewGoodDetail, viewShopNotice};
        tvExpandedGoodDetail.setVisibility(View.VISIBLE);
        tvExpandedShopNotice.setVisibility(View.GONE);
        llGoodDetail.setOnClickListener(this);
        llShopNotice.setOnClickListener(this);
        tvAddShoppingTrolley.setOnClickListener(this);
        llShoppingCart.setOnClickListener(this);
        tvPayImmediately.setOnClickListener(this);
        // 必须要计算并初始化宽度
        tvExpandedGoodDetail.initWidth(ScreenUtil.getScreenWidth(ShopGoodsDetailActivity.this) - ScreenUtil.dp2px(ShopGoodsDetailActivity.this, 80));
        String content = "  茫茫的长白大山，浩瀚的原始森林，大山脚下，原始森林环抱中散落着几十户人家的" +
                "一个小山村，茅草房，对面炕，烟筒立在屋后边。在村东头有一个独立的房子，那就是青年点，" +
                "窗前有一道小溪流过。学子在这里吃饭，由这里出发每天随社员去地里干活。干的活要么上山伐" +
                "树，抬树，要么砍柳树毛子开荒种地。在山里，可听那吆呵声：“顺山倒了！”放树谨防回头棒！" +
                "树上的枯枝打到别的树上再蹦回来，这回头棒打人最厉害。";
        tvExpandedGoodDetail.setMaxLines(4);
        tvExpandedGoodDetail.setCloseText(content);
        // 必须要计算并初始化宽度
        tvExpandedShopNotice.initWidth(ScreenUtil.getScreenWidth(ShopGoodsDetailActivity.this) - ScreenUtil.dp2px(ShopGoodsDetailActivity.this, 80));
        String notice = " 时光荏苒，岁月无声。日子不紧不慢的如涓涓溪水静静的流去，而从身边流去的只有时光，" +
                "沉淀下来的是与你一路相伴的幸福和快乐，温馨和安暖。于我，在这个凋零都感受到诗意横溢的秋，" +
                "只想做一件事，拈一片绯红的枫叶，轻轻地刻上我的心语。对信仰，是我今生永不改变的主题！而后，" +
                "幸福的寄往有你的那个城市。从此，在我心里，于我的生命里，轻握你许的安暖，" +
                "静静地在岁月的彼岸，为你守候一世永恒。夜幕降临了，春雨柔柔的亲吻着薄如蝉翼的纱帘，" +
                "有节奏的淅沥在窗棂上，更增添了无限的意念。意念中的我，在幸福和恬淡中，漫捻心弦化为若水般的轻柔曼妙在深情的雨夜里。" +
                "此刻，窗外的雨不再是清冷的秋雨了，在我的眸里是一种柔软，似撒娇少女的情怀，是怜、是爱、是柔、是润在我的心里是一种憧憬，" +
                "憧憬着一份美好的未来，与你相拥在花雨飘飞的时节，让爱情肆意的怒放在油纸伞下，青石边，丁香小巷这样的心境，" +
                "这样的时光，这样的时刻，心不经意间便醉了、醉了、醉在这如曼妙轻盈舞步的秋雨中";
        tvExpandedShopNotice.setMaxLines(4);
        tvExpandedShopNotice.setCloseText(notice);
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("goodsId", goodsId);
            requestData.put("lon", lon);
            requestData.put("lat", lat);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestData.toString());
        selectGoodInfoPresenter.selectGoodsInfo(requestBody);
        showProgressDialog();
        goodSpecPresenter = new GoodSpecPresenter(ShopGoodsDetailActivity.this);
        goodSpecPresenter.onCreate();


        guideDetailGoodsListPresenter = new GuideDetailGoodsListPresenter(ShopGoodsDetailActivity.this);
        guideDetailGoodsListPresenter.onCreate();
        guideDetailGoodsListPresenter.attachView(guideDetailGoodsListView);
        JSONObject requestData1 = new JSONObject();
        try {
            requestData1.put("sceneryId", sceneryId);
            requestData1.put("lon", Double.parseDouble(SPHelper.getInstance(ShopGoodsDetailActivity.this).getLongitude()));
            requestData1.put("lat", Double.parseDouble(SPHelper.getInstance(ShopGoodsDetailActivity.this).getLatitude()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody1 = RequestBody.create(MediaType.parse("application/json"), requestData1.toString());
        guideDetailGoodsListPresenter.getGuideDetailGoodsList(requestBody1);

    }

    SelectGoodInfoView selectGoodInfoView = new SelectGoodInfoView() {
        @Override
        public void onSuccess(BaseBean<GoodsInfoDetailBean> mGoodsInfoDetailBean) {
            dismissProgressDialog();
            Logger.d("mGoodsInfoDetailBean", mGoodsInfoDetailBean.getValue().toString());
            banner(mGoodsInfoDetailBean);
            tvName.setText(mGoodsInfoDetailBean.getValue().getGoodsName());
            tvLocation.setText(mGoodsInfoDetailBean.getValue().getAddress());
            tvSaleNum.setText("已售" + mGoodsInfoDetailBean.getValue().getVolume() + "份");
            tvDistance.setText("距您" + mGoodsInfoDetailBean.getValue().getDistanceFromTerminal() + "千米");
            tvPrice.setText("￥" + mGoodsInfoDetailBean.getValue().getMinPrice() + "");
            badgeView = new BadgeView(ShopGoodsDetailActivity.this);
            badgeView.setTargetView(imgShoppingTrolley);
            badgeView.setBackground(18, getResources().getColor(R.color.font_red));
            badgeView.setBadgeCount(mGoodsInfoDetailBean.getValue().getShoppingCartNum());
            badgeView.setTextSize(10);
            badgeView.setBadgeGravity(Gravity.TOP | Gravity.LEFT);
        }

        @Override
        public void onError(String result) {
            dismissProgressDialog();
            ToastUtils.msg(result);
        }
    };

    public void banner(BaseBean<GoodsInfoDetailBean> mGoodsInfoDetailBean) {
        networkImages = new ArrayList<>();
        for (int i = 0; i < mGoodsInfoDetailBean.getValue().getImageUrlList().size(); i++) {
            networkImages.add(mGoodsInfoDetailBean.getValue().getImageUrlList().get(i).getImageUrl());
        }
//            ToastUtils.msg(networkImages.size()+"");
        banner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        }, networkImages)
                .setPageIndicator(new int[]{R.mipmap.ic_home_idot_default, R.mipmap.ic_home_idot_selected});
        banner.setPageIndicatorAlign(CENTER_HORIZONTAL);
    }

    @Override
    protected void onResume() {
        super.onResume();
        banner.startTurning(5000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        banner.stopTurning();
        selectGoodInfoPresenter.onStop();
        goodSpecPresenter.onStop();
        guideDetailGoodsListPresenter.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_good_detail://商品详情
                setSelect(0);
                tvExpandedGoodDetail.setVisibility(View.VISIBLE);
                tvExpandedShopNotice.setVisibility(View.GONE);
                break;
            case R.id.ll_shop_notice://购物须知
                setSelect(1);
                tvExpandedGoodDetail.setVisibility(View.GONE);
                tvExpandedShopNotice.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_add_shopping_trolley://加入购物车
                goodSpecPresenter.attachView(goodSpecInfoView);
                showProgressDialog();
                JSONObject requestData = new JSONObject();
                try {
                    requestData.put("goodsId", goodsId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestData.toString());
                goodSpecPresenter.selectGoodsSpec(requestBody);
                break;
            case R.id.ll_shopping_cart://跳转到购物车
                Intent intent = new Intent(ShopGoodsDetailActivity.this, ShoppingCartActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_pay_immediately:
                goodSpecPresenter.attachView(goodSpecInfoView1);
                showProgressDialog();
                JSONObject requestData1 = new JSONObject();
                try {
                    requestData1.put("goodsId", goodsId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody requestBody1 = RequestBody.create(MediaType.parse("application/json"), requestData1.toString());
                goodSpecPresenter.selectGoodsSpec(requestBody1);
                break;
        }
    }

    public void setSelect(int position) {//设置被选中的状态
        for (int i = 0; i < titleLinearLyaouts.length; i++) {
            if (i == position) {
                titleTextView[i].setTextColor(getResources().getColor(R.color.orange));
                titleViews[i].setVisibility(View.VISIBLE);

            } else {
                titleTextView[i].setTextColor(getResources().getColor(R.color.text_color14));
                titleViews[i].setVisibility(View.INVISIBLE);
            }
        }
    }

    GoodSpecInfoView goodSpecInfoView = new GoodSpecInfoView() {
        @Override
        public void onSuccess(BaseBean<GoodSpecBean> mGoodSpecBean) {//获取商品规格成功之后再弹出商品规格框
            dismissProgressDialog();
            Log.d("GoodSpecBean", mGoodSpecBean.toString());
            popupWindow = new AddShoppingTrolleyPopupWindow(ShopGoodsDetailActivity.this, "shopCar", mGoodSpecBean, tvName.getText().toString());
            popupWindow.showPopupWindow(imgShoppingTrolley);
        }

        @Override
        public void onError(String result) {
            ToastUtils.msg(result);
        }
    };
    GoodSpecInfoView goodSpecInfoView1 = new GoodSpecInfoView() {
        @Override
        public void onSuccess(BaseBean<GoodSpecBean> mGoodSpecBean) {//获取商品规格成功之后再弹出商品规格框
            dismissProgressDialog();
            Log.d("GoodSpecBean", mGoodSpecBean.toString());
            popupWindow = new AddShoppingTrolleyPopupWindow(ShopGoodsDetailActivity.this, "pay", mGoodSpecBean, tvName.getText().toString());
            popupWindow.showPopupWindow(imgShoppingTrolley);
        }

        @Override
        public void onError(String result) {

        }
    };
    LinearLayoutManager linearLayoutManager;
    GuideDetailGoodsListView guideDetailGoodsListView = new GuideDetailGoodsListView() {
        @Override
        public void onSuccess(final BaseBean<List<BuyAgainBean>> list) {
            Log.d("BuyAgainBean", list.toString());
            linearLayoutManager = new LinearLayoutManager(ShopGoodsDetailActivity.this);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            ryBuyAgain.setLayoutManager(linearLayoutManager);
            adapter = new BaseRecyclerAdapter(ShopGoodsDetailActivity.this, list.getValue(), R.layout.item_but_again) {
                @Override
                protected void convert(BaseViewHolder helper, Object item, final int position) {
                    Glide.with(ShopGoodsDetailActivity.this).load(list.getValue().get(position).getImageUrl()).into((ImageView) helper.getView(R.id.img_photo));
                    helper.setText(R.id.tv_name, list.getValue().get(position).getGoodsName());
                    helper.setText(R.id.tv_grade, list.getValue().get(position).getGrade()+"A");
                    helper.setText(R.id.tv_sales, "已售" +list.getValue().get(position).getVolume()+"份");
                    helper.setText(R.id.tv_score, list.getValue().get(position).getGrade() + "分");
                    helper.setText(R.id.tv_distance, "距您" + list.getValue().get(position).getDistanceFromTerminal() + "千米");
                    helper.setText(R.id.tv_price, list.getValue().get(position).getMinPrice()+"");
//                    helper.setText(R.id.tv_sign,  list.getValue().get(position).getGoodsTypeName());
                    helper.setOnClickListener(R.id.ll_cardview, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(ShopGoodsDetailActivity.this, ShopGoodsDetailActivity.class);
                            intent.putExtra("goodsId", list.getValue().get(position).getGoodsId());
                            intent.putExtra("sceneryId", sceneryId);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            };
            ryBuyAgain.setAdapter(adapter);
        }

        @Override
        public void onError(String result) {
            ToastUtils.msg(result);
        }
    };

    @OnClick(R.id.rl_back)
    public void onClick() {
        finish();
    }

}
