package com.guidemachine.service.view;


import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.ShoppingCartListBean;

public interface GetShoppingCartListView extends View  {
    void onSuccess(BaseBean<ShoppingCartListBean> mShoppingCartListBean);
    void onError(String result);
}
