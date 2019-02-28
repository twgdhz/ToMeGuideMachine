package com.guidemachine.service.view;

import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.SelectOrderBean;

public interface SelectOrdersView extends View {

    void onSuccess(BaseBean<SelectOrderBean> bean);
    void onError(String result);
}
