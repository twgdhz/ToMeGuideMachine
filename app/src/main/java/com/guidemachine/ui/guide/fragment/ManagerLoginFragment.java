package com.guidemachine.ui.guide.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.guidemachine.R;
import com.guidemachine.base.ui.BaseFragment;
import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.LoginBean;
import com.guidemachine.service.presenter.AdminLoginPresenter;
import com.guidemachine.service.view.LoginView;
import com.guidemachine.ui.admin.AdminMainActivity;
import com.guidemachine.ui.guide.GuideMainActivity;
import com.guidemachine.ui.superadmin.SuperAdminMainActivity;
import com.guidemachine.util.IntentUtils;
import com.guidemachine.util.Logger;
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
public class ManagerLoginFragment extends BaseFragment {


    @BindView(R.id.et_unser_name)
    EditText etUnserName;
    @BindView(R.id.rl_name)
    RelativeLayout rlName;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.rl_pwd)
    RelativeLayout rlPwd;
    @BindView(R.id.ll_manager_login)
    LinearLayout llManagerLogin;
    Unbinder unbinder;

    AdminLoginPresenter adminLoginPresenter;

    @Override
    protected int setRootViewId() {
        return R.layout.fragment_manager_login;
    }


    @Override
    protected void initView(View view, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        adminLoginPresenter=new AdminLoginPresenter(getContext());
        adminLoginPresenter.onCreate();
        adminLoginPresenter.attachView(loginView);
        llManagerLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                IntentUtils.openActivity(getActivity(), SuperAdminMainActivity.class);
//                IntentUtils.openActivity(getActivity(), AdminMainActivity.class);
                if (etUnserName.getText().toString().equals("") || etPassword.getText().equals("")) {
                    ToastUtils.msg("账号密码不能为空！");
                    return;
                }
                JSONObject requestData = new JSONObject();
                try {
                    requestData.put("username", etUnserName.getText().toString());
                    requestData.put("password", etPassword.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestData.toString());
                adminLoginPresenter.adminLogin(requestBody);
                showProgressDialog();
            }
        });

    }

    LoginView loginView = new LoginView() {
        @Override
        public void onSuccess(BaseBean<LoginBean> mGetCodeBean) {
            dismissProgressDialog();
            Logger.d("登录成功", mGetCodeBean.toString());
            ToastUtils.msg("登录成功");
            //            SPHelper.getInstance(LoginActivity.this).setToken(mLoginBean.getValue().getToken_type() + " " + mLoginBean.getValue().getAccess_token());
            SPHelper.getInstance(getContext()).setToken("Bearer" + " " + mGetCodeBean.getValue().getAccess_token());
            Logger.d("登录成功 token", SPHelper.getInstance(getContext()).getToken());
            if (mGetCodeBean.getResultStatus().getResultMessage().equals("admin")){//超级管理员
                IntentUtils.openActivity(getActivity(), SuperAdminMainActivity.class);
            }else {
                IntentUtils.openActivity(getActivity(), AdminMainActivity.class);
            }
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
