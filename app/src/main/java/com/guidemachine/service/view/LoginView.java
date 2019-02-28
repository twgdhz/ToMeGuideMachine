package com.guidemachine.service.view;


import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.GetCodeBean;
import com.guidemachine.service.entity.LoginBean;


public interface LoginView extends View {
    void onSuccess(BaseBean<LoginBean> mGetCodeBean);

    void onError(String result);
}
