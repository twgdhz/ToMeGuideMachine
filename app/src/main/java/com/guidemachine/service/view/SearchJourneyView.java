package com.guidemachine.service.view;


import com.guidemachine.service.entity.AdminTouristTeamBean;
import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.SearchJourneyBean;

import java.util.List;


public interface SearchJourneyView extends View {
    void onSuccess(BaseBean<List<SearchJourneyBean>> mSearchJourneyList);

    void onError(String result);
}
