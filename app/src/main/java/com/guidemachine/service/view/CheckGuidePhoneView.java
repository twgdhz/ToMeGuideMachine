package com.guidemachine.service.view;


import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.CheckGuidePhoneBean;


public interface CheckGuidePhoneView extends View {
    void onSuccess(BaseBean<CheckGuidePhoneBean> mBaseBean);

    void onError(String result);
}
