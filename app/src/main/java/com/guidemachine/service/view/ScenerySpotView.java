package com.guidemachine.service.view;


import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.LoginBean;
import com.guidemachine.service.entity.ScenerySpotListBean;


public interface ScenerySpotView extends View {
    void onSuccess(BaseBean<ScenerySpotListBean> mScenerySpotListBean);

    void onError(String result);
}
