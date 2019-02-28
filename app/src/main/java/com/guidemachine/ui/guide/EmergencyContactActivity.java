package com.guidemachine.ui.guide;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.guidemachine.R;
import com.guidemachine.base.ui.BaseActivity;
import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.presenter.GuideAddLinkManPresenter;
import com.guidemachine.service.presenter.ModifyLinkManPresenter;
import com.guidemachine.service.view.BaseView;
import com.guidemachine.service.view.ModifyLinkManView;
import com.guidemachine.util.PhoneNumberUtils;
import com.guidemachine.util.StatusBarUtils;
import com.guidemachine.util.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * @author ChenLinWang
 * @email 422828518@qq.com
 * @create 2018/12/13 0013 9:17
 * description: 修改或者添加紧急联系人
 */
public class EmergencyContactActivity extends BaseActivity {


    @BindView(R.id.tv_complete)
    TextView tvComplete;
    GuideAddLinkManPresenter guideAddLinkManPresenter;
    String type;//标志位，1为修改，2为添加
    String name;
    String phone;
    String orderNumber;
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_title_center)
    TextView tvTitleCenter;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.tv_title_one)
    TextView tvTitleOne;
    @BindView(R.id.et_emergency_name_one)
    EditText etEmergencyNameOne;
    @BindView(R.id.ll_name_one)
    LinearLayout llNameOne;
    @BindView(R.id.et_emergency_phone_one)
    EditText etEmergencyPhoneOne;
    @BindView(R.id.ll_phone_one)
    LinearLayout llPhoneOne;
    ModifyLinkManPresenter modifyLinkManPresenter;

    @Override
    protected int setRootViewId() {
        return R.layout.activity_emergency_contact;
    }

    @Override
    protected boolean setIsFull() {
        return false;
    }

    @Override
    protected void InitialView() {
        StatusBarUtils.setWindowStatusBarColor(EmergencyContactActivity.this, R.color.white);
        type = getIntent().getExtras().getString("type");
        if (type.equals("1")) {
            name = getIntent().getExtras().getString("name");
            phone = getIntent().getExtras().getString("phone");
            orderNumber = getIntent().getExtras().getString("orderNumber");
            etEmergencyNameOne.setText(name);
            etEmergencyPhoneOne.setText(phone);
            tvTitleCenter.setText("修改紧急联系人");
            tvComplete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!PhoneNumberUtils.isMobile(etEmergencyPhoneOne.getText().toString())) {
                        ToastUtils.msg("请输入合法的电话号码");
                        return;
                    }
                    modifyLinkManPresenter = new ModifyLinkManPresenter(EmergencyContactActivity.this);
                    modifyLinkManPresenter.onCreate();
                    modifyLinkManPresenter.attachView(modifyLinkManView);
                    JSONObject requestData = new JSONObject();
                    try {
                        requestData.put("name", etEmergencyNameOne.getText().toString());
                        requestData.put("phone", etEmergencyPhoneOne.getText().toString());
                        requestData.put("orderNumber", orderNumber);
                        requestData.put("sceneryId", "1");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestData.toString());
                    modifyLinkManPresenter.updatelinkman(requestBody);
                    showProgressDialog();
                }
            });
        } else if (type.equals("2")) {
            orderNumber = getIntent().getExtras().getString("orderNumber");
            tvTitleCenter.setText("添加紧急联系人");
            tvComplete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!PhoneNumberUtils.isMobile(etEmergencyPhoneOne.getText().toString())) {
                        ToastUtils.msg("请输入合法的电话号码");
                        return;
                    }
                    guideAddLinkManPresenter = new GuideAddLinkManPresenter(EmergencyContactActivity.this);
                    guideAddLinkManPresenter.onCreate();
                    guideAddLinkManPresenter.attachView(baseView);
                    JSONObject requestData = new JSONObject();
                    try {
                        requestData.put("name", etEmergencyNameOne.getText().toString());
                        requestData.put("phone", etEmergencyPhoneOne.getText().toString());
                        requestData.put("orderNumber", orderNumber);
                        requestData.put("sceneryId", "1");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestData.toString());
                    guideAddLinkManPresenter.addlinkman(requestBody);
                    showProgressDialog();
                }
            });
        }


    }

    BaseView baseView = new BaseView() {
        @Override
        public void onSuccess(BaseBean mBaseBean) {
            finish();
            dismissProgressDialog();
            ToastUtils.msg(mBaseBean.getResultStatus().getResultMessage().toString());
        }

        @Override
        public void onError(String result) {
            dismissProgressDialog();
            ToastUtils.msg(result);
        }
    };
    ModifyLinkManView modifyLinkManView = new ModifyLinkManView() {
        @Override
        public void onSuccess(BaseBean mBaseBean) {
            finish();
            dismissProgressDialog();
            ToastUtils.msg(mBaseBean.getResultStatus().getResultMessage().toString());
        }

        @Override
        public void onError(String result) {
            dismissProgressDialog();
            ToastUtils.msg(result);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.rl_back)
    public void onClick() {
        finish();
    }
}
