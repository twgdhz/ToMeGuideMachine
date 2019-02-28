package com.guidemachine.service.view;


import com.guidemachine.service.entity.BaseBean;

/**
 * Created by win764-1 on 2016/12/12.
 */

public interface BookView extends View {
    void onSuccess(BaseBean mBaseBean);
    void onError(String result);
}
