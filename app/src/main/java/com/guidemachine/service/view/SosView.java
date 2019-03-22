package com.guidemachine.service.view;

import com.guidemachine.service.entity.BaseBean;
public interface SosView extends View{
    void onSuccess(String msg,String flag);

    void onError(String result);
}
