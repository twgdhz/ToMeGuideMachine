package com.guidemachine.service.view;


import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.CreateOrderRefundReserveBean;

public interface OrderRefundView extends View {

    void onSuccess(BaseBean<CreateOrderRefundReserveBean> bean);
    void onError(String result);
}
