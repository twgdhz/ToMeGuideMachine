package com.guidemachine.service.view;


import com.guidemachine.service.entity.AdminStatisticsBean;
import com.guidemachine.service.entity.BaseBean;

import java.util.List;


public interface AdminStatisticsView extends View {
    void onSuccess(BaseBean<List<AdminStatisticsBean>> mStatisticsBeanList);

    void onError(String result);
}
