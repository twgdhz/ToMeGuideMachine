package com.guidemachine.service.view;


import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.FenceBean;
import com.guidemachine.service.entity.LinkManListBean;

import java.util.List;


public interface LinkListView extends View {
    void onSuccess(BaseBean<List<LinkManListBean>> mFenceBeanList);

    void onError(String result);
}
