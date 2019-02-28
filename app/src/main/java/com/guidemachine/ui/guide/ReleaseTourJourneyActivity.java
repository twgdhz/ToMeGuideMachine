package com.guidemachine.ui.guide;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guidemachine.R;
import com.guidemachine.base.ui.BaseActivity;
import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.presenter.ReleaseJourneyPresenter;
import com.guidemachine.service.view.BaseView;
import com.guidemachine.ui.activity.SceneListActivity;
import com.guidemachine.util.StatusBarUtils;
import com.guidemachine.util.ToastUtils;
import com.guidemachine.util.share.SPHelper;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * @author ChenLinWang
 * @email 422828518@qq.com
 * @create 2019/1/3 0003 14:43
 * description: 行程发布
 */
public class ReleaseTourJourneyActivity extends BaseActivity {
    public static final int CHAR_NUMBER_EDIT = 50;
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_title_center)
    TextView tvTitleCenter;
    @BindView(R.id.rl_right)
    RelativeLayout rlRight;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.tv_fill_et_number)
    TextView tvFillEtNumber;
    @BindView(R.id.et_journey)
    EditText etJourney;
    @BindView(R.id.tv_release)
    TextView tvRelease;
    ReleaseJourneyPresenter releaseJourneyPresenter;
    String imei;

    @Override
    protected int setRootViewId() {
        return R.layout.activity_release_tour_journey;
    }

    @Override
    protected boolean setIsFull() {
        return false;
    }

    @Override
    protected void InitialView() {
        StatusBarUtils.setWindowStatusBarColor(ReleaseTourJourneyActivity.this, R.color.white);
        releaseJourneyPresenter = new ReleaseJourneyPresenter(ReleaseTourJourneyActivity.this);
        releaseJourneyPresenter.onCreate();
        releaseJourneyPresenter.attachView(baseView);
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            imei = intent.getExtras().getString("imei");
        } else {
            imei = "";
        }
        etJourney.addTextChangedListener(new TextWatcher() {//监听输入框字数
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String format = "%d/%d";
                tvFillEtNumber.setText(String.format(format, s.length(), CHAR_NUMBER_EDIT));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tvRelease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etJourney.getText().toString().length() < 6) {
                    ToastUtils.msg("不能少于六个字！");
                    return;
                }
                JSONObject requestData = new JSONObject();
                try {
                    requestData.put("sceneryId", SPHelper.getInstance(ReleaseTourJourneyActivity.this).getSceneryId());
                    requestData.put("content", etJourney.getText().toString());
                    requestData.put("imei", imei);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestData.toString());
                releaseJourneyPresenter.releaseJourney(requestBody);
                showProgressDialog();
            }
        });
    }

    BaseView baseView = new BaseView() {
        @Override
        public void onSuccess(BaseBean mBaseBean) {
            dismissProgressDialog();
            ToastUtils.msg(mBaseBean.getResultStatus().getResultMessage().toString());
            finish();
        }

        @Override
        public void onError(String result) {
            ToastUtils.msg(result);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.rl_back)
    public void onClick() {
        finish();
    }
}
