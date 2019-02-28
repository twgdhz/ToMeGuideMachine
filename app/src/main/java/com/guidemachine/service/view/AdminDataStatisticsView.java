package com.guidemachine.service.view;


import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.SceneryServiceInfoDetail;

public interface AdminDataStatisticsView extends View {

    void onSuccess(BaseBean<SceneryServiceInfoDetail> bean);
    void onError(String result);
}
