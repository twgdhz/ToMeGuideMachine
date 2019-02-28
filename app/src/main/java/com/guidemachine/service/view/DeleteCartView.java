package com.guidemachine.service.view;


import com.guidemachine.service.entity.BaseBean;

public interface DeleteCartView extends View  {
    void onSuccess(BaseBean mBaseBean);
    void onError(String result);
}
