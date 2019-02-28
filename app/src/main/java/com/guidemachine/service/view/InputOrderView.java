package com.guidemachine.service.view;


import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.InputOrderBean;

public interface InputOrderView extends View {

    void onSuccess(BaseBean<InputOrderBean> bean);
    void onError(String result);
}
