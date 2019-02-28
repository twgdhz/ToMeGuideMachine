package com.guidemachine.ui.activity.shop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.guidemachine.R;
import com.guidemachine.base.ui.BaseActivity;
import com.guidemachine.service.entity.AppCreateKindOrderCartDto;
import com.guidemachine.service.entity.AppCreateKindOrderCartItemDto;
import com.guidemachine.service.entity.AppCreateKindOrderCartShopDto;
import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.CartKindOrderFillingBean;
import com.guidemachine.service.entity.CreateOrderBean;
import com.guidemachine.service.presenter.CartKindOrderFillingPresenter;
import com.guidemachine.service.presenter.CreateCartOrderPresenter;
import com.guidemachine.service.view.CartKindOrderFillingView;
import com.guidemachine.service.view.CreateOrderView;
import com.guidemachine.ui.activity.shop.adapter.ShoppingCartGoodsOrderInputAdapter;
import com.guidemachine.util.LogUtils;
import com.guidemachine.util.Logger;
import com.guidemachine.util.StatusBarUtils;
import com.guidemachine.util.ToastUtils;
import com.guidemachine.util.share.SPHelper;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * @author ChenLinWang
 * @email 422828518@qq.com
 * @create 2018/12/5 0005 15:51
 * description:购物车实体商品订单填写页提交订单
 */
public class ShoppingCartGoodsOrderInputActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_titlebar_center)
    TextView tvTitlebarCenter;
    @BindView(R.id.xl_order_list)
    ExpandableListView xlOrderList;
    @BindView(R.id.btn_order)
    Button btnOrder;
    @BindView(R.id.tv_total_price)
    TextView tvTotalPrice;
    @BindView(R.id.rl_total_price)
    RelativeLayout rlTotalPrice;
    @BindView(R.id.tv_postage)
    TextView tvPostage;
    @BindView(R.id.rl_postage)
    RelativeLayout rlPostage;
    @BindView(R.id.tv_discount)
    TextView tvDiscount;
    @BindView(R.id.rl_discount)
    RelativeLayout rlDiscount;
    @BindView(R.id.rl)
    RelativeLayout rl;
    CartKindOrderFillingPresenter cartKindOrderFillingPresenter;
    //购物车订单填写
    ShoppingCartGoodsOrderInputAdapter shoppingCartGoodsOrderInputAdapter;
    //购物车实体商品订单填写页提交订单，生成订单并返回支付页面数据
    CreateCartOrderPresenter createCartOrderPresenter;
    //上个页面传过来的总价
    String totalPrice;
    BaseBean<List<CartKindOrderFillingBean>> mCartKindOrderFillingBeanList;
    int selectGroupPosition;
    int selectChildPosition;
    int selectSelftype;//第一次请求获取到的真实的配送方式
    boolean isFirstClick = true;
    //购物车商品总折扣
    BigDecimal totalDiscount =new BigDecimal(0);
    //计算后的总价
    BigDecimal total_price=new BigDecimal(0);
    //总邮费
    BigDecimal totalPostage=new BigDecimal(0);
    String goodsItemIds;
    Set<String> set = new HashSet<String>();//存储选中的goodsid

    @Override
    protected int setRootViewId() {
        return R.layout.activity_shopping_cart_goods_order_input;
    }


    @Override
    protected boolean setIsFull() {
        return false;
    }

    @Override
    protected void InitialView() {
        Add(this);
        StatusBarUtils.setWindowStatusBarColor(ShoppingCartGoodsOrderInputActivity.this, R.color.text_color4);
        btnOrder.setOnClickListener(this);
        goodsItemIds = getIntent().getExtras().getString("goodsItemIds");
        totalPrice = getIntent().getExtras().getString("totalPrice");
        Logger.d("goodsItemIds", goodsItemIds);
        cartKindOrderFillingPresenter = new CartKindOrderFillingPresenter(ShoppingCartGoodsOrderInputActivity.this);
        cartKindOrderFillingPresenter.onCreate();
        cartKindOrderFillingPresenter.attachView(cartKindOrderFillingView);
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("goodsItemIds", goodsItemIds);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestData.toString());
        cartKindOrderFillingPresenter.cartKindOrderFilling(requestBody);
        showProgressDialog();

//        tvTotalPrice.setText(totalPrice);

        createCartOrderPresenter = new CreateCartOrderPresenter(ShoppingCartGoodsOrderInputActivity.this);
        createCartOrderPresenter.onCreate();
        createCartOrderPresenter.attachView(createOrderView);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    CartKindOrderFillingView cartKindOrderFillingView = new CartKindOrderFillingView() {
        @Override
        public void onSuccess(final BaseBean<List<CartKindOrderFillingBean>> mList) {
            dismissProgressDialog();
            LogUtils.d("CartKindOrderFillingBean", mList.toString());
            LogUtils.d("CartKindOrderFillingBean", mList.getValue().size() + "");
            mCartKindOrderFillingBeanList = mList;
            shoppingCartGoodsOrderInputAdapter = new ShoppingCartGoodsOrderInputAdapter(ShoppingCartGoodsOrderInputActivity.this, mList);
            shoppingCartGoodsOrderInputAdapter.setTotalPrice(totalPrice);
            xlOrderList.setAdapter(shoppingCartGoodsOrderInputAdapter);
            //使所有组展开
            for (int i = 0; i < shoppingCartGoodsOrderInputAdapter.getGroupCount(); i++) {
                xlOrderList.expandGroup(i);
            }
            //使组点击无效果
            xlOrderList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                @Override
                public boolean onGroupClick(ExpandableListView parent, View v,
                                            int groupPosition, long id) {
                    return true;
                }
            });
            xlOrderList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                    selectGroupPosition = groupPosition;
                    selectChildPosition = childPosition;
//                    if (isFirstClick == true) {
//                        isFirstClick = false;
//
//                    }
                    selectSelftype = mList.getValue().get(groupPosition).getKindOrderItems().get(childPosition).getSelfType();
//                    SPHelper.getInstance(ShoppingCartGoodsOrderInputActivity.this).setSelfType(selectSelftype + "");
//                    Intent intent = new Intent(ShoppingCartGoodsOrderInputActivity.this, SelectDistributionActivity.class);
////                    intent.putExtra("selfType", mList.getValue().get(groupPosition).getKindOrderItems().get(childPosition).getSelfType());
//                    startActivityForResult(intent, 1);
                    return false;
                }
            });
            CartKindOrderFillingBean.KindOrderItemsBean goodsBean;
            for (int i = 0; i < mList.getValue().size(); i++) {
                for (int j = 0; j < mList.getValue().get(i).getKindOrderItems().size(); j++) {
                    goodsBean = mList.getValue().get(i).getKindOrderItems().get(j);
                    if (goodsBean.getIsDiscountsPrice() == 1) {//是否有优惠 (0:无优惠1:有优惠)
                        //乘法
                        totalDiscount = totalDiscount.add((goodsBean.getDiscountsPrice()).multiply(goodsBean.getGoodsNum()) );
                    }
                    total_price = total_price.add((goodsBean.getUnitPrice() ).multiply(goodsBean.getGoodsNum()));
                    if (goodsBean.getUserSelfType() == 0) {// 配送方式   (0配送1自提2自提和配送)
//                        if (goodsBean.getIsExpress() == 0) {//是否包邮 (0:不包邮1:包邮) (当配送方式为配送的时候有数据)
//                            totalPostage = totalPostage + goodsBean.getExpressAmount();
//                        }
                    }
                    set.add(goodsBean.getGoodsId());
                }
            }
            tvTotalPrice.setText(total_price.subtract(totalDiscount.add(totalPostage))+"");
            tvPostage.setText("邮费:" + totalPostage);
            tvDiscount.setText("优惠：" + totalDiscount);
//            totalDiscount = 0;
//            total_price = 0;
//            totalPostage = 0;
        }

        @Override
        public void onError(String result) {
            dismissProgressDialog();
            ToastUtils.msg(result);
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2) {
            String way = data.getStringExtra("way");
//            if (way.contains("自提")) {
//                mCartKindOrderFillingBeanList.getValue().get(selectGroupPosition).getKindOrderItems().get(selectChildPosition).setSelfType(1);
//            } else if (way.contains("配送")) {
//                mCartKindOrderFillingBeanList.getValue().get(selectGroupPosition).getKindOrderItems().get(selectChildPosition).setSelfType(0);
//            }
//            shoppingCartGoodsOrderInputAdapter.notifyDataSetChanged();
            String goodsId = mCartKindOrderFillingBeanList.getValue().get(selectGroupPosition).getKindOrderItems().get(selectChildPosition).getGoodsId();
            JSONObject requestData = new JSONObject();
            try {
                requestData.put("goodsItemIds", goodsItemIds);

                if (way.contains("自提")) {
                    requestData.put("selfType", goodsId + ",1");
                } else if (way.contains("配送")) {
                    requestData.put("selfType", goodsId + ",0");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("CartKindOrder", goodsId + way);
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestData.toString());
            cartKindOrderFillingPresenter.cartKindOrderFilling(requestBody);
        }
    }

    @OnClick(R.id.rl_back)
    public void onClick() {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_order:
                for (int i = 0; i < mCartKindOrderFillingBeanList.getValue().size(); i++) {
                    for (int j = 0; j < mCartKindOrderFillingBeanList.getValue().get(i).getKindOrderItems().size(); j++) {
                        int type = mCartKindOrderFillingBeanList.getValue().get(i).getKindOrderItems().get(j).getUserSelfType();
                        if (mCartKindOrderFillingBeanList.getValue().get(i).getKindOrderItems().get(j).getUserSelfType() == 2) {//有等于2的情况，说明有未选择配送方式的商品
                            ToastUtils.msg("请确保所有商品都选择了配送方式");
                            return;
                        }
                    }
                }
                //购物车实体商品订单填写页提交订单，生成订单并返回支付页面数据
//                getData(mCartKindOrderFillingBeanList);
                getData(mCartKindOrderFillingBeanList);
                break;
        }

    }

    private void getData(BaseBean<List<CartKindOrderFillingBean>> source) {//生成双层数组
        AppCreateKindOrderCartDto cart = new AppCreateKindOrderCartDto();
        List<AppCreateKindOrderCartShopDto> shops = new ArrayList<>();
//        Double saleAmount = 0d;
        for (CartKindOrderFillingBean appCreateKindOrderCartDto : source.getValue()) {
            AppCreateKindOrderCartShopDto shop = new AppCreateKindOrderCartShopDto();
            shop.setShopId(appCreateKindOrderCartDto.getId());
            List<AppCreateKindOrderCartItemDto> items = new ArrayList<>();
            List<CartKindOrderFillingBean.KindOrderItemsBean> kindOrderItems = appCreateKindOrderCartDto.getKindOrderItems();
            for (CartKindOrderFillingBean.KindOrderItemsBean kindOrderItemsBean : kindOrderItems) {
                AppCreateKindOrderCartItemDto item = new AppCreateKindOrderCartItemDto();
                item.setGoodsItemId(kindOrderItemsBean.getGoodsItemId());
                int centerTransportFlag = kindOrderItemsBean.getCenterTransportFlag();
                if (centerTransportFlag == 1) {
//                    item.setSelfType(0);
                    item.setAddressId(kindOrderItemsBean.getCenterTransport().getId());
                } else {
//                    item.setSelfType(1);
                }
                items.add(item);
                shop.setItems(items);
//                saleAmount = saleAmount + kindOrderItemsBean.getUnitPrice() - kindOrderItemsBean.getDiscountsPrice() * kindOrderItemsBean.getGoodsNum() +
//                        kindOrderItemsBean.getExpressAmount() * kindOrderItemsBean.getGoodsNum();
            }
            shops.add(shop);
        }

        cart.setPrice(tvTotalPrice.getText().toString());
        cart.setShops(shops);
        JSONObject jsonObject1 = (JSONObject) JSONObject.toJSON(cart);
        Log.d("jsonObject1+:", jsonObject1.toJSONString());

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject1.toJSONString());
        createCartOrderPresenter.createCartOrder(requestBody);
        showProgressDialog();
    }

    CreateOrderView createOrderView = new CreateOrderView() {
        @Override
        public void onSuccess(BaseBean<CreateOrderBean> bean) {//创建购物车订单并返回支付页面数据
            dismissProgressDialog();
//            ToastUtils.msg(bean.getResultStatus().getResultMessage() + "");
            Log.d("CreateOrderBean", bean.toString());
            Intent intent = new Intent(ShoppingCartGoodsOrderInputActivity.this, GoodsOrderPaymentActivity.class);
//            intent.putExtra("orderNo", bean.getValue().getOrderNo());
//            intent.putExtra("price", bean.getValue().getSaleAmount() - bean.getValue().getDiscountsAmount());
            intent.putExtra("goodsOrderId", bean.getValue().getOrderId());
            intent.putExtra("source", "2");//此字段用来判断是从订单页面过来还是直接购买。1：直接购买 2：订单页面
            startActivity(intent);
            finish();
            FinishAll();
        }

        @Override
        public void onError(String result) {
            dismissProgressDialog();
            ToastUtils.msg(result);
        }
    };

    /*
 首先创建json对象，把需要的字段放到json中，将所有的json对象添加到
 List集合中，最后完成List与json的转化
 */
    public void getData1(BaseBean<List<CartKindOrderFillingBean>> list) {//作废
        Map<String, Object> shopMap = new HashMap<String, Object>();//装店铺的集合
        Map<String, Object> goodsMap = new HashMap<String, Object>();//装商品的集合

        Set<Map> jsonObjects = new HashSet<>();
        List<Map> jsonObjectss = new ArrayList<Map>();
        for (int i = 0; i < list.getValue().size(); i++) {
            goodsMap.put("merchantId", list.getValue().get(i).getId());
            for (int j = 0; j < list.getValue().get(i).getKindOrderItems().size(); j++) {
                Map<String, Object> map1 = new HashMap<String, Object>();
                map1.put("goodsItemId", list.getValue().get(i).getKindOrderItems().get(j).getGoodsItemId());
                map1.put("selfType", list.getValue().get(i).getKindOrderItems().get(j).getSelfType());
                if (list.getValue().get(i).getKindOrderItems().get(j).getCenterTransportFlag() == 1) {//是否有默认配送地址 (0:无配送地址,1:有配送地址)(当配送方式为配送的时候有数据)
                    map1.put("addressId", list.getValue().get(i).getKindOrderItems().get(j).getCenterTransport().getId());
                }
                jsonObjects.add(map1);
                System.out.println("集合中Map创建json对象  map1+:" + map1);
                System.out.println("集合中Map创建json对象  jsonObjects+:" + jsonObjects.toString());
                goodsMap.put("items", jsonObjects);
                jsonObjectss.add(goodsMap);
                shopMap.put("shops", jsonObjectss);
                shopMap.put("saleAmount", totalPrice.toString());
            }
        }
        System.out.println("集合中Map创建json对象:" + new JSONObject(shopMap));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cartKindOrderFillingPresenter.onStop();
    }

}
