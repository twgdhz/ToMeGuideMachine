package com.guidemachine.service.view;


import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.OrderDetailBean;

/**
 * 基类接口
 */

public interface OrderDetailView extends View {
    void onSuccess(BaseBean<OrderDetailBean> mBaseBean);

    void onError(String result);
}
