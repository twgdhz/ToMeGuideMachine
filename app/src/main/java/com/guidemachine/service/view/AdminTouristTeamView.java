package com.guidemachine.service.view;


import com.guidemachine.service.entity.AdminStatisticsBean;
import com.guidemachine.service.entity.AdminTouristTeamBean;
import com.guidemachine.service.entity.BaseBean;

import java.util.List;


public interface AdminTouristTeamView extends View {
    void onSuccess(BaseBean<List<AdminTouristTeamBean>> mStatisticsBeanList);

    void onError(String result);
}
