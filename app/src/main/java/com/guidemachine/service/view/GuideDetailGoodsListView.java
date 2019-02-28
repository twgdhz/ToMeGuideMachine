package com.guidemachine.service.view;

import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.BuyAgainBean;

import java.util.List;

public interface GuideDetailGoodsListView extends View {

    void onSuccess(BaseBean<List<BuyAgainBean>> list);
    void onError(String result);
}
