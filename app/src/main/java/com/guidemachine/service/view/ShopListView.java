package com.guidemachine.service.view;


import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.ShopListBean;


public interface ShopListView extends View {
    void onSuccess(BaseBean<ShopListBean> mBaseBean);

    void onError(String result);
}
