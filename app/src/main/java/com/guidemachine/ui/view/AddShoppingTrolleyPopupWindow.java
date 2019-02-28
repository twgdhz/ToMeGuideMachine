package com.guidemachine.ui.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.guidemachine.R;
import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.GoodSpecBean;
import com.guidemachine.service.presenter.AddGoodsCartPresenter;
import com.guidemachine.service.presenter.GoodSpecPresenter;
import com.guidemachine.service.view.AddGoodCartView;
import com.guidemachine.service.view.BaseView;
import com.guidemachine.service.view.GoodSpecInfoView;
import com.guidemachine.ui.activity.shop.InputShopOrderActivity;
import com.guidemachine.ui.adapter.GoodsSpecificationAdapter;
import com.guidemachine.util.Logger;
import com.guidemachine.util.ToastUtils;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;


/**
 * Created by Administrator on 2018/8/28/018.
 * 查看商品规格
 */

public class AddShoppingTrolleyPopupWindow extends PopupWindow implements View.OnClickListener, GoodsSpecificationAdapter.GetStock {
    private Activity context;
    private RelativeLayout llPopWindow;
    Window window;
    WindowManager.LayoutParams lp;
    /* 购买数量 */
    private TextView tv_exchange_num;
    /* 购买数量 */
    private int num = 1;
    private TextView tv_remove_num;
    private TextView tv_add_num;
    private TextView tvEnsure;
    private TextView tvSouvenirName;
    private TextView tvSouvenirPrice, tvRemain;
    private RecyclerView rySpecification;
    private ImageView imgSouvenirPhoto;
    TagFlowLayout tagColor;
    TagFlowLayout tagSize;
    private List<String> mColors = new ArrayList<>();
    private List<String> mSize = new ArrayList<>();
    private String sign;
    BaseBean<GoodSpecBean> getSpecificsBean;
    private String goodsName;
    //获取对应规格下的商品价格、库存等信息
    GoodSpecPresenter getInfoBySpecificPresenter;
    String color = "";
    String size = "";
    //添加到购物车
    AddGoodsCartPresenter addGoodsCartPresenter;
    GoodsSpecificationAdapter adapter;
    LinearLayoutManager linearLayoutManager;

    public AddShoppingTrolleyPopupWindow(Activity context, String sign) {
        super(context);
        this.sign = sign;
        init(context);
    }

    public AddShoppingTrolleyPopupWindow(Activity context, String sign, BaseBean<GoodSpecBean> getSpecificsBean, String goodsName) {
        super(context);
        this.context = context;
        this.sign = sign;
        this.getSpecificsBean = getSpecificsBean;
        this.goodsName = goodsName;
        init(context);
    }

    public void init(final Activity context) {
        View view = View.inflate(context, R.layout.add_shopping_trolley_pop, null);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setOutsideTouchable(true);
        this.setContentView(view);

        //背景虚化
        window = context.getWindow();
        lp = window.getAttributes();
        lp.alpha = 0.3f;
        window.setAttributes(lp);
        llPopWindow = view.findViewById(R.id.rl_close);
        tv_remove_num = view.findViewById(R.id.tv_remove_num);
        tv_add_num = view.findViewById(R.id.tv_add_num);
        tv_exchange_num = view.findViewById(R.id.tv_exchange_num);
        tvEnsure = view.findViewById(R.id.tv_ensure);
        tvSouvenirName = view.findViewById(R.id.tv_souvenir_name);
        tvSouvenirPrice = view.findViewById(R.id.tv_souvenir_price);
        rySpecification = view.findViewById(R.id.ry_spec);
        imgSouvenirPhoto = view.findViewById(R.id.img_souvenir_photo);
        tvRemain = view.findViewById(R.id.tv_remain);
        llPopWindow.setOnClickListener(this);
        tv_remove_num.setOnClickListener(this);
        tv_add_num.setOnClickListener(this);
        tvSouvenirName.setText(goodsName);
        tvSouvenirPrice.setText(getSpecificsBean.getValue().getMinPrice() + "-" + getSpecificsBean.getValue().getMaxPrice());
        Glide.with(context).load(getSpecificsBean.getValue().getImageUrl()).into(imgSouvenirPhoto);
//        Logger.d("规格:   ", getString().toString());
        linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rySpecification.setLayoutManager(linearLayoutManager);
        adapter = new GoodsSpecificationAdapter(getSpecificsBean, context);
        rySpecification.setAdapter(adapter);
        adapter.setGetStock(this);
        getInfoBySpecificPresenter = new GoodSpecPresenter(context);
        getInfoBySpecificPresenter.onCreate();
        getInfoBySpecificPresenter.attachView(getInfoBySpecificView);
        addGoodsCartPresenter = new AddGoodsCartPresenter(context);
        addGoodsCartPresenter.onCreate();
        addGoodsCartPresenter.attachView(addGoodCartView);
        if (getSpecificsBean.getValue().getFlag() == 0) {//标志 (是否有规格0:没有1:有)
            rySpecification.setVisibility(View.GONE);
            tv_add_num.setEnabled(true);
            tvSouvenirPrice.setText(getSpecificsBean.getValue().getMinPrice() + "");
            remain = getSpecificsBean.getValue().getStock();
            salePrice = getSpecificsBean.getValue().getMinPrice();
            tvRemain.setText(getSpecificsBean.getValue().getStock() + "");
            tvEnsure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (sign.equals("shopCar")) {
                        JSONObject requestData = new JSONObject();
                        try {
                            requestData.put("flag", 0);
                            requestData.put("goodsId", getSpecificsBean.getValue().getGoodsId());
                            requestData.put("goodsNum", Integer.parseInt(tv_exchange_num.getText().toString()));
                            requestData.put("goodsPriceId", getSpecificsBean.getValue().getGoodsPriceId());
//                        requestData.put("goodsSkuId", 1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestData.toString());
                        addGoodsCartPresenter.addGoodsToCart(requestBody);

                    } else if (sign.equals("pay")){
                        Intent intent = new Intent(context, InputShopOrderActivity.class);
                        intent.putExtra("num", num);
                        intent.putExtra("price", tvSouvenirPrice.getText().toString());
                        intent.putExtra("flag", getSpecificsBean.getValue().getFlag());
                        intent.putExtra("goodsId", getSpecificsBean.getValue().getGoodsId());
                        if (getSpecificsBean.getValue().getGoodsPriceId() != null) {
                            intent.putExtra("goodsPriceId", getSpecificsBean.getValue().getGoodsPriceId().toString());
                        }
                        context.startActivity(intent);
                    }
                }
            });
        } else {
            rySpecification.setVisibility(View.VISIBLE);
            tv_add_num.setEnabled(false);

        }
    }

    int remain = 1;
    double salePrice;
    GoodSpecInfoView getInfoBySpecificView = new GoodSpecInfoView() {
        @Override
        public void onSuccess(final BaseBean<GoodSpecBean> bean) {
            Log.d("GetInfoBySpecificBean", bean.toString());
            tvRemain.setText(bean.getValue().getStock() + "");
            tvSouvenirPrice.setText(bean.getValue().getMinPrice() + "");
            salePrice = bean.getValue().getMinPrice();
            remain = bean.getValue().getStock();
            if (bean.getValue().getStock() > 1) {
                tv_add_num.setEnabled(true);
            } else {
                tv_add_num.setEnabled(false);
            }
        }

        @Override
        public void onError(String result) {
            ToastUtils.msg(result);
        }
    };


    @Override
    public void stockNum(final GoodSpecBean.GoodsSkuListBean goodsSkuListBean) {//有规格
        if (goodsSkuListBean != null) {
            Logger.d("goodsSkuListBean:  ", goodsSkuListBean.toString());
            tvRemain.setText(goodsSkuListBean.getSkuNum() + "");
            remain = Integer.parseInt(goodsSkuListBean.getSkuNum());
            salePrice = goodsSkuListBean.getPrice();
            tvSouvenirPrice.setText("" + goodsSkuListBean.getPrice());
            if (Integer.parseInt(goodsSkuListBean.getSkuNum()) > 1) {
                tv_add_num.setEnabled(true);
            } else {
                tv_add_num.setEnabled(false);
            }
            tvEnsure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (sign.equals("shopCar")) {
                        JSONObject requestData = new JSONObject();
                        try {
                            requestData.put("flag", 0);
                            requestData.put("goodsId", getSpecificsBean.getValue().getGoodsId());
                            requestData.put("goodsNum", Integer.parseInt(tv_exchange_num.getText().toString()));
                            requestData.put("goodsPriceId", getSpecificsBean.getValue().getGoodsPriceId());
                            requestData.put("goodsSkuId", goodsSkuListBean.getGoodsSkuId());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestData.toString());
                        addGoodsCartPresenter.addGoodsToCart(requestBody);
                    } else if (sign.equals("pay")) {
                        Intent intent = new Intent(context, InputShopOrderActivity.class);
                        intent.putExtra("num", num);
                        intent.putExtra("price", tvSouvenirPrice.getText().toString());
                        intent.putExtra("flag", getSpecificsBean.getValue().getFlag());
                        intent.putExtra("goodsId", getSpecificsBean.getValue().getGoodsId());
                        intent.putExtra("goodsPriceId", getSpecificsBean.getValue().getGoodsPriceId().toString());
                        intent.putExtra("goodsSkuId", goodsSkuListBean.getGoodsSkuId());
                        context.startActivity(intent);
                    }
                }
            });
        } else {
            tvRemain.setText("0");
            tv_add_num.setEnabled(false);
            tvEnsure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtils.msg("没有对应规格的库存！");
                }
            });
        }
    }

    AddGoodCartView addGoodCartView = new AddGoodCartView() {
        @Override
        public void onSuccess(BaseBean mBaseBean) {
            ToastUtils.msg(mBaseBean.getResultStatus().getResultMessage().toString());
            dismiss();
            lp.alpha = 1.0f;
            window.setAttributes(lp);
            getInfoBySpecificPresenter.onStop();
            addGoodsCartPresenter.onStop();
        }

        @Override
        public void onError(String result) {
            ToastUtils.msg(result);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_close:
                dismiss();
                lp.alpha = 1.0f;
                window.setAttributes(lp);
                getInfoBySpecificPresenter.onStop();
                addGoodsCartPresenter.onStop();
                break;
            case R.id.tv_remove_num://减少
                if (num > 1)
                    num--;
                tv_exchange_num.setText("" + num);
                tvSouvenirPrice.setText("" + num * salePrice);
                break;
            case R.id.tv_add_num://增加
                if (num >= remain) {
                    ToastUtils.msg("已达最大库存");
                    return;
                }
                num++;
                tv_exchange_num.setText("" + num);
                tvSouvenirPrice.setText("" + num * salePrice);
                break;
        }
    }

    /**
     * 显示popwindow
     */
    public void showPopupWindow(View view) {
        if (!isShowing()) {
//            MyPopUpWindow popupWindow=new MyPopUpWindow();
//            popupWindow.showAsDropDown(view);
//            this.showAsDropDown(view);
            this.showAtLocation(view, Gravity.CENTER, 0, 0);
        }

    }

    public GoodSpecBean.GoodsSkuListBean getSpec(String goodsName) {//获取下一个接口的请求参数
        goodsName = "颜色:红色,尺码:M";
//goodsSkuList
        String[] specific = goodsName.split(",");
        GoodSpecBean.GoodsSkuListBean resultSku = null;
        for (int i = 0; i < getSpecificsBean.getValue().getGoodsSkuList().size(); i++) {
            GoodSpecBean.GoodsSkuListBean tmKindGoodsSkuInfo = getSpecificsBean.getValue().getGoodsSkuList().get(i);
            boolean flag = true;
            for (int j = 0; j < specific.length; j++) {
                if (tmKindGoodsSkuInfo.getSkuName().indexOf(specific[j]) == -1) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                resultSku = tmKindGoodsSkuInfo;
                break;
            }
        }
        return resultSku;
    }

    private List<String> getString() {//[颜色-红色;黄色, 尺码-M;L;XL]
        List<String> nameList = new ArrayList<>();
        for (GoodSpecBean.ParentSpecListBean parentSpec : getSpecificsBean.getValue().getParentSpecList()) {
            StringBuilder name = new StringBuilder();
            name.append(parentSpec.getName()).append("-");
            for (GoodSpecBean.ParentSpecListBean.ChildSpecListBean childSpec : parentSpec.getChildSpecList()) {
                name.append(childSpec.getName()).append(";");
            }
            nameList.add(name.substring(0, name.toString().length() - 1));
        }
        return nameList;
    }


}
