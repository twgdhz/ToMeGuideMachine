package com.guidemachine.service.view;


import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.ShopBasicInfoBean;
import com.guidemachine.service.entity.ShopListBean;


public interface ShopBasicInfoView extends View {
    void onSuccess(BaseBean<ShopBasicInfoBean> mShopBasicInfoBean);

    void onError(String result);
}
