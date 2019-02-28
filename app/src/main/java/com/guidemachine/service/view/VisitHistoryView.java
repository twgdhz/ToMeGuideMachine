package com.guidemachine.service.view;


import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.VisitHistoryBean;

import java.util.List;


public interface VisitHistoryView extends View {
    void onSuccess(BaseBean<List<VisitHistoryBean>> mVisitHistoryBean);

    void onError(String result);
}
