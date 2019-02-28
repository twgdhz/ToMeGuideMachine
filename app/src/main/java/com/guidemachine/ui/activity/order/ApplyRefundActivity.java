package com.guidemachine.ui.activity.order;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guidemachine.R;
import com.guidemachine.base.ui.BaseActivity;
import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.CreateOrderRefundReserveBean;
import com.guidemachine.service.presenter.OrderRefundPresenter;
import com.guidemachine.service.presenter.PushOrderRefundPresenter;
import com.guidemachine.service.view.BaseView;
import com.guidemachine.service.view.OrderRefundView;
import com.guidemachine.ui.view.ContainsEmojiEditText;
import com.guidemachine.util.StatusBarUtils;
import com.guidemachine.util.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class ApplyRefundActivity extends BaseActivity {

    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_title_center)
    TextView tvTitleCenter;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.rl_screen)
    RelativeLayout rlScreen;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.text_park_name)
    TextView textParkName;
    @BindView(R.id.tv_refund_content)
    TextView tvRefundContent;
    @BindView(R.id.tv_refund_pay_type)
    TextView tvRefundPayType;
    @BindView(R.id.rg_refund_reason)
    RadioGroup rgRefundReason;
    @BindView(R.id.tv_fill_et_number)
    TextView tvFillEtNumber;
    @BindView(R.id.et_advice)
    ContainsEmojiEditText etAdvice;
    @BindView(R.id.tv_apply_refund)
    TextView tvApplyRefund;
    //售后详情
    OrderRefundPresenter orderRefundPresenter;
    //申请退款
    PushOrderRefundPresenter pushOrderRefundPresenter;
    String selectText = "";
    int orderType;//订单类型类型(1 导游机/套票 2实体商品)
    String orderId;

    @Override
    protected int setRootViewId() {
        return R.layout.activity_apply_refund;
    }


    @Override
    protected boolean setIsFull() {
        return false;
    }

    @Override
    protected void InitialView() {
        Add(this);
        StatusBarUtils.setWindowStatusBarColor(ApplyRefundActivity.this, R.color.text_color4);
        tvTitleCenter.setText("退款申请");
        orderId = getIntent().getExtras().getString("orderId");
        orderRefundPresenter = new OrderRefundPresenter(ApplyRefundActivity.this);
        orderRefundPresenter.onCreate();
        orderRefundPresenter.attachView(orderRefundView);
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("orderId", orderId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestData.toString());
        orderRefundPresenter.getOrderRefundFilling(requestBody);
        showProgressDialog();
        pushOrderRefundPresenter = new PushOrderRefundPresenter(ApplyRefundActivity.this);
        pushOrderRefundPresenter.onCreate();
        pushOrderRefundPresenter.attachView(baseView);
        rgRefundReason.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) findViewById(rgRefundReason.getCheckedRadioButtonId());
                selectText = radioButton.getText().toString();
//                ToastUtils.msg(selectText);
                etAdvice.setEnabled(false);
            }
        });

    }

    OrderRefundView orderRefundView = new OrderRefundView() {
        @Override
        public void onSuccess(final BaseBean<CreateOrderRefundReserveBean> bean) {

            dismissProgressDialog();
            textParkName.setText("退款内容：" + bean.getValue().getGoodsName());
            tvRefundContent.setText("现金：￥" + bean.getValue().getRealPrice());
            if (bean.getValue().getPayType() == 1) {//支付方式(1微信2支付宝)
                tvRefundPayType.setText("微信");
            } else if (bean.getValue().getPayType() == 2) {
                tvRefundPayType.setText("支付宝");
            }
            tvApplyRefund.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectText.equals("")) {
                        ToastUtils.msg("请选择退款原因");
                        return;
                    }
//                if (etAdvice.getText().toString().equals("")){
//                    ToastUtils.msg("请输入退款原因");
//                }
                    JSONObject requestData = new JSONObject();
                    try {
                        requestData.put("orderId", orderId);
                        requestData.put("refundInfo", bean.getValue().getGoodsName() + selectText);//退款原因
                        requestData.put("refundContent", etAdvice.getText().toString());//退还内容
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestData.toString());
                    pushOrderRefundPresenter.commitOrderSale(requestBody);
                }
            });
        }

        @Override
        public void onError(String result) {
            dismissProgressDialog();
        }
    };
    BaseView baseView = new BaseView() {
        @Override
        public void onSuccess(BaseBean mBaseBean) {
            ToastUtils.msg(mBaseBean.getResultStatus().getResultMessage().toString());
            finish();
        }

        @Override
        public void onError(String result) {
            ToastUtils.msg(result);
        }
    };

    @OnClick(R.id.rl_back)
    public void onClick() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}

