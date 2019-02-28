package com.guidemachine.service.view;



import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.FaceRegisterBean;

import java.util.List;

/**
 * Created by win764-1 on 2016/12/12.
 */

public interface AllFaceView extends View {
    void onSuccess(BaseBean<List<FaceRegisterBean>> mList);
    void onError(String result);
}
