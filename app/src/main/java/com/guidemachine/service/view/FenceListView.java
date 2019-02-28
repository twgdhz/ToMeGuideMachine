package com.guidemachine.service.view;


import com.guidemachine.service.entity.AdminStatisticsBean;
import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.FenceBean;

import java.util.List;


public interface FenceListView extends View {
    void onSuccess(BaseBean<List<FenceBean>> mFenceBeanList);

    void onError(String result);
}
