package com.guidemachine.service.view;


import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.ShopMarkersBean;
import com.guidemachine.service.entity.WeatherBean;

import java.util.List;


public interface ShopMarkersView extends View {
    void onSuccess(BaseBean<ShopMarkersBean> mShopMarkersBean);

    void onError(String result);
}
