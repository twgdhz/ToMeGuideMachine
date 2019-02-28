package com.guidemachine.service.view;


import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.GetCodeBean;


public interface GetCodeView extends View {
    void onSuccess(BaseBean<GetCodeBean> mGetCodeBean);

    void onError(String result);
}
