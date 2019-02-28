package com.guidemachine.service.view;


import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.FenceBean;

import java.util.List;

/**
 * 基类接口
 */

public interface FenceView extends View {
    void onSuccess(BaseBean<List<FenceBean>> mFenceBean);

    void onError(String result);
}
