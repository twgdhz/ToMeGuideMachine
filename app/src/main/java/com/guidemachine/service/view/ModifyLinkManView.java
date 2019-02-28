package com.guidemachine.service.view;


import com.guidemachine.service.entity.BaseBean;

/**
 * 基类接口
 */

public interface ModifyLinkManView extends View {
    void onSuccess(BaseBean mBaseBean);

    void onError(String result);
}
