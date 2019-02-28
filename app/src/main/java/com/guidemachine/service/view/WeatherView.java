package com.guidemachine.service.view;


import com.guidemachine.service.entity.AreaSourceBean;
import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.WeatherBean;


public interface WeatherView extends View {
    void onSuccess(BaseBean<WeatherBean> mGetCodeBean);

    void onError(String result);
}
