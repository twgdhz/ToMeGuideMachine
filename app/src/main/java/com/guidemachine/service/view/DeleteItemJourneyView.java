package com.guidemachine.service.view;


import com.guidemachine.service.entity.BaseBean;


public interface DeleteItemJourneyView extends View {
    void onSuccess(BaseBean mBaseBean);

    void onError(String result);
}
