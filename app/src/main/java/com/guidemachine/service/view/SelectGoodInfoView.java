package com.guidemachine.service.view;


import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.GoodsInfoDetailBean;
import com.guidemachine.service.entity.ShopGoodListBean;


public interface SelectGoodInfoView extends View {
    void onSuccess(BaseBean<GoodsInfoDetailBean> mGoodsInfoDetailBean);

    void onError(String result);
}
