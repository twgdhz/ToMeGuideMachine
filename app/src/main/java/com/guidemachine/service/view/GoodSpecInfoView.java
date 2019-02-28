package com.guidemachine.service.view;


import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.GoodSpecBean;
import com.guidemachine.service.entity.GoodsInfoDetailBean;


public interface GoodSpecInfoView extends View {
    void onSuccess(BaseBean<GoodSpecBean> mGoodSpecBean);

    void onError(String result);
}
