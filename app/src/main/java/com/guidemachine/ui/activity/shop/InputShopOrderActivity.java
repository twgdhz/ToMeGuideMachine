package com.guidemachine.ui.activity.shop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.guidemachine.R;
import com.guidemachine.base.ui.BaseActivity;
import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.KindOrderFillingBean;
import com.guidemachine.service.presenter.KindOrderFillingPresenter;
import com.guidemachine.service.view.KindOrderFillingView;
import com.guidemachine.ui.activity.shop.view.ShopOrderDetailPopupWindow;
import com.guidemachine.util.StatusBarUtils;
import com.guidemachine.util.ToastUtils;
import com.guidemachine.util.share.SPHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * @author ChenLinWang
 * @email 422828518@qq.com
 * @create 2018/12/5 0005 16:53
 * description:商品直接购买订单填写
 */
public class InputShopOrderActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_title_center)
    TextView tvTitleCenter;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.tv_shop_name)
    TextView tvShopName;
    @BindView(R.id.img_photo)
    ImageView imgPhoto;
    @BindView(R.id.tv_good_name)
    TextView tvGoodName;
    @BindView(R.id.tv_goods_sku)
    TextView tvGoodsSku;
    @BindView(R.id.tv_pirce)
    TextView tvPirce;
    @BindView(R.id.tv_amount)
    TextView tvAmount;
    @BindView(R.id.tv_get_self_name_phone)
    TextView tvGetSelfNamePhone;
    @BindView(R.id.tv_get_self_address)
    TextView tvGetSelfAddress;
    @BindView(R.id.tv_order_due)
    TextView tvOrderDue;
    @BindView(R.id.ll_get_self)
    LinearLayout llGetSelf;
    @BindView(R.id.tv_is_bill)
    TextView tvIsBill;
    @BindView(R.id.tv_total_price)
    TextView tvTotalPrice;
    @BindView(R.id.img_price_detail)
    ImageView imgPriceDetail;
    @BindView(R.id.ll_see_price_detail)
    LinearLayout llSeePriceDetail;
    @BindView(R.id.tv_input_order)
    TextView tvInputOrder;
    private boolean isShow = false;
    ShopOrderDetailPopupWindow shopOrderDetailPopupWindow;
    int num;
    String price;
    private int flag;
    private String goodsPriceId;
    private String goodsId;
    private String goodsSkuId;
    //商品订单填写
    KindOrderFillingPresenter kindOrderFillingPresenter;
    BaseBean<KindOrderFillingBean> kindOrderFillingBeanBaseBean;
    private static final int REQUEST_CODE = 10000;

    @Override
    protected int setRootViewId() {
        return R.layout.activity_input_shop_order;
    }

    @Override
    protected void InitialView() {
        Add(this);
        StatusBarUtils.setWindowStatusTextAndBarColor(InputShopOrderActivity.this);
        StatusBarUtils.setWindowStatusBarColor(InputShopOrderActivity.this, R.color.text_color4);
        tvTitleCenter.setText("订单填写");
        tvInputOrder.setOnClickListener(this);
        llGetSelf.setVisibility(View.GONE);
        llSeePriceDetail.setOnClickListener(this);
        num = getIntent().getExtras().getInt("num");
        price = getIntent().getExtras().getString("price");
        flag = getIntent().getExtras().getInt("flag");
        goodsPriceId = getIntent().getExtras().getString("goodsPriceId") == null ? null : getIntent().getExtras().getString("goodsPriceId");
        goodsSkuId = getIntent().getExtras().getString("goodsSkuId");
        goodsId = getIntent().getExtras().getString("goodsId");
        tvPirce.setText("￥" + price);
        tvAmount.setText("数量X" + num);
        kindOrderFillingPresenter = new KindOrderFillingPresenter(InputShopOrderActivity.this);
        kindOrderFillingPresenter.onCreate();
        kindOrderFillingPresenter.attachView(kindOrderFillingView);
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("flag", flag);
            requestData.put("goodsId", goodsId);
            requestData.put("goodsNum", num);
            requestData.put("goodsPriceId", goodsPriceId);
            requestData.put("goodsSkuId", goodsSkuId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestData.toString());
        kindOrderFillingPresenter.kindOrderFilling(requestBody);
        showProgressDialog();
    }

    KindOrderFillingView kindOrderFillingView = new KindOrderFillingView() {
        @Override
        public void onSuccess(BaseBean<KindOrderFillingBean> mKindOrderFillingBean) {
            Log.d("KindOrderFillingBean", mKindOrderFillingBean.toString());
            dismissProgressDialog();
            kindOrderFillingBeanBaseBean = mKindOrderFillingBean;
            tvShopName.setText(mKindOrderFillingBean.getValue().getShopName());
            tvGoodName.setText(mKindOrderFillingBean.getValue().getGoodsName());
            tvGoodsSku.setText(mKindOrderFillingBean.getValue().getSkuName());
            tvPirce.setText("￥" + mKindOrderFillingBean.getValue().getUnitPrice() + "");
            tvAmount.setText("数量X" + mKindOrderFillingBean.getValue().getGoodsNum());
            Glide.with(InputShopOrderActivity.this).load(mKindOrderFillingBean.getValue().getImageUrl()).into(imgPhoto);
            if (mKindOrderFillingBean.getValue().getIsDiscountsPrice() == 0) {//是否有优惠 (0:无优惠1:有优惠)
//                double totalPrice = (mKindOrderFillingBean.getValue().getUnitPrice() * mKindOrderFillingBean.getValue().getGoodsNum());
//                BigDecimal UnitPrice =new BigDecimal(mKindOrderFillingBean.getValue().getUnitPrice() );
//                BigDecimal GoodsNum =new BigDecimal(mKindOrderFillingBean.getValue().getGoodsNum() );
                //乘法
                BigDecimal bigDecimalMultiply = mKindOrderFillingBean.getValue().getUnitPrice().multiply(mKindOrderFillingBean.getValue().getGoodsNum());
                double totalPrice = bigDecimalMultiply.doubleValue();
                tvTotalPrice.setText(totalPrice + "");
            } else {
//                double totalPrice = (mKindOrderFillingBean.getValue().getUnitPrice() * mKindOrderFillingBean.getValue().getGoodsNum())
//                        - mKindOrderFillingBean.getValue().getDiscountsPrice() * mKindOrderFillingBean.getValue().getGoodsNum();
//                BigDecimal UnitPrice =new BigDecimal(mKindOrderFillingBean.getValue().getUnitPrice() );
//                BigDecimal DiscountsPrice =new BigDecimal(mKindOrderFillingBean.getValue().getDiscountsPrice() );
//                BigDecimal GoodsNum =new BigDecimal(mKindOrderFillingBean.getValue().getGoodsNum() );
                //减法
                BigDecimal bigDecimalSubtract = mKindOrderFillingBean.getValue().getUnitPrice().subtract(mKindOrderFillingBean.getValue().getDiscountsPrice());
                //乘法
                BigDecimal bigDecimalMultiply = bigDecimalSubtract.multiply(mKindOrderFillingBean.getValue().getGoodsNum());
                double totalPrice = bigDecimalMultiply.doubleValue();
                tvTotalPrice.setText(totalPrice + "");
            }
            tvIsBill.setText(mKindOrderFillingBean.getValue().getIsBill() == 0 ? "不支持" : "支持");//是否支持发票(0不支持1支持)

            llGetSelf.setVisibility(View.VISIBLE);
            tvGetSelfNamePhone.setText(kindOrderFillingBeanBaseBean.getValue().getShopName() + " " + kindOrderFillingBeanBaseBean.getValue().getTelephone());
            tvGetSelfAddress.setText(kindOrderFillingBeanBaseBean.getValue().getAddress());
            if (kindOrderFillingBeanBaseBean.getValue().getIsOrderDue() == 0) {//是否有订单有效期  (0:无1:有)
                tvOrderDue.setVisibility(View.GONE);
            } else {
                tvOrderDue.setVisibility(View.VISIBLE);
                tvOrderDue.setText("请于" + kindOrderFillingBeanBaseBean.getValue().getOrderDue() + "个工作日之内自提，否则过期作废");
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
    protected boolean setIsFull() {
        return false;
    }

    @OnClick(R.id.rl_back)
    public void onClick() {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_input_order://提交订单
//                if (tvGetSelfNamePhone.getText().toString().equals("") && tvGetDistributionNamePhone.getText().toString().equals("")) {
//                    ToastUtils.msg("请选择配送方式");
//                    return;
//                }
                Intent intentOrder = new Intent(InputShopOrderActivity.this, GoodsOrderPaymentActivity.class);
                intentOrder.putExtra("flag", kindOrderFillingBeanBaseBean.getValue().getFlag());
                intentOrder.putExtra("goodsId", kindOrderFillingBeanBaseBean.getValue().getGoodsId());
                intentOrder.putExtra("goodsNum", kindOrderFillingBeanBaseBean.getValue().getGoodsNum().intValue());
                intentOrder.putExtra("goodsSkuId", kindOrderFillingBeanBaseBean.getValue().getSkuId());
//                double totalPrice = (kindOrderFillingBeanBaseBean.getValue().getUnitPrice() * kindOrderFillingBeanBaseBean.getValue().getGoodsNum())
//                        - kindOrderFillingBeanBaseBean.getValue().getDiscountsPrice() * kindOrderFillingBeanBaseBean.getValue().getGoodsNum();
                //减法
                BigDecimal bigDecimalSubtract = kindOrderFillingBeanBaseBean.getValue().getUnitPrice().subtract(kindOrderFillingBeanBaseBean.getValue().getDiscountsPrice());
                //乘法
                BigDecimal bigDecimalMultiply = bigDecimalSubtract.multiply(kindOrderFillingBeanBaseBean.getValue().getGoodsNum());
                double totalPrice = bigDecimalMultiply.doubleValue();
//                tvTotalPrice.setText(totalPrice + "");
                intentOrder.putExtra("saleAmount", totalPrice);
                intentOrder.putExtra("goodsPriceId", kindOrderFillingBeanBaseBean.getValue().getGoodsPriceId());
                intentOrder.putExtra("source", "1");//此字段用来判断是从订单页面过来还是直接购买。1：直接购买 2：订单页面
                startActivity(intentOrder);
                break;
            case R.id.ll_see_price_detail:
                shopOrderDetailPopupWindow = new ShopOrderDetailPopupWindow(InputShopOrderActivity.this, kindOrderFillingBeanBaseBean);
                if (isShow == false) {
                    isShow = true;
                    shopOrderDetailPopupWindow.showPopupWindow(llSeePriceDetail);
                    imgPriceDetail.setBackgroundResource(R.mipmap.ic_home_order_show_price_detail);
                } else if (isShow == true) {
                    isShow = false;
                    shopOrderDetailPopupWindow.dissmiss();
                    imgPriceDetail.setBackgroundResource(R.mipmap.ic_home_order_hide_price_detail);
                }
                break;
        }
    }


}
