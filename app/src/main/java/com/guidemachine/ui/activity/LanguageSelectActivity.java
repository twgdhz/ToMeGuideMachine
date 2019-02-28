package com.guidemachine.ui.activity;

import android.Manifest;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.App;
import com.baidu.idl.face.platform.FaceConfig;
import com.baidu.idl.face.platform.FaceEnvironment;
import com.baidu.idl.face.platform.FaceSDKManager;
import com.baidu.idl.face.platform.LivenessTypeEnum;
import com.guidemachine.R;
import com.guidemachine.baiduface.Config;
import com.guidemachine.baiduface.FaceLivenessExpActivity;
import com.guidemachine.base.ui.BaseActivity;
import com.guidemachine.util.IntentUtils;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * @author ChenLinWang
 * @email 422828518@qq.com
 * @create 2018/11/12 0012 15:05
 * description: 语言选择
 */
public class LanguageSelectActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {

    @BindView(R.id.ll_language_ensure)
    LinearLayout llLanguageEnsure;
    @BindView(R.id.rg_chinese)
    RadioButton rgChinese;
    @BindView(R.id.rg_english)
    RadioButton rgEnglish;
    @BindView(R.id.bn_indonesia)
    RadioButton bnIndonesia;
    @BindView(R.id.bn_france)
    RadioButton bnFrance;
    @BindView(R.id.tb_rg)
    RadioGroup tbRg;
    String language = "cn";
    String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private static final int RC_CAMERA_AND_LOCATION = 00000;
    @Override
    protected int setRootViewId() {
        return R.layout.activity_language_select;
    }

    @Override
    protected boolean setIsFull() {
        return false;
    }

    @Override
    protected void InitialView() {
        setTranslucentStatus();
//        rgChinese.setChecked(true);
        tbRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
                if (checkId == R.id.rg_chinese) {
                    language = "cn";
                    IntentUtils.openActivity(LanguageSelectActivity.this, FaceLivenessExpActivity.class);
                    finish();
                } else if (checkId == R.id.rg_english) {
                    language = "en";
                    IntentUtils.openActivity(LanguageSelectActivity.this, FaceLivenessExpActivity.class);
                    finish();
                } else if (checkId == R.id.bn_indonesia) {
                    language = "cn";
                    IntentUtils.openActivity(LanguageSelectActivity.this, FaceLivenessExpActivity.class);
                    finish();
                } else if (checkId == R.id.bn_france) {
                    language = "cn";
                    IntentUtils.openActivity(LanguageSelectActivity.this, FaceLivenessExpActivity.class);
                    finish();
                }
            }
        });
        llLanguageEnsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchLanguage(language);
//                IntentUtils.openActivity(LanguageSelectActivity.this, LoginActivity.class);
//                IntentUtils.openActivity(LanguageSelectActivity.this, RegisterAndRecognizeActivity.class);
                IntentUtils.openActivity(LanguageSelectActivity.this, FaceLivenessExpActivity.class);
                finish();
            }
        });

        // 根据需求添加活体动作
        App.livenessList.clear();
        App.livenessList.add(LivenessTypeEnum.Eye);
//        App.livenessList.add(LivenessTypeEnum.Mouth);
//        App.livenessList.add(LivenessTypeEnum.HeadUp);
//        App.livenessList.add(LivenessTypeEnum.HeadDown);
//        App.livenessList.add(LivenessTypeEnum.HeadLeft);
//        App.livenessList.add(LivenessTypeEnum.HeadRight);
//        App.livenessList.add(LivenessTypeEnum.HeadLeftOrRight);
        initLib();
        setFaceConfig();

    }
    @Override
    protected void onResume() {
        super.onResume();
        methodRequiresTwoPermission();

    }
    @AfterPermissionGranted(RC_CAMERA_AND_LOCATION)
    private void methodRequiresTwoPermission() {
        if (EasyPermissions.hasPermissions(this, permissions)) {
            // Already have permission, do the thing
            // ...
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, "",RC_CAMERA_AND_LOCATION, permissions);
        }
    }

    //切换方法
    private void switchLanguage(String language) {
        //获取资源
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        if (language.equals("cn")) {
            config.locale = Locale.CHINA;//方法过时但是一样可以使用
        } else if (language.equals("cn")) {
            config.setLocale(Locale.ENGLISH);//低版本无法使用,如果使用不了,就使用上面的方法
        }
        //更新,稍后提及此方法过时.
        resources.updateConfiguration(config, resources.getDisplayMetrics());
        //关闭页面,重新绘制
        finish();
//        startActivity(new Intent(LanguageSelectActivity.this, MainActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    /**
     * 初始化SDK
     */
    private void initLib() {
        // 为了android和ios 区分授权，appId=appname_face_android ,其中appname为申请sdk时的应用名
        // 应用上下文
        // 申请License取得的APPID
        // assets目录下License文件名
        FaceSDKManager.getInstance().initialize(this, Config.licenseID, Config.licenseFileName);
        // setFaceConfig();
    }

    private void setFaceConfig() {
        FaceConfig config = FaceSDKManager.getInstance().getFaceConfig();
        // SDK初始化已经设置完默认参数（推荐参数），您也根据实际需求进行数值调整
        config.setLivenessTypeList(App.livenessList);
        config.setLivenessRandom(App.isLivenessRandom);
        config.setBlurnessValue(FaceEnvironment.VALUE_BLURNESS);
        config.setBrightnessValue(FaceEnvironment.VALUE_BRIGHTNESS);
        config.setCropFaceValue(FaceEnvironment.VALUE_CROP_FACE_SIZE);
        config.setHeadPitchValue(FaceEnvironment.VALUE_HEAD_PITCH);
        config.setHeadRollValue(FaceEnvironment.VALUE_HEAD_ROLL);
        config.setHeadYawValue(FaceEnvironment.VALUE_HEAD_YAW);
        config.setMinFaceSize(FaceEnvironment.VALUE_MIN_FACE_SIZE);
        config.setNotFaceValue(FaceEnvironment.VALUE_NOT_FACE_THRESHOLD);
        config.setOcclusionValue(FaceEnvironment.VALUE_OCCLUSION);
        config.setCheckFaceQuality(true);
        config.setFaceDecodeNumberOfThreads(2);
        FaceSDKManager.getInstance().setFaceConfig(config);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        // Forward results to EasyPermissions
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
//            new AppSettingsDialog.Builder(this)
//                    .setRationale("拒绝功能可能导致APP无法使用，请到设置中授予权限")
//                    .build().show();
        }
    }
}
