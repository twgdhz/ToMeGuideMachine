package com.guidemachine.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.guidemachine.R;
import com.guidemachine.base.ui.BaseActivity;
import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.GetCodeBean;
import com.guidemachine.service.entity.LoginBean;
import com.guidemachine.service.presenter.GetCodePresenter;
import com.guidemachine.service.presenter.LoginPresenter;
import com.guidemachine.service.view.GetCodeView;
import com.guidemachine.service.view.LoginView;
import com.guidemachine.ui.activity.chat.ChatActivity;
import com.guidemachine.ui.activity.chat.ChatLoginActivity;
import com.guidemachine.ui.view.CustomAlertDialog;
import com.guidemachine.ui.view.TimerView;
import com.guidemachine.ui.view.TourTimerView;
import com.guidemachine.util.IntentUtils;
import com.guidemachine.util.Logger;
import com.guidemachine.util.MobileInfoUtil;
import com.guidemachine.util.PhoneNumberUtils;
import com.guidemachine.util.SoundManager;
import com.guidemachine.util.ToastUtils;
import com.guidemachine.util.share.SPHelper;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.exceptions.HyphenateException;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.ll_ensure)
    LinearLayout llEnsure;
    @BindView(R.id.ll_cancel)
    LinearLayout llCancel;
    @BindView(R.id.tv_get_code)
    TextView tvGetCode;
    TourTimerView timerView;
    GetCodePresenter getCodePresenter;
    @BindView(R.id.et_phone)
    EditText etPhone;
    LoginPresenter loginPresenter;
    @BindView(R.id.et_code)
    EditText etCode;
    String type;

    @Override
    protected int setRootViewId() {
        return R.layout.activity_login;
    }

    @Override
    protected boolean setIsFull() {
        return false;
    }

    @Override
    protected void InitialView() {
        Add(this);
        setTranslucentStatus();
        type = getIntent().getExtras().getString("type");
        timerView = new TourTimerView(tvGetCode);
        tvGetCode.setOnClickListener(this);
        llEnsure.setOnClickListener(this);
        llCancel.setOnClickListener(this);
        getCodePresenter = new GetCodePresenter(LoginActivity.this);
        getCodePresenter.onCreate();
        getCodePresenter.attachView(baseView);
        loginPresenter = new LoginPresenter(LoginActivity.this);
        loginPresenter.onCreate();
        loginPresenter.attachView(loginView);
//        SoundManager.getInstence().initMusic(LoginActivity.this);
       // EditText禁止输空格+换行键
        etPhone.setFilters(new InputFilter[]{filter});
        etCode.setFilters(new InputFilter[]{filter});

    }
    private InputFilter filter=new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if(source.equals(" ")||source.toString().contentEquals("\n"))return "";
            else return null;
        }
    };
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_get_code:
                if (!PhoneNumberUtils.isMobile(etPhone.getText().toString())) {//手机号不合法
                    Toast.makeText(LoginActivity.this,
                            R.string.login_illegal_mobile,
                            Toast.LENGTH_LONG).show();
                    return;
                }
                JSONObject requestData = new JSONObject();
                try {
                    requestData.put("phone", etPhone.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestData.toString());
                getCodePresenter.getCode(requestBody);
                showProgressDialog();
                break;
            case R.id.ll_ensure:
                JSONObject loginRequestData = new JSONObject();
                try {
                    loginRequestData.put("id", SPHelper.getInstance(LoginActivity.this).getUserId());
                    loginRequestData.put("codeId", SPHelper.getInstance(LoginActivity.this).getCodeId());
                    loginRequestData.put("code", etCode.getText().toString());
                    loginRequestData.put("phone", etPhone.getText().toString());
                    loginRequestData.put("imei", MobileInfoUtil.getIMEI(LoginActivity.this));
                    Logger.d("登录成功", etCode.getText().toString() + "  " + etPhone.getText().toString()+ "   id"+
                            SPHelper.getInstance(LoginActivity.this).getUserId());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody loginRequestBody = RequestBody.create(MediaType.parse("application/json"), loginRequestData.toString());
                loginPresenter.login(loginRequestBody);
                showProgressDialog();
                break;
            case R.id.ll_cancel:
                CustomAlertDialog.Builder builder =
                        new CustomAlertDialog.Builder(LoginActivity.this);
                builder.setMessage("")
                        .setNegativeButton("", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                if (type.equals("1")) {//正常页面登录
                                    IntentUtils.openActivity(LoginActivity.this, GuideActivity.class);
                                    finish();
                                } else if (type.equals("2")) {//聊天登录
                                    IntentUtils.openActivity(LoginActivity.this, MapActivity.class);
                                    finish();
                                }
                            }
                        })
                        .create().show();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    GetCodeView baseView = new GetCodeView() {
        @Override
        public void onSuccess(BaseBean<GetCodeBean> mBaseBean) {
            dismissProgressDialog();
            Logger.d("获取验证码", mBaseBean.toString());
            timerView.start();
            SPHelper.getInstance(LoginActivity.this).setCodeId(mBaseBean.getValue().getKey());
            Logger.d(mBaseBean.toString());
        }

        @Override
        public void onError(String result) {
            dismissProgressDialog();
            ToastUtils.msg(result);
        }
    };
    LoginView loginView = new LoginView() {
        @Override
        public void onSuccess(BaseBean<LoginBean> mLoginBean) {
            dismissProgressDialog();
//            register();
            ToastUtils.msg(mLoginBean.getResultStatus().getResultMessage().toString());
            Logger.d("登录成功", mLoginBean.toString());
//            SPHelper.getInstance(LoginActivity.this).setToken(mLoginBean.getValue().getToken_type() + " " + mLoginBean.getValue().getAccess_token());
            SPHelper.getInstance(LoginActivity.this).setToken("Bearer" + " " + mLoginBean.getValue().getAccess_token());
            Logger.d("登录成功 token", SPHelper.getInstance(LoginActivity.this).getToken());
            if (type.equals("1")) {//正常页面登录
                IntentUtils.openActivity(LoginActivity.this, GuideActivity.class);
                finish();
                FinishAll();
                register();
            } else if (type.equals("2")) {//聊天登录
                register();
            }
        }

        @Override
        public void onError(String result) {
            dismissProgressDialog();
            Logger.d("登录失败：", result);
            ToastUtils.msg(result);
        }
    };

    private void register() {//环信注册
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().createAccount(etPhone.getText().toString().trim(), etPhone.getText().toString().trim());
                    Log.d("TalkFragment", "注册成功");
                    SPHelper.getInstance(LoginActivity.this).setPhone(etPhone.getText().toString().trim());
                    login();
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    Log.e("TalkFragment", "注册失败" + e.getMessage());
                    if (e.getMessage().equals("User already exist")) {//如果已经注册过了
                        login();
                    }
                }
            }
        }).start();
    }

    //登录的方法
    private void login() {
        EMClient.getInstance().login(etPhone.getText().toString().trim(), etPhone.getText().toString().trim(), new EMCallBack() {
            @Override
            public void onSuccess() {
                Log.d("TalkFragment", "Login Success");
                SPHelper.getInstance(LoginActivity.this).setPhone(etPhone.getText().toString().trim());
                if (type.equals("2")) {//聊天登录
                    startActivity(new Intent(LoginActivity.this, ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, etPhone.getText().toString().trim()));
                    finish();
                    FinishAll();
                }
            }

            @Override
            public void onError(int i, String s) {
                Log.d("TalkFragment", i + " " + s.toString());

            }

            @Override
            public void onProgress(int i, String s) {

            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        loginPresenter.onStop();
        getCodePresenter.onStop();
    }
}
