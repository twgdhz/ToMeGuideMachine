package com.guidemachine.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Html;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guidemachine.R;
import com.guidemachine.base.ui.BaseActivity;
import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.presenter.SOSLogPresenter;
import com.guidemachine.service.view.BaseView;
import com.guidemachine.ui.view.CompletedView;
import com.guidemachine.util.Logger;
import com.guidemachine.util.MobileInfoUtil;
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

public class SOSActivity extends BaseActivity {

    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_title_center)
    TextView tvTitleCenter;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.tv_sos_notice)
    TextView tvSosNotice;
    @BindView(R.id.tasks_view)
    CompletedView tasksView;
    @BindView(R.id.tv_sos)
    TextView tvSos;
    private int mTotalProgress = 100;
    private int mCurrentProgress = 0;
    //进度条
    private CompletedView mTasksView;
    //发送sos
    SOSLogPresenter sosLogPresenter;

    @Override
    protected int setRootViewId() {
        return R.layout.activity_sos;
    }

    @Override
    protected boolean setIsFull() {
        return false;
    }

    @Override
    protected void InitialView() {//遇到紧急事件长按“SOS”键3秒，即可呼救我们将及时赶往您的位置提供帮助
        StatusBarUtils.setWindowStatusBarColor(SOSActivity.this, R.color.text_color4);
        String content = "遇到" + "<font color='#e4140f'>" + "紧急事件长按“SOS”键3秒，即可呼救" + "</font>" + "我们将及时赶往您的位置提供帮助";
        tvSosNotice.setText(Html.fromHtml(content));
        mTasksView = (CompletedView) findViewById(R.id.tasks_view);
        tvSos.setVisibility(View.VISIBLE);
        sosLogPresenter = new SOSLogPresenter(SOSActivity.this);
        sosLogPresenter.onCreate();
        sosLogPresenter.attachView(baseView);
//        mTasksView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
        mTasksView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                tvSos.setVisibility(View.GONE);
                new Thread(new ProgressRunable()).start();
                return false;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    class ProgressRunable implements Runnable {
        @Override
        public void run() {
            while (mCurrentProgress < mTotalProgress) {

                mCurrentProgress += 1;
                mTasksView.setProgress(mCurrentProgress);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mCurrentProgress == 100) {
                            Vibrator vibrator = (Vibrator) SOSActivity.this.getSystemService(SOSActivity.this.VIBRATOR_SERVICE);
                            vibrator.vibrate(1000);
                            JSONObject loginRequestData = new JSONObject();
                            try {
                                loginRequestData.put("address", SPHelper.getInstance(SOSActivity.this).getCityName());
//                                loginRequestData.put("imei", MobileInfoUtil.getIMEI(SOSActivity.this));
                                loginRequestData.put("imei", "956680617111246");
                                loginRequestData.put("lon", SPHelper.getInstance(SOSActivity.this).getLongitude());
                                loginRequestData.put("lat", SPHelper.getInstance(SOSActivity.this).getLatitude());
                                loginRequestData.put("sceneryId", 1);
                                loginRequestData.put("createTime", System.currentTimeMillis());
                                Logger.d("时间戳", System.currentTimeMillis() + "");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            RequestBody loginRequestBody = RequestBody.create(MediaType.parse("application/json"), loginRequestData.toString());
                            sosLogPresenter.getSOSLog(loginRequestBody);
                        }
                    }
                });
                try {
                    Thread.sleep(25);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    BaseView baseView = new BaseView() {
        @Override
        public void onSuccess(BaseBean mBaseBean) {
//            ToastUtils.msg(mBaseBean.getResultStatus().getResultMessage().toString());
            ToastUtils.msg("求救成功");
            callPhone("15828472427");
            finish();
        }

        @Override
        public void onError(String result) {
            ToastUtils.msg(result);
        }
    };

    /**
     * 拨打电话（直接拨打电话）
     *
     * @param phoneNum 电话号码
     */
    public void callPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sosLogPresenter.onStop();
    }

    @OnClick(R.id.rl_back)
    public void onClick() {
        finish();
    }
}
