package com.guidemachine.ui.guide.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.guidemachine.R;
import com.guidemachine.base.ui.BaseFragment;
import com.guidemachine.constant.Constants;
import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.CheckGuidePhoneBean;
import com.guidemachine.service.entity.GetCodeBean;
import com.guidemachine.service.entity.LoginBean;
import com.guidemachine.service.presenter.CheckGuidePhonePresenter;
import com.guidemachine.service.presenter.GetCodePresenter;
import com.guidemachine.service.presenter.GuideLoginPresenter;
import com.guidemachine.service.presenter.LoginPresenter;
import com.guidemachine.service.presenter.TeamAccountLoginPresenter;
import com.guidemachine.service.view.CheckGuidePhoneView;
import com.guidemachine.service.view.GetCodeView;
import com.guidemachine.service.view.GuideLoginView;
import com.guidemachine.service.view.LoginView;
import com.guidemachine.ui.activity.LoginActivity;
import com.guidemachine.ui.admin.AdminMainActivity;
import com.guidemachine.ui.guide.GuideMainActivity;
import com.guidemachine.ui.superadmin.SuperAdminMainActivity;
import com.guidemachine.ui.view.TimerView;
import com.guidemachine.util.IntentUtils;
import com.guidemachine.util.Logger;
import com.guidemachine.util.MobileInfoUtil;
import com.guidemachine.util.PhoneNumberUtils;
import com.guidemachine.util.ToastUtils;
import com.guidemachine.util.share.SPHelper;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * A simple {@link Fragment} subclass.
 */
public class GuideLoginFragment extends BaseFragment {


    @BindView(R.id.et_unser_name)
    EditText etUnserName;
    @BindView(R.id.rl_name)
    RelativeLayout rlName;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.rl_pwd)
    RelativeLayout rlPwd;
    @BindView(R.id.ll_guide_login)
    LinearLayout llGuideLogin;
    @BindView(R.id.cs_guide_pwd_login)
    ConstraintLayout csGuidepwdLogin;
    @BindView(R.id.tv_code_login)
    TextView tvCodeLogin;
    @BindView(R.id.cs_guide_phone_login)
    ConstraintLayout csGuidePhoneLogin;
    @BindView(R.id.rl_account_name)
    RelativeLayout rllAccountName;
    @BindView(R.id.et_account__name)
    EditText etAccountName;
    @BindView(R.id.rl_code)
    RelativeLayout rlCode;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.ll_guide_code_login)
    LinearLayout llGuideCodeLogin;
    @BindView(R.id.tv_account_login)
    TextView tvAccountLogin;
    Unbinder unbinder;
    @BindView(R.id.tv_get_code)
    TextView tvGetCode;
    TimerView timerView;
    //团账号
    TeamAccountLoginPresenter teamAccountLoginPresenter;
    //获取验证码
    GetCodePresenter getCodePresenter;
    //验证是否是 导游电话
    CheckGuidePhonePresenter checkGuidePhonePresenter;
    //导游登录
    GuideLoginPresenter guideLoginPresenter;

    public GuideLoginFragment() {
        // Required empty public constructor
    }


    @Override
    protected int setRootViewId() {
        return R.layout.fragment_guide_login;
    }

    @Override
    protected void initView(View view, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        teamAccountLoginPresenter = new TeamAccountLoginPresenter(getContext());
        teamAccountLoginPresenter.onCreate();
        teamAccountLoginPresenter.attachView(loginView);

        getCodePresenter = new GetCodePresenter(getContext());
        getCodePresenter.onCreate();
        getCodePresenter.attachView(baseView);

        checkGuidePhonePresenter = new CheckGuidePhonePresenter(getContext());
        checkGuidePhonePresenter.onCreate();
        checkGuidePhonePresenter.attachView(checkGuidePhoneView);

        guideLoginPresenter = new GuideLoginPresenter(getContext());
        guideLoginPresenter.onCreate();
        guideLoginPresenter.attachView(guideLoginView);

        timerView = new TimerView(tvGetCode);

        llGuideLogin.setOnClickListener(new View.OnClickListener() {//导游账号登录
            @Override
            public void onClick(View view) {
                JSONObject requestData = new JSONObject();
                try {
                    requestData.put("account", etUnserName.getText().toString());
                    requestData.put("password", etPassword.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestData.toString());
                teamAccountLoginPresenter.teamAccountLogin(requestBody);
                showProgressDialog();
            }
        });
        llGuideCodeLogin.setOnClickListener(new View.OnClickListener() {//验证码登录
            @Override
            public void onClick(View view) {
                JSONObject loginRequestData = new JSONObject();
                try {
                    loginRequestData.put("codeId", SPHelper.getInstance(getContext()).getCodeId());
                    loginRequestData.put("code", etCode.getText().toString());
                    loginRequestData.put("phone", etAccountName.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody loginRequestBody = RequestBody.create(MediaType.parse("application/json"), loginRequestData.toString());
                guideLoginPresenter.loginGuide(loginRequestBody);
                showProgressDialog();
            }
        });
        tvCodeLogin.setOnClickListener(new View.OnClickListener() {//切换成验证码登录
            @Override
            public void onClick(View view) {
                csGuidepwdLogin.setVisibility(View.GONE);
                csGuidePhoneLogin.setVisibility(View.VISIBLE);
            }
        });
        tvAccountLogin.setOnClickListener(new View.OnClickListener() {//切换成团账号密码登录
            @Override
            public void onClick(View view) {
                csGuidepwdLogin.setVisibility(View.VISIBLE);
                csGuidePhoneLogin.setVisibility(View.GONE);
            }
        });

        tvGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!PhoneNumberUtils.isMobile(etAccountName.getText().toString())) {//手机号不合法
                    Toast.makeText(getActivity(),
                            R.string.login_illegal_mobile,
                            Toast.LENGTH_LONG).show();
                    return;
                }
                Logger.d("etAccountName    ", etAccountName.getText().toString());
                ToastUtils.msg(etAccountName.getText().toString());
                checkGuidePhonePresenter.checkGuidePhone(etAccountName.getText().toString());
                showProgressDialog();

            }
        });
    }

    GuideLoginView guideLoginView = new GuideLoginView() {
        @Override
        public void onSuccess(BaseBean<LoginBean> mGetCodeBean) {
            dismissProgressDialog();
            ToastUtils.msg(mGetCodeBean.getResultStatus().getResultMessage().toString());
            SPHelper.getInstance(getContext()).setToken("Bearer" + " " + mGetCodeBean.getValue().getAccess_token());
            IntentUtils.openActivity(getActivity(), GuideMainActivity.class);
            getActivity().finish();
        }

        @Override
        public void onError(String result) {
            dismissProgressDialog();
            ToastUtils.msg(result);
        }
    };
    LoginView loginView = new LoginView() {
        @Override
        public void onSuccess(BaseBean<LoginBean> mGetCodeBean) {
            dismissProgressDialog();
            Logger.d("登录成功", mGetCodeBean.toString());
            ToastUtils.msg("登录成功");
            //            SPHelper.getInstance(LoginActivity.this).setToken(mLoginBean.getValue().getToken_type() + " " + mLoginBean.getValue().getAccess_token());
            SPHelper.getInstance(getContext()).setToken("Bearer" + " " + mGetCodeBean.getValue().getAccess_token());
            Logger.d("登录成功 token", SPHelper.getInstance(getContext()).getToken());
            IntentUtils.openActivity(getActivity(), GuideMainActivity.class);
        }

        @Override
        public void onError(String result) {
            dismissProgressDialog();
            if (result.equals(Constants.RE_CODE_0003)) {
                ToastUtils.msg("账号或密码错误");
            }else {
                ToastUtils.msg(result);
            }
        }
    };

    CheckGuidePhoneView checkGuidePhoneView = new CheckGuidePhoneView() {//判断是否是导游电话号码
        @Override
        public void onSuccess(BaseBean<CheckGuidePhoneBean> mBaseBean) {//是的话则请求验证码
            dismissProgressDialog();
            SPHelper.getInstance(getContext()).setUserId(mBaseBean.getValue().getId());
            JSONObject requestData = new JSONObject();
            try {
                requestData.put("phone", etAccountName.getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestData.toString());
            getCodePresenter.getCode(requestBody);
            showProgressDialog();
        }

        @Override
        public void onError(String result) {
            dismissProgressDialog();
            ToastUtils.msg(result);
        }
    };
    GetCodeView baseView = new GetCodeView() {
        @Override
        public void onSuccess(BaseBean<GetCodeBean> mBaseBean) {
            dismissProgressDialog();
            timerView.start();
            Logger.d(mBaseBean.toString());
            SPHelper.getInstance(getContext()).setCodeId(mBaseBean.getValue().getKey());
        }

        @Override
        public void onError(String result) {
            dismissProgressDialog();
            ToastUtils.msg(result);
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
