package com.guidemachine.service.view;


import com.guidemachine.service.entity.AdminDeviceRentNumBean;
import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.CreateOrderRefundReserveBean;

public interface AdminDeviceRentNumView extends View {

    void onSuccess(BaseBean<AdminDeviceRentNumBean> bean);
    void onError(String result);
}
