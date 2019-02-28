package com.guidemachine.service.view;


import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.SceneryServiceInfoDetail;
import com.guidemachine.service.entity.TeamLocationBean;

public interface GuideTeamLocationView extends View {

    void onSuccess(BaseBean<TeamLocationBean> bean);
    void onError(String result);
}
