package com.guidemachine.service.view;


import com.guidemachine.service.entity.AreaSourceBean;
import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.LoginBean;


public interface AreaSourceView extends View {
    void onSuccess(BaseBean<AreaSourceBean> mGetCodeBean);

    void onError(String result);
}
