package com.guidemachine.service.view;


import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.GoodSpecBean;


public interface AddGoodCartView extends View {
    void onSuccess(BaseBean mBaseBean);

    void onError(String result);
}
