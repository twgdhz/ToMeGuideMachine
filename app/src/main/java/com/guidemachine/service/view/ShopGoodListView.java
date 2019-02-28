package com.guidemachine.service.view;


import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.ShopBasicInfoBean;
import com.guidemachine.service.entity.ShopGoodListBean;


public interface ShopGoodListView extends View {
    void onSuccess(BaseBean<ShopGoodListBean> mShopGoodListBean);

    void onError(String result);
}
