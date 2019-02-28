package com.guidemachine.service.view;


import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.LoginBean;


public interface GuideLoginView extends View {
    void onSuccess(BaseBean<LoginBean> mGetCodeBean);

    void onError(String result);
}
