package com.guidemachine;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arcsoft.face.ErrorInfo;
import com.arcsoft.face.FaceEngine;
import com.guidemachine.base.ui.BaseActivity;
import com.guidemachine.constant.Constants;
import com.guidemachine.ui.activity.LoginActivity;
import com.guidemachine.ui.activity.MapActivity;
import com.guidemachine.ui.activity.NoticeActivity;
import com.guidemachine.ui.activity.PersonCenterActivity;
import com.guidemachine.ui.activity.RegisterAndRecognizeActivity;
import com.guidemachine.ui.activity.ShopListActivity;
import com.guidemachine.ui.activity.WelcomeActivity;
import com.guidemachine.ui.activity.chat.ChatActivity;
import com.guidemachine.ui.activity.chat.ChatLoginActivity;
import com.guidemachine.ui.activity.chat.domain.DemoHelper;
import com.guidemachine.util.IntentUtils;
import com.guidemachine.util.IsLoginUtils;
import com.guidemachine.util.share.SPHelper;
import com.hyphenate.easeui.EaseConstant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
/**
* @author ChenLinWang
* @email 422828518@qq.com
* @create 2018/12/14 0014 14:40
* description: 主页面
*/
public class MainActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.tv_item_name)
    Button tvItemName;
    @BindView(R.id.btn_active)
    Button btnActive;
    @BindView(R.id.btn_change)
    Button btnChange;
    @BindView(R.id.btn_map)
    Button btnMap;
    @BindView(R.id.btn_communication)
    Button btnCommunication;
    @BindView(R.id.ll_speech_tour)
    LinearLayout llSpeechTour;
    @BindView(R.id.ll_shop_tour)
    LinearLayout llShopTour;
    @BindView(R.id.ll_im_tour)
    LinearLayout llImTour;
    @BindView(R.id.ll_message)
    LinearLayout llMessage;
    @BindView(R.id.ll_personnal_center)
    LinearLayout llPersonnalCenter;
    private Toast toast = null;
    private static final int ACTION_REQUEST_PERMISSIONS = 0x001;
    private static final String[] NEEDED_PERMISSIONS = new String[]{
            Manifest.permission.READ_PHONE_STATE
    };
    private int BatteryN; //目前电量
    private int BatteryV; //电池电压
    private double BatteryT; //电池温度
    private String BatteryStatus; //电池状态
    private String BatteryTemp; //电池使用情况
    public Button TV;
    @Override
    protected int setRootViewId() {
        return R.layout.activity_main;
    }


    @Override
    protected boolean setIsFull() {
        return false;
    }

    @Override
    protected void InitialView() {
        setTranslucentStatus();
        llImTour.setOnClickListener(this);
        llMessage.setOnClickListener(this);
        llPersonnalCenter.setOnClickListener(this);
        llShopTour.setOnClickListener(this);
        llSpeechTour.setOnClickListener(this);
        tvItemName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegisterAndRecognizeActivity.class));
            }
        });
        btnActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activeEngine(view);
            }
        });
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchLanguage("en");
            }
        });
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, WelcomeActivity.class));
            }
        });
        btnCommunication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ChatLoginActivity.class));
            }
        });
        Log.d("手机imei", getIMEI(MainActivity.this));
//        JSONObject requestData = new JSONObject();
//        try {
//            requestData.put("sceneryId", 1);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestData.toString());
//        scenerySpotPresenter.getSceneryList(requestBody, 4);
//        showProgressDialog();

// 注册一个系统 BroadcastReceiver，作为访问电池计量之用这个不能直接在AndroidManifest.xml中注册
        registerReceiver(mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        TV = (Button)findViewById(R.id.tv_item_name);
    }
    /* 创建广播接收器 */
    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver()
    {
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();
            /*
             * 如果捕捉到的action是ACTION_BATTERY_CHANGED， 就运行onBatteryInfoReceiver()
             */
            if (Intent.ACTION_BATTERY_CHANGED.equals(action))
            {
                BatteryN = intent.getIntExtra("level", 0); //目前电量
                BatteryV = intent.getIntExtra("voltage", 0); //电池电压
                BatteryT = intent.getIntExtra("temperature", 0); //电池温度
                switch (intent.getIntExtra("status", BatteryManager.BATTERY_STATUS_UNKNOWN))
                {
                    case BatteryManager.BATTERY_STATUS_CHARGING:
                        BatteryStatus = "充电状态";
                        break;
                    case BatteryManager.BATTERY_STATUS_DISCHARGING:
                        BatteryStatus = "放电状态";
                        break;
                    case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                        BatteryStatus = "未充电";
                        break;
                    case BatteryManager.BATTERY_STATUS_FULL:
                        BatteryStatus = "充满电";
                        break;
                    case BatteryManager.BATTERY_STATUS_UNKNOWN:
                        BatteryStatus = "未知道状态";
                        break;
                }
                switch (intent.getIntExtra("health", BatteryManager.BATTERY_HEALTH_UNKNOWN))
                {
                    case BatteryManager.BATTERY_HEALTH_UNKNOWN:
                        BatteryTemp = "未知错误";
                        break;
                    case BatteryManager.BATTERY_HEALTH_GOOD:
                        BatteryTemp = "状态良好";
                        break;
                    case BatteryManager.BATTERY_HEALTH_DEAD:
                        BatteryTemp = "电池没有电";
                        break;
                    case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                        BatteryTemp = "电池电压过高";
                        break;
                    case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                        BatteryTemp = "电池过热";
                        break;
                }
                TV.setText("目前电量为" + BatteryN + "% --- " + BatteryStatus + "\n" + "电压为" + BatteryV + "mV --- " + BatteryTemp + "\n" + "温度为" + (BatteryT*0.1) + "℃");
            }
        }
    };

    /**
     * 获取手机IMEI号
     */
    public static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();
        return imei;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_speech_tour:
                startActivity(new Intent(MainActivity.this, MapActivity.class));
                break;
            case R.id.ll_shop_tour:
                if (IsLoginUtils.getInstence().isLogin(MainActivity.this)==true) {
                    startActivity(new Intent(MainActivity.this, ShopListActivity.class));
                    finish();
                } else {
                    IntentUtils.openActivity(MainActivity.this, LoginActivity.class,"type","1");
                    finish();
                }
                break;
            case R.id.ll_im_tour:
//                startActivity(new Intent(MainActivity.this, ChatLoginActivity.class));
                if (DemoHelper.getInstance().isLoggedIn()) {
                    startActivity(new Intent(MainActivity.this, ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID,
                            SPHelper.getInstance(MainActivity.this).getPhone()));
                    finish();
                } else {
                    IntentUtils.openActivity(MainActivity.this, LoginActivity.class,"type","2");
                    finish();
                }
                break;
            case R.id.ll_message:
                startActivity(new Intent(MainActivity.this, NoticeActivity.class));
                break;
            case R.id.ll_personnal_center:
                startActivity(new Intent(MainActivity.this, PersonCenterActivity.class));
                break;
        }
    }

    //切换方法
    private void switchLanguage(String language) {
        //获取资源
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        if (language.equals("cn"))
            config.locale = Locale.CHINA;//方法过时但是一样可以使用
        else
            config.setLocale(Locale.ENGLISH);//低版本无法使用,如果使用不了,就使用上面的方法

        //更新,稍后提及次方法过时.
        resources.updateConfiguration(config, resources.getDisplayMetrics());

        //关闭页面,重新绘制
        finish();
        startActivity(new Intent(MainActivity.this, MainActivity.class));
    }

    /**
     * 激活引擎
     *
     * @param view
     */
    public void activeEngine(final View view) {
        if (!checkPermissions(NEEDED_PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, NEEDED_PERMISSIONS, ACTION_REQUEST_PERMISSIONS);
            return;
        }
        if (view != null) {
            view.setClickable(false);
        }
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                FaceEngine faceEngine = new FaceEngine();
                int activeCode = faceEngine.active(MainActivity.this, Constants.APP_ID, Constants.SDK_KEY);
                emitter.onNext(activeCode);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer activeCode) {
                        if (activeCode == ErrorInfo.MOK) {
                            showToast(getString(R.string.active_success));
                        } else if (activeCode == ErrorInfo.MERR_ASF_ALREADY_ACTIVATED) {
                            showToast(getString(R.string.already_activated));
                        } else {
                            showToast(getString(R.string.active_failed, activeCode));
                        }

                        if (view != null) {
                            view.setClickable(true);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private boolean checkPermissions(String[] neededPermissions) {
        if (neededPermissions == null || neededPermissions.length == 0) {
            return true;
        }
        boolean allGranted = true;
        for (String neededPermission : neededPermissions) {
            allGranted &= ContextCompat.checkSelfPermission(this, neededPermission) == PackageManager.PERMISSION_GRANTED;
        }
        return allGranted;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ACTION_REQUEST_PERMISSIONS) {
            boolean isAllGranted = true;
            for (int grantResult : grantResults) {
                isAllGranted &= (grantResult == PackageManager.PERMISSION_GRANTED);
            }
            if (isAllGranted) {
                activeEngine(null);
            } else {
                showToast(getString(R.string.permission_denied));
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    private void showToast(String s) {
        if (toast == null) {
            toast = Toast.makeText(this, s, Toast.LENGTH_SHORT);
            toast.show();
        } else {
            toast.setText(s);
            toast.show();
        }
    }

}
