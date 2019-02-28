package com.guidemachine.face.faceserver;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.util.Log;

import com.arcsoft.face.ErrorInfo;
import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.FaceFeature;
import com.arcsoft.face.FaceInfo;
import com.arcsoft.face.FaceSimilar;
import com.guidemachine.face.model.FaceRegisterInfo;
import com.guidemachine.service.entity.BaseBean;
import com.guidemachine.service.entity.FaceRegisterBean;
import com.guidemachine.service.presenter.AllFacePresenter;
import com.guidemachine.service.presenter.BookPresenter;
import com.guidemachine.service.presenter.PostFaceCheckCompletePresenter;
import com.guidemachine.service.view.AllFaceView;
import com.guidemachine.service.view.BaseView;
import com.guidemachine.service.view.BookView;
import com.guidemachine.util.ImageUtil;
import com.guidemachine.util.Logger;
import com.guidemachine.util.MobileInfoUtil;
import com.guidemachine.util.ToastUtils;
import com.guidemachine.util.qiniuutils.Auth;
import com.guidemachine.util.share.SPHelper;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 人脸库操作类，包含注册和搜索
 */
public class FaceServer {
    private static final String TAG = "FaceServer";
    public static final String IMG_SUFFIX = ".jpg";
    private static FaceEngine faceEngine = null;
    private static FaceServer faceServer = null;
    private static List<FaceRegisterInfo> faceRegisterInfoList;
    private static List<FaceRegisterBean> faceRegisterBeanList;
    public static String ROOT_PATH;
    public static final String SAVE_IMG_DIR = "register" + File.separator + "imgs";
    private static final String SAVE_FEATURE_DIR = "register" + File.separator + "features";
    private BookPresenter mBookPresenter;
    AllFacePresenter mAllFacePresenter;
    //以下是上传头像图片到七牛云所用参数
    private static String AccessKey = "eedQh_OseJoXX5OTAKgdqUkml0gGc4XUEfJ3E1lM";//此处填你自己的AccessKey
    private static String SecretKey = "8h1T6qPJWqhtGpTUSy9TMRtNi7yPYNTOJFMDdbqR";//此处填你自己的SecretKey
    /**
     * 是否正在搜索人脸，保证搜索操作单线程进行
     */
    private boolean isProcessing = false;
    private PostFaceCheckCompletePresenter postFaceCheckCompletePresenter;

    public static FaceServer getInstance() {
        if (faceServer == null) {
            synchronized (FaceServer.class) {
                if (faceServer == null) {
                    faceServer = new FaceServer();
                }
            }
        }
        return faceServer;
    }

    /**
     * 初始化
     *
     * @param context 上下文对象
     * @return 是否初始化成功
     */
    public boolean init(Context context) {
        synchronized (this) {
            if (faceEngine == null && context != null) {
                faceEngine = new FaceEngine();
                int engineCode = faceEngine.init(context, FaceEngine.ASF_DETECT_MODE_IMAGE, FaceEngine.ASF_OP_0_HIGHER_EXT, 16, 1, FaceEngine.ASF_FACE_RECOGNITION | FaceEngine.ASF_FACE_DETECT);
                if (engineCode == ErrorInfo.MOK) {
//                    initFaceList(context);
                    mAllFacePresenter = new AllFacePresenter(context);
                    mAllFacePresenter.onCreate();
                    mAllFacePresenter.attachView(allFaceView);
                    mAllFacePresenter.getAllFace();
                    Log.e(TAG, "成功 init: failed! code = " + engineCode);
                    return true;
                } else {
                    faceEngine = null;
                    Log.e(TAG, "失败 init: failed! code = " + engineCode);
                    return false;
                }
            }
            return false;
        }
    }

    /**
     * 销毁
     */
    public void unInit() {
        synchronized (this) {
            if (faceRegisterInfoList != null) {
                faceRegisterInfoList.clear();
                faceRegisterInfoList = null;
            }
            if (faceEngine != null) {
                faceEngine.unInit();
                faceEngine = null;
            }
        }
    }

    /**
     * 初始化人脸特征数据以及人脸特征数据对应的注册图
     *
     * @param context 上下文对象
     */
    private void initFaceList(Context context) {
        synchronized (this) {
            if (ROOT_PATH == null) {
                ROOT_PATH = context.getFilesDir().getAbsolutePath();
            }
            File featureDir = new File(ROOT_PATH + File.separator + SAVE_FEATURE_DIR);
            if (!featureDir.exists() || !featureDir.isDirectory()) {
                return;
            }
            File[] featureFiles = featureDir.listFiles();
            if (featureFiles == null || featureFiles.length == 0) {
                return;
            }
            faceRegisterInfoList = new ArrayList<>();
            for (File featureFile : featureFiles) {
                try {
                    FileInputStream fis = new FileInputStream(featureFile);
                    byte[] feature = new byte[FaceFeature.FEATURE_SIZE];
                    fis.read(feature);
                    fis.close();
                    faceRegisterInfoList.add(new FaceRegisterInfo(feature, featureFile.getName()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public int getFaceNumber(Context context) {
        synchronized (this) {
            if (context == null) {
                return 0;
            }
            if (ROOT_PATH == null) {
                ROOT_PATH = context.getFilesDir().getAbsolutePath();
            }

            File featureFileDir = new File(ROOT_PATH + File.separator + SAVE_FEATURE_DIR);
            int featureCount = 0;
            if (featureFileDir.exists() && featureFileDir.isDirectory()) {
                String[] featureFiles = featureFileDir.list();
                featureCount = featureFiles == null ? 0 : featureFiles.length;
            }
            int imageCount = 0;
            File imgFileDir = new File(ROOT_PATH + File.separator + SAVE_IMG_DIR);
            if (imgFileDir.exists() && imgFileDir.isDirectory()) {
                String[] imageFiles = imgFileDir.list();
                imageCount = imageFiles == null ? 0 : imageFiles.length;
            }
            return featureCount > imageCount ? imageCount : featureCount;
        }
    }

    public int clearAllFaces(Context context) {
        synchronized (this) {
            if (context == null) {
                return 0;
            }
            if (ROOT_PATH == null) {
                ROOT_PATH = context.getFilesDir().getAbsolutePath();
            }
            if (faceRegisterInfoList != null) {
                faceRegisterInfoList.clear();
            }
            File featureFileDir = new File(ROOT_PATH + File.separator + SAVE_FEATURE_DIR);
            int deletedFeatureCount = 0;
            if (featureFileDir.exists() && featureFileDir.isDirectory()) {
                File[] featureFiles = featureFileDir.listFiles();
                if (featureFiles != null && featureFiles.length > 0) {
                    for (File featureFile : featureFiles) {
                        if (featureFile.delete()) {
                            deletedFeatureCount++;
                        }
                    }
                }
            }
            int deletedImageCount = 0;
            File imgFileDir = new File(ROOT_PATH + File.separator + SAVE_IMG_DIR);
            if (imgFileDir.exists() && imgFileDir.isDirectory()) {
                File[] imgFiles = imgFileDir.listFiles();
                if (imgFiles != null && imgFiles.length > 0) {
                    for (File imgFile : imgFiles) {
                        if (imgFile.delete()) {
                            deletedImageCount++;
                        }
                    }
                }
            }
            return deletedFeatureCount > deletedImageCount ? deletedImageCount : deletedFeatureCount;
        }
    }

    /**
     * 注册人脸
     *
     * @param context 上下文对象
     * @param nv21    NV21数据
     * @param width   NV21宽度
     * @param height  NV21高度
     * @param name    保存的名字，可为空
     * @return 是否注册成功
     */
    public boolean register(Context context, byte[] nv21, int width, int height, String name) {
        synchronized (this) {
            if (faceEngine == null || context == null || nv21 == null || width % 4 != 0 || nv21.length != width * height * 3 / 2) {
                return false;
            }

            if (ROOT_PATH == null) {
                ROOT_PATH = context.getFilesDir().getAbsolutePath();
            }
            boolean dirExists = true;
            //特征存储的文件夹
            File featureDir = new File(ROOT_PATH + File.separator + SAVE_FEATURE_DIR);
            if (!featureDir.exists()) {
                dirExists = featureDir.mkdirs();
            }
            if (!dirExists) {
                return false;
            }
            //图片存储的文件夹
            File imgDir = new File(ROOT_PATH + File.separator + SAVE_IMG_DIR);
            Log.d("图片存储的路径", ROOT_PATH + File.separator + SAVE_IMG_DIR);
            if (!imgDir.exists()) {
                dirExists = imgDir.mkdirs();
            }
            if (!dirExists) {
                return false;
            }
            //1.人脸检测
            List<FaceInfo> faceInfoList = new ArrayList<>();
            int code = faceEngine.detectFaces(nv21, width, height, FaceEngine.CP_PAF_NV21, faceInfoList);
            if (code == ErrorInfo.MOK && faceInfoList.size() > 0) {
                FaceFeature faceFeature = new FaceFeature();

                //2.特征提取
                code = faceEngine.extractFaceFeature(nv21, width, height, FaceEngine.CP_PAF_NV21, faceInfoList.get(0), faceFeature);
                String userName = name == null ? String.valueOf(System.currentTimeMillis()) : name;
                try {
                    //3.保存注册结果（注册图、特征数据）
                    if (code == ErrorInfo.MOK) {
                        YuvImage yuvImage = new YuvImage(nv21, ImageFormat.NV21, width, height, null);
                        //为了美观，扩大rect截取注册图
                        Rect cropRect = getBestRect(width, height, faceInfoList.get(0).getRect());
                        if (cropRect == null) {
                            return false;
                        }
                        File file = new File(imgDir + File.separator + userName + IMG_SUFFIX);
                        FileOutputStream fosImage = new FileOutputStream(file);
                        yuvImage.compressToJpeg(cropRect, 100, fosImage);
                        fosImage.close();
                        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                        Log.d("注册路径", file.getAbsolutePath());
                        //判断人脸旋转角度，若不为0度则旋转注册图
                        boolean needAdjust = false;
                        if (bitmap != null) {
                            switch (faceInfoList.get(0).getOrient()) {
                                case FaceEngine.ASF_OC_0:
                                    break;
                                case FaceEngine.ASF_OC_90:
                                    bitmap = ImageUtil.getRotateBitmap(bitmap, 90);
                                    needAdjust = true;
                                    break;
                                case FaceEngine.ASF_OC_180:
                                    bitmap = ImageUtil.getRotateBitmap(bitmap, 180);
                                    needAdjust = true;
                                    break;
                                case FaceEngine.ASF_OC_270:
                                    bitmap = ImageUtil.getRotateBitmap(bitmap, 270);
                                    needAdjust = true;
                                    break;
                                default:
                                    break;
                            }
                        }
                        if (needAdjust) {
                            fosImage = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fosImage);
                            fosImage.close();
                        }

                        FileOutputStream fosFeature = new FileOutputStream(featureDir + File.separator + userName);
                        fosFeature.write(faceFeature.getFeatureData());
                        fosFeature.close();

                        //内存中的数据同步
                        if (faceRegisterInfoList == null) {
                            faceRegisterInfoList = new ArrayList<>();
                        }
                        //将人脸特征存入集合
//                        faceRegisterInfoList.add(new FaceRegisterInfo(faceFeature.getFeatureData(), userName));
                        //-------------------------------------------------------------------------------------------------
                        FaceRegisterInfo faceRegisterInfo = new FaceRegisterInfo();
                        faceRegisterInfo.setFeatureData(faceFeature.getFeatureData());
                        faceRegisterInfo.setName(userName);

                        uploadImg2QiNiu(context, file.getAbsolutePath(), faceRegisterInfo);
                        return true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return false;
        }

    }

    private void uploadImg2QiNiu(final Context context, String picPath, final FaceRegisterInfo faceRegisterInfo) {//上传头像到七牛云
        UploadManager uploadManager = new UploadManager();
        // 设置图片名字
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String key = "guidemachine_" + sdf.format(new Date());
//         picPath = getOutputMediaFile().toString();
        Log.i(TAG, "picPath: " + picPath);

        uploadManager.put(picPath, key, Auth.create(AccessKey, SecretKey).uploadToken("zhqylandarea"), new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject res) {
                // info.error中包含了错误信息，可打印调试
                // 上传成功后将key值上传到自己的服务器
                if (info.isOK()) {
                    Log.i(TAG, "token===" + Auth.create(AccessKey, SecretKey).uploadToken("photo"));
                    String headpicPath = "http://tomepicture.zhihuiquanyu.com/" + key;
                    Log.i(TAG, "注册路径: " + headpicPath);
                    mBookPresenter = new BookPresenter(context);
                    mBookPresenter.onCreate();
                    mBookPresenter.attachView(mBookView);
                    JSONObject requestData = new JSONObject();
                    try {
                        requestData.put("faceBytes", toHexString(faceRegisterInfo.getFeatureData()));
                        requestData.put("faceUrl", headpicPath);
                        requestData.put("name", faceRegisterInfo.getName());
                        Log.d("注册路径请求参数", headpicPath);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestData.toString());
                    mBookPresenter.registerAppUser(requestBody);
                }
                //上传至阡陌链接
                //     uploadpictoQianMo(headpicPath, picPath);

            }
        }, null);
    }

    private BookView mBookView = new BookView() {
        @Override
        public void onSuccess(BaseBean mBook) {
            Log.d("注册路径", mBook.toString());
        }

        @Override
        public void onError(String result) {
            Log.d("注册路径", result.toString());
        }
    };
    AllFaceView allFaceView = new AllFaceView() {
        @Override
        public void onSuccess(BaseBean<List<FaceRegisterBean>> mList) {
            if (mList.getValue() != null && mList.getValue().size() != 0) {

                faceRegisterBeanList = new ArrayList<>();
                for (int i = 0; i < mList.getValue().size(); i++) {
                    Log.d("打印图片路径", mList.getValue().get(i).getFaceUrl() + "");
                    faceRegisterBeanList.add(new FaceRegisterBean(mList.getValue().get(i).getFaceBytes(), mList.getValue().get(i).getName(), mList.getValue().get(i).getId(), mList.getValue().get(i).getFaceUrl()));
                }
            }
        }

        @Override
        public void onError(String result) {
            Log.d("注册路径", result.toString());
        }
    };

    /**
     * 在特征库中搜索
     *
     * @param faceFeature 传入特征数据
     * @return 比对结果
     */
    public CompareResult getTopOfFaceLib(Context context, FaceFeature faceFeature) {
        if (faceEngine == null || isProcessing || faceFeature == null || faceRegisterBeanList == null || faceRegisterBeanList.size() == 0) {
            Log.d("注册路径0：", "空的" + faceEngine + "   " + isProcessing + "   " + faceFeature + "  " + faceRegisterBeanList + "  " + faceRegisterBeanList.size());
            return null;
        }
        FaceFeature tempFaceFeature = new FaceFeature();
        FaceSimilar faceSimilar = new FaceSimilar();
        float maxSimilar = 0;
        int maxSimilarIndex = -1;
        isProcessing = true;
        for (int i = 0; i < faceRegisterBeanList.size(); i++) {
            tempFaceFeature.setFeatureData(toByteArray(faceRegisterBeanList.get(i).getFaceBytes()));
            Log.d("注册路径1", faceRegisterBeanList.get(i).getFaceBytes() + "");
            Log.d("注册路径2", toHexString(faceFeature.getFeatureData()) + "");
            //比较两个人脸特征
            faceEngine.compareFaceFeature(faceFeature, tempFaceFeature, faceSimilar);
            if (faceSimilar.getScore() > maxSimilar) {
                maxSimilar = faceSimilar.getScore();
                maxSimilarIndex = i;
                Log.d("注册路径3", maxSimilar + "");
                Log.d("注册路径4", maxSimilarIndex + "");
                SPHelper.getInstance(context).setUserId(faceRegisterBeanList.get(maxSimilarIndex).getId());
                SPHelper.getInstance(context).setMaxSimilarIndex(maxSimilarIndex+"");
            }
            Log.d("注册路径4", SPHelper.getInstance(context).getUserId() + "");
        }
        isProcessing = false;
        if (maxSimilarIndex != -1) {
            return new CompareResult(faceRegisterBeanList.get(maxSimilarIndex).getName(), maxSimilar);
        }
        return null;
    }

    public void faceCheckSuccess(Context context) {//人脸识别成功
        postFaceCheckCompletePresenter = new PostFaceCheckCompletePresenter(context);
        postFaceCheckCompletePresenter.onCreate();
        postFaceCheckCompletePresenter.attachView(baseView);
        int id = Integer.parseInt(SPHelper.getInstance(context).getMaxSimilarIndex());
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("faceId", faceRegisterBeanList.get(id).getId());
            requestData.put("faceUrl", faceRegisterBeanList.get(id).getFaceUrl());
            requestData.put("imei", MobileInfoUtil.getIMEI(context));
            Logger.d("人脸识别成功imei",MobileInfoUtil.getIMEI(context));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestData.toString());
        postFaceCheckCompletePresenter.postFaceCheckComplete(requestBody);
    }

    BaseView baseView = new BaseView() {
        @Override
        public void onSuccess(BaseBean mBaseBean) {
//            ToastUtils.msg(mBaseBean.getResultStatus().getResultMessage().toString());
        }

        @Override
        public void onError(String result) {
//            ToastUtils.msg(result);
        }
    };

    /**
     * 将图像中需要截取的Rect向外扩张一倍，若扩张一倍会溢出，则扩张到边界，若Rect已溢出，则收缩到边界
     *
     * @param width   图像宽度
     * @param height  图像高度
     * @param srcRect 原Rect
     * @return 调整后的Rect
     */
    private static Rect getBestRect(int width, int height, Rect srcRect) {

        if (srcRect == null) {
            return null;
        }
        Rect rect = new Rect(srcRect);
        //1.原rect边界已溢出宽高的情况
        int maxOverFlow = 0;
        int tempOverFlow = 0;
        if (rect.left < 0) {
            maxOverFlow = -rect.left;
        }
        if (rect.top < 0) {
            tempOverFlow = -rect.top;
            if (tempOverFlow > maxOverFlow) {
                maxOverFlow = tempOverFlow;
            }
        }
        if (rect.right > width) {
            tempOverFlow = rect.right - width;
            if (tempOverFlow > maxOverFlow) {
                maxOverFlow = tempOverFlow;
            }
        }
        if (rect.bottom > height) {
            tempOverFlow = rect.bottom - height;
            if (tempOverFlow > maxOverFlow) {
                maxOverFlow = tempOverFlow;
            }
        }
        if (maxOverFlow != 0) {
            rect.left += maxOverFlow;
            rect.top += maxOverFlow;
            rect.right -= maxOverFlow;
            rect.bottom -= maxOverFlow;
            return rect;
        }
        //1.原rect边界未溢出宽高的情况
        int padding = rect.height() / 2;
        //若以此padding扩张rect会溢出，取最大padding为四个边距的最小值
        if (!(rect.left - padding > 0 && rect.right + padding < width && rect.top - padding > 0 && rect.bottom + padding < height)) {
            padding = Math.min(Math.min(Math.min(rect.left, width - rect.right), height - rect.bottom), rect.top);
        }

        rect.left -= padding;
        rect.top -= padding;
        rect.right += padding;
        rect.bottom += padding;
        return rect;
    }


    /**
     * 16进制的字符串表示转成字节数组
     *
     * @param hexString 16进制格式的字符串
     * @return 转换后的字节数组
     **/
    public static byte[] toByteArray(String hexString) {
        hexString = hexString.toLowerCase();
        final byte[] byteArray = new byte[hexString.length() / 2];
        int k = 0;
        for (int i = 0; i < byteArray.length; i++) {// 因为是16进制，最多只会占用4位，转换成字节需要两个16进制的字符，高位在先
            byte high = (byte) (Character.digit(hexString.charAt(k), 16) & 0xff);
            byte low = (byte) (Character.digit(hexString.charAt(k + 1), 16) & 0xff);
            byteArray[i] = (byte) (high << 4 | low);
            k += 2;
        }
        return byteArray;
    }

    /**
     * 字节数组转成16进制表示格式的字符串
     *
     * @param byteArray 需要转换的字节数组
     * @return 16进制表示格式的字符串
     **/
    public static String toHexString(byte[] byteArray) {
        String str = null;
        if (byteArray != null && byteArray.length > 0) {
            StringBuffer stringBuffer = new StringBuffer(byteArray.length);
            for (byte byteChar : byteArray) {
                stringBuffer.append(String.format("%02X", byteChar));
            }
            str = stringBuffer.toString();
        }
        return str;
    }

}
