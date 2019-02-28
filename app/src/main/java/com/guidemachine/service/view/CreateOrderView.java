package com.guidemachine.service.view;


import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.CreateOrderBean;

public interface CreateOrderView extends View {

    void onSuccess(BaseBean<CreateOrderBean> bean);
    void onError(String result);
}
