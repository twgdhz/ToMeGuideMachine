package com.guidemachine.ui.activity;

import android.Manifest;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.arcsoft.face.AgeInfo;
import com.arcsoft.face.ErrorInfo;
import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.FaceFeature;
import com.arcsoft.face.GenderInfo;
import com.arcsoft.face.LivenessInfo;
import com.arcsoft.face.VersionInfo;
import com.guidemachine.R;
import com.guidemachine.face.faceserver.CompareResult;
import com.guidemachine.face.faceserver.FaceServer;
import com.guidemachine.face.model.DrawInfo;
import com.guidemachine.face.model.FacePreviewInfo;
import com.guidemachine.face.widget.FaceRectView;
import com.guidemachine.face.widget.ShowFaceInfoAdapter;
import com.guidemachine.util.ConfigUtil;
import com.guidemachine.util.DrawHelper;
import com.guidemachine.util.IntentUtils;
import com.guidemachine.util.ToastUtils;
import com.guidemachine.util.camera.CameraHelper;
import com.guidemachine.util.camera.CameraListener;
import com.guidemachine.util.face.FaceHelper;
import com.guidemachine.util.face.FaceListener;
import com.guidemachine.util.face.RequestFeatureStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author ChenLinWang
 * @email 422828518@qq.com
 * @create 2018/11/8 0008 10:19
 * description: 人脸识与注册
 */
public class RegisterAndRecognizeActivity extends AppCompatActivity {
    private static final String TAG = "RegisterAndRecognize";
    private static final int MAX_DETECT_NUM = 10;
    /**
     * 当FR成功，活体未成功时，FR等待活体的时间
     */
    private static final int WAIT_LIVENESS_INTERVAL = 50;
    private CameraHelper cameraHelper;
    private DrawHelper drawHelper;
    private Camera.Size previewSize;
    /**
     * 优先打开的摄像头
     */
    private Integer cameraID = Camera.CameraInfo.CAMERA_FACING_FRONT;
    private FaceEngine faceEngine;
    private FaceHelper faceHelper;
    private List<CompareResult> compareResultList;
    private ShowFaceInfoAdapter adapter;
    /**
     * 活体检测的开关
     */
    private boolean livenessDetect = true;

    /**
     * 注册人脸状态码，准备注册
     */
    private static final int REGISTER_STATUS_READY = 0;
    /**
     * 注册人脸状态码，注册中
     */
    private static final int REGISTER_STATUS_PROCESSING = 1;
    /**
     * 注册人脸状态码，注册结束（无论成功失败）
     */
    private static final int REGISTER_STATUS_DONE = 2;

    private int registerStatus = REGISTER_STATUS_READY;

    private int afCode = -1;
    private ConcurrentHashMap<Integer, Integer> requestFeatureStatusMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Integer, Integer> livenessMap = new ConcurrentHashMap<>();
    private CompositeDisposable getFeatureDelayedDisposables = new CompositeDisposable();
    /**
     * 相机预览显示的控件，可为SurfaceView或TextureView
     */
    private View previewView;
    /**
     * 绘制人脸框的控件
     */
    private FaceRectView faceRectView;

    private Switch switchLivenessDetect;

    private static final int ACTION_REQUEST_PERMISSIONS = 0x001;
    private static final float SIMILAR_THRESHOLD = 0.8F;
    /**
     * 所需的所有权限信息
     */
    private static final String[] NEEDED_PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE
    };
    TextView tv_login_failed;
    int num = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_and_recognize);
        //保持亮屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setTranslucentStatus();
        tv_login_failed = findViewById(R.id.tv_login_failed);
        tv_login_failed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.openActivity(RegisterAndRecognizeActivity.this, LoginActivity.class, "type", "1");
                finish();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams attributes = getWindow().getAttributes();
            attributes.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            getWindow().setAttributes(attributes);
        }

        // Activity启动后就锁定为启动时的方向
        switch (getResources().getConfiguration().orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                break;
            default:
                break;
        }
        //人脸库初始化
        FaceServer.getInstance().init(this);

        previewView = findViewById(R.id.texture_preview);
        faceRectView = findViewById(R.id.face_rect_view);
        switchLivenessDetect = findViewById(R.id.switch_liveness_detect);
        switchLivenessDetect.setChecked(livenessDetect);
        switchLivenessDetect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                livenessDetect = isChecked;
            }
        });
        RecyclerView recyclerShowFaceInfo = findViewById(R.id.recycler_view_person);
        compareResultList = new ArrayList<>();
        adapter = new ShowFaceInfoAdapter(compareResultList, this);
        recyclerShowFaceInfo.setAdapter(adapter);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int spanCount = (int) (dm.widthPixels / (getResources().getDisplayMetrics().density * 100 + 0.5f));
        recyclerShowFaceInfo.setLayoutManager(new GridLayoutManager(this, spanCount));
        recyclerShowFaceInfo.setItemAnimator(new DefaultItemAnimator());

        if (!checkPermissions(NEEDED_PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, NEEDED_PERMISSIONS, ACTION_REQUEST_PERMISSIONS);
        } else {
            initEngine();
            initCamera();
        }


    }

    /**
     * 初始化引擎
     */
    private void initEngine() {
        faceEngine = new FaceEngine();
        afCode = faceEngine.init(this, FaceEngine.ASF_DETECT_MODE_VIDEO, ConfigUtil.getFtOrient(this),
                16, MAX_DETECT_NUM, FaceEngine.ASF_FACE_RECOGNITION | FaceEngine.ASF_FACE_DETECT | FaceEngine.ASF_LIVENESS);
        VersionInfo versionInfo = new VersionInfo();
        faceEngine.getVersion(versionInfo);
        Log.i(TAG, "initEngine:  init: " + afCode + "  version:" + versionInfo);

        if (afCode != ErrorInfo.MOK) {
            Toast.makeText(this, getString(R.string.init_failed, afCode), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 销毁引擎
     */
    private void unInitEngine() {

        if (afCode == ErrorInfo.MOK) {
            afCode = faceEngine.unInit();
            Log.i(TAG, "unInitEngine: " + afCode);
        }
    }


    @Override
    protected void onDestroy() {

        if (cameraHelper != null) {
            cameraHelper.release();
            cameraHelper = null;
        }

        //faceHelper中可能会有FR耗时操作仍在执行，加锁防止crash
        if (faceHelper != null) {
            synchronized (faceHelper) {
                unInitEngine();
            }
            ConfigUtil.setTrackId(this, faceHelper.getCurrentTrackId());
            faceHelper.release();
        } else {
            unInitEngine();
        }
        if (getFeatureDelayedDisposables != null) {
            getFeatureDelayedDisposables.dispose();
            getFeatureDelayedDisposables.clear();
        }
        FaceServer.getInstance().unInit();
        super.onDestroy();
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

    private void initCamera() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        final FaceListener faceListener = new FaceListener() {
            @Override
            public void onFail(Exception e) {
                Log.e(TAG, "onFail: " + e.getMessage());
            }

            //请求FR的回调
            @Override
            public void onFaceFeatureInfoGet(@Nullable final FaceFeature faceFeature, final Integer requestId) {
                //FR成功
                if (faceFeature != null) {
                    Log.i(TAG, "onPreview: fr end = " + System.currentTimeMillis() + " trackId = " + requestId);

                    //不做活体检测的情况，直接搜索
                    if (!livenessDetect) {
                        searchFace(faceFeature, requestId);
                    }
                    //活体检测通过，搜索特征
                    else if (livenessMap.get(requestId) != null && livenessMap.get(requestId) == LivenessInfo.ALIVE) {
                        searchFace(faceFeature, requestId);
                    }
                    //活体检测未出结果，延迟100ms再执行该函数
                    else if (livenessMap.get(requestId) != null && livenessMap.get(requestId) == LivenessInfo.UNKNOWN) {
                        getFeatureDelayedDisposables.add(Observable.timer(WAIT_LIVENESS_INTERVAL, TimeUnit.MILLISECONDS)
                                .subscribe(new Consumer<Long>() {
                                    @Override
                                    public void accept(Long aLong) {
                                        onFaceFeatureInfoGet(faceFeature, requestId);
                                    }
                                }));
                    }
                    //活体检测失败
                    else {
                        requestFeatureStatusMap.put(requestId, RequestFeatureStatus.NOT_ALIVE);
                    }

                }
                //FR 失败
                else {
                    requestFeatureStatusMap.put(requestId, RequestFeatureStatus.FAILED);
                }
            }

        };


        CameraListener cameraListener = new CameraListener() {
            @Override
            public void onCameraOpened(Camera camera, int cameraId, int displayOrientation, boolean isMirror) {
                previewSize = camera.getParameters().getPreviewSize();
                drawHelper = new DrawHelper(previewSize.width, previewSize.height, previewView.getWidth(), previewView.getHeight(), displayOrientation
                        , cameraId, isMirror);

                faceHelper = new FaceHelper.Builder()
                        .faceEngine(faceEngine)
                        .frThreadNum(MAX_DETECT_NUM)
                        .previewSize(previewSize)
                        .faceListener(faceListener)
                        .currentTrackId(ConfigUtil.getTrackId(RegisterAndRecognizeActivity.this.getApplicationContext()))
                        .build();
            }


            @Override
            public void onPreview(final byte[] nv21, Camera camera) {
                if (faceRectView != null) {
                    faceRectView.clearFaceInfo();
                }
                List<FacePreviewInfo> facePreviewInfoList = faceHelper.onPreviewFrame(nv21);
                if (facePreviewInfoList != null && faceRectView != null && drawHelper != null) {
                    List<DrawInfo> drawInfoList = new ArrayList<>();
                    for (int i = 0; i < facePreviewInfoList.size(); i++) {
                        String name = faceHelper.getName(facePreviewInfoList.get(i).getTrackId());
//                        drawInfoList.add(new DrawInfo(facePreviewInfoList.get(i).getFaceInfo().getRect(), GenderInfo.UNKNOWN, AgeInfo.UNKNOWN_AGE, LivenessInfo.UNKNOWN,
//                                name == null ? String.valueOf(facePreviewInfoList.get(i).getTrackId()) : name));
                        drawInfoList.add(new DrawInfo(facePreviewInfoList.get(i).getFaceInfo().getRect(), GenderInfo.UNKNOWN, AgeInfo.UNKNOWN_AGE, LivenessInfo.UNKNOWN,""));
                    }
                    drawHelper.draw(faceRectView, drawInfoList);
                }
                if (registerStatus == REGISTER_STATUS_READY && facePreviewInfoList != null && facePreviewInfoList.size() > 0) {
                    registerStatus = REGISTER_STATUS_PROCESSING;
                    Observable.create(new ObservableOnSubscribe<Boolean>() {
                        @Override
                        public void subscribe(ObservableEmitter<Boolean> emitter) {
//                            boolean success = FaceServer.getInstance().register(RegisterAndRecognizeActivity.this, nv21.clone(), previewSize.width, previewSize.height, "registered" + faceHelper.getCurrentTrackId());
                            boolean success = FaceServer.getInstance().register(RegisterAndRecognizeActivity.this, nv21.clone(), previewSize.width, previewSize.height, "");
                            emitter.onNext(success);
                        }
                    })
                            .subscribeOn(Schedulers.computation())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<Boolean>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onNext(Boolean success) {
//                                    String result = success ? "注册成功!" : "注册失败，请重试!";
                                    if (success == false) {
//                                        num++;
//                                        if (num >= 3) {
//                                            tv_login_failed.setVisibility(View.VISIBLE);
//                                        }
                                    }
//                                    Toast.makeText(RegisterAndRecognizeActivity.this, result, Toast.LENGTH_SHORT).show();
                                    registerStatus = REGISTER_STATUS_READY;
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            IntentUtils.openActivity(RegisterAndRecognizeActivity.this, LoginActivity.class, "type", "1");
                                            finish();
                                        }
                                    }, 3000);
                                }

                                @Override
                                public void onError(Throwable e) {
//                                    Toast.makeText(RegisterAndRecognizeActivity.this, "注册失败，请重试!", Toast.LENGTH_SHORT).show();
                                    registerStatus = REGISTER_STATUS_READY;
//                                    new Handler().postDelayed(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            IntentUtils.openActivity(RegisterAndRecognizeActivity.this, LoginActivity.class, "type", "1");
//                                            finish();
//                                        }
//                                    }, 3000);
                                }

                                @Override
                                public void onComplete() {

                                }
                            });
                }
                clearLeftFace(facePreviewInfoList);

                if (facePreviewInfoList != null && facePreviewInfoList.size() > 0 && previewSize != null) {

                    for (int i = 0; i < facePreviewInfoList.size(); i++) {
                        if (livenessDetect) {
                            livenessMap.put(facePreviewInfoList.get(i).getTrackId(), facePreviewInfoList.get(i).getLivenessInfo().getLiveness());
                        }
                        /**
                         * 对于每个人脸，若状态为空或者为失败，则请求FR（可根据需要添加其他判断以限制FR次数），
                         * FR回传的人脸特征结果在{@link FaceListener#onFaceFeatureInfoGet(FaceFeature, Integer)}中回传
                         */
                        if (requestFeatureStatusMap.get(facePreviewInfoList.get(i).getTrackId()) == null
                                || requestFeatureStatusMap.get(facePreviewInfoList.get(i).getTrackId()) == RequestFeatureStatus.FAILED) {
                            requestFeatureStatusMap.put(facePreviewInfoList.get(i).getTrackId(), RequestFeatureStatus.SEARCHING);
                            faceHelper.requestFaceFeature(nv21, facePreviewInfoList.get(i).getFaceInfo(), previewSize.width, previewSize.height, FaceEngine.CP_PAF_NV21, facePreviewInfoList.get(i).getTrackId());
//                            Log.i(TAG, "onPreview: fr start = " + System.currentTimeMillis() + " trackId = " + facePreviewInfoList.get(i).getTrackId());
                        }
                    }
                }
            }

            @Override
            public void onCameraClosed() {
                Log.i(TAG, "onCameraClosed: ");
            }

            @Override
            public void onCameraError(Exception e) {
                Log.i(TAG, "onCameraError: " + e.getMessage());
            }

            @Override
            public void onCameraConfigurationChanged(int cameraID, int displayOrientation) {
                if (drawHelper != null) {
                    drawHelper.setCameraDisplayOrientation(displayOrientation);
                }
                Log.i(TAG, "onCameraConfigurationChanged: " + cameraID + "  " + displayOrientation);
            }
        };
        cameraHelper = new CameraHelper.Builder()
                .metrics(metrics)
                .rotation(getWindowManager().getDefaultDisplay().getRotation())
                .specificCameraId(cameraID != null ? cameraID : Camera.CameraInfo.CAMERA_FACING_FRONT)
                .isMirror(false)
                .previewOn(previewView)
                .cameraListener(cameraListener)
                .build();
        cameraHelper.init();
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
                initEngine();
                initCamera();
                if (cameraHelper != null) {
                    cameraHelper.start();
                }
            } else {
                Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 删除已经离开的人脸
     *
     * @param facePreviewInfoList 人脸和trackId列表
     */
    private void clearLeftFace(List<FacePreviewInfo> facePreviewInfoList) {
        Set<Integer> keySet = requestFeatureStatusMap.keySet();
        if (compareResultList != null) {
            for (int i = compareResultList.size() - 1; i >= 0; i--) {
                if (!keySet.contains(compareResultList.get(i).getTrackId())) {
                    compareResultList.remove(i);
                    adapter.notifyItemRemoved(i);
                }
            }
        }
        if (facePreviewInfoList == null || facePreviewInfoList.size() == 0) {
            requestFeatureStatusMap.clear();
            livenessMap.clear();
            return;
        }

        for (Integer integer : keySet) {
            boolean contained = false;
            for (FacePreviewInfo facePreviewInfo : facePreviewInfoList) {
                if (facePreviewInfo.getTrackId() == integer) {
                    contained = true;
                    break;
                }
            }
            if (!contained) {
                requestFeatureStatusMap.remove(integer);
                livenessMap.remove(integer);
            }
        }

    }

    private void searchFace(final FaceFeature frFace, final Integer requestId) {
        Observable
                .create(new ObservableOnSubscribe<CompareResult>() {
                    @Override
                    public void subscribe(ObservableEmitter<CompareResult> emitter) {
//                        Log.i(TAG, "subscribe: fr search start = " + System.currentTimeMillis() + " trackId = " + requestId);
                        //人脸对比结果
                        CompareResult compareResult = FaceServer.getInstance().getTopOfFaceLib(RegisterAndRecognizeActivity.this, frFace);
                        Log.i(TAG, "subscribe: fr search end = " + System.currentTimeMillis() + " trackId = " + requestId);
                        if (compareResult == null) {
                            emitter.onError(null);
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Toast.makeText(RegisterAndRecognizeActivity.this, "识别出现异常，请退出重新进入本页面", Toast.LENGTH_SHORT).show();
//                                }
//                            });

                        } else {
                            emitter.onNext(compareResult);
                        }
                    }
                })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CompareResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(CompareResult compareResult) {
                        if (compareResult == null || compareResult.getUserName() == null) {
                            requestFeatureStatusMap.put(requestId, RequestFeatureStatus.FAILED);
                            faceHelper.addName(requestId, "VISITOR " + requestId);
                            return;
                        }

                        Log.i(TAG, "onNext: fr search get result  = " + System.currentTimeMillis() + " trackId = " + requestId + "  similar = " + compareResult.getSimilar());
                        Log.i(TAG, "onNext: fr search get result : similar = " + compareResult.getSimilar() + " userName = " + compareResult.getUserName());

                        if (compareResult.getSimilar() > SIMILAR_THRESHOLD) {//识别相似度大于80%
                            boolean isAdded = false;
                            if (compareResultList == null) {
                                requestFeatureStatusMap.put(requestId, RequestFeatureStatus.FAILED);
                                faceHelper.addName(requestId, "VISITOR " + requestId);
                                return;
                            }
                            for (CompareResult compareResult1 : compareResultList) {
                                if (compareResult1.getTrackId() == requestId) {
                                    isAdded = true;
                                    break;
                                }
                            }
                            if (!isAdded) {
                                //对于多人脸搜索，假如最大显示数量为 MAX_DETECT_NUM 且有新的人脸进入，则以队列的形式移除
                                if (compareResultList.size() >= MAX_DETECT_NUM) {
                                    compareResultList.remove(0);
                                    adapter.notifyItemRemoved(0);
                                }
                                //添加显示人员时，保存其trackId
                                compareResult.setTrackId(requestId);
                                compareResultList.add(compareResult);
                                adapter.notifyItemInserted(compareResultList.size() - 1);
                            }
                            requestFeatureStatusMap.put(requestId, RequestFeatureStatus.SUCCEED);
//                            faceHelper.addName(requestId, compareResult.getUserName());
                            faceHelper.addName(requestId, "");
                            ToastUtils.msg("识别成功,即将进入主页");
                            FaceServer.getInstance().faceCheckSuccess(RegisterAndRecognizeActivity.this);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    IntentUtils.openActivity(RegisterAndRecognizeActivity.this, LoginActivity.class, "type", "1");
                                    finish();
                                }
                            }, 2000);

                        } else {//识别失败
                            requestFeatureStatusMap.put(requestId, RequestFeatureStatus.FAILED);
//                            faceHelper.addName(requestId, "VISITOR " + requestId);
                            faceHelper.addName(requestId, "");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        requestFeatureStatusMap.put(requestId, RequestFeatureStatus.FAILED);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    /**
     * 将准备注册的状态置为{@link #REGISTER_STATUS_READY}
     *
     * @param view 注册按钮
     */
    public void register(View view) {
        if (registerStatus == REGISTER_STATUS_DONE) {
            registerStatus = REGISTER_STATUS_READY;
        }
    }

    public void setTranslucentStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//安卓版本大于4.4
            Window window = getWindow();
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.flags |= WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            window.setAttributes(layoutParams);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

