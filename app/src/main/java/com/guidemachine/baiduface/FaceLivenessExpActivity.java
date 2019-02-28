package com.guidemachine.baiduface;

import android.content.DialogInterface;
import android.os.Bundle;

import com.baidu.idl.face.platform.FaceStatusEnum;
import com.baidu.idl.face.platform.ui.FaceLivenessActivity;
import com.guidemachine.R;
import com.guidemachine.baiduface.widget.DefaultDialog;
import com.guidemachine.ui.activity.LoginActivity;
import com.guidemachine.ui.activity.SceneAlbumActivity;
import com.guidemachine.util.IntentUtils;
import com.guidemachine.util.StatusBarUtils;
import com.guidemachine.util.ToastUtils;

import java.util.HashMap;

/**
 * 百度人脸识别
 */
public class FaceLivenessExpActivity extends FaceLivenessActivity {

    private DefaultDialog mDefaultDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onLivenessCompletion(FaceStatusEnum status, String message, HashMap<String, String> base64ImageMap) {
        super.onLivenessCompletion(status, message, base64ImageMap);
        StatusBarUtils.setWindowStatusBarColor(FaceLivenessExpActivity.this, R.color.white);
        if (status == FaceStatusEnum.OK && mIsCompletion) {
//            showMessageDialog("活体检测", "检测成功");
            ToastUtils.msg("检测完成！");
            IntentUtils.openActivity(FaceLivenessExpActivity.this, LoginActivity.class, "type", "1");
            finish();
        } else if (status == FaceStatusEnum.Error_DetectTimeout ||
                status == FaceStatusEnum.Error_LivenessTimeout ||
                status == FaceStatusEnum.Error_Timeout) {
            showMessageDialog("人脸识别", "采集超时");
        }
    }

    private void showMessageDialog(String title, String message) {
        if (mDefaultDialog == null) {
            DefaultDialog.Builder builder = new DefaultDialog.Builder(this);
            builder.setTitle(title).
                    setMessage(message).
                    setNegativeButton("确认",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mDefaultDialog.dismiss();
                                    finish();
                                }
                            });
            mDefaultDialog = builder.create();
            mDefaultDialog.setCancelable(true);
        }
        mDefaultDialog.dismiss();
        mDefaultDialog.show();
    }

    @Override
    public void finish() {
        super.finish();
    }
}
