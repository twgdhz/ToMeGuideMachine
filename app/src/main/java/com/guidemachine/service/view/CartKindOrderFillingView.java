package com.guidemachine.service.view;


import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.CartKindOrderFillingBean;

import java.util.List;

public interface CartKindOrderFillingView extends View  {
    void onSuccess(BaseBean<List<CartKindOrderFillingBean>> mList);
    void onError(String result);
}
