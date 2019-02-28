package com.guidemachine.service.view;


import com.guidemachine.service.entity.AdminDeviceRentNumBean;
import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.SceneryServiceInfoBean;

public interface SceneryServiceInfoView extends View {

    void onSuccess(BaseBean<SceneryServiceInfoBean> bean);
    void onError(String result);
}
