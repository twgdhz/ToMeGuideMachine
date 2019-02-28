package com.guidemachine.service.view;


import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.LoginBean;

import okhttp3.ResponseBody;


public interface QRCodePayView extends View {
    void onSuccess(ResponseBody mResponseBody);

    void onError(String result);
}
