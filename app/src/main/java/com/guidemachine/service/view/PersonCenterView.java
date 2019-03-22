package com.guidemachine.service.view;

import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.PersonCenterBean;

public interface PersonCenterView extends View {

    void onSuccess(BaseBean<PersonCenterBean> bean);
    void onError(String result);
}
