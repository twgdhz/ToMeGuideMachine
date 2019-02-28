package com.guidemachine.service.view;


import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.GetCodeBean;
import com.guidemachine.service.entity.GetSelfCodeBean;
import com.guidemachine.service.entity.WeatherBean;


public interface GetSelfQRCodeView extends View {
    void onSuccess(BaseBean<GetSelfCodeBean> mGetCodeBean);

    void onError(String result);
}
