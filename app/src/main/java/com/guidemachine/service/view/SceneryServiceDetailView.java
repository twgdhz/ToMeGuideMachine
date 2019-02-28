package com.guidemachine.service.view;


import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.SceneryServiceInfoBean;
import com.guidemachine.service.entity.SceneryServiceInfoDetail;

public interface SceneryServiceDetailView extends View {

    void onSuccess(BaseBean<SceneryServiceInfoDetail> bean);
    void onError(String result);
}
