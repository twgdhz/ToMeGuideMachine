package com.guidemachine.service.view;


import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.KindOrderFillingBean;

public interface KindOrderFillingView extends View  {
    void onSuccess(BaseBean<KindOrderFillingBean> mBaseBean);
    void onError(String result);
}
