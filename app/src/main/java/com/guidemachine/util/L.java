package com.guidemachine.util;

import android.text.TextUtils;
import android.util.Log;


import com.guidemachine.constant.SF;

import java.io.File;
import java.nio.charset.StandardCharsets;

/** 日志 工具类 */
public class L {

    private static L mlog; // 本类实例
    private boolean isDebug = true;// 是否需要打印bug，可以在application的onCreate函数里面初始化
    private final String TAG = "com.guidemachine";

    private L() {
    }

    /** 获取 LogUtils 的实例 */
    public static L gi() {
        if (mlog == null) {
            synchronized (L.class) {
                if (mlog == null) {
                    mlog = new L();
                }
            }
        }
        return mlog;
    }

    public boolean isDebug() {
        return isDebug;
    }

    public void setDebug(boolean isDebug) {
        this.isDebug = isDebug;
    }

    public String getTag() {
        return TAG;
    }

    public void v(String msg) {
        if (isDebug)
            Log.v(TAG, msg);
    }

    public void d(String msg) {

            Log.d(TAG, msg);
    }

    public void i(String msg) {
        if (isDebug)
            Log.i(TAG, msg);
    }

    public void w(String msg) {
        if (isDebug)
            Log.w(TAG, msg);
    }

    public void e(String msg) {
        if (isDebug)
            Log.e(TAG, msg);
    }

    public void v(String tag, String msg) {
        if (isDebug)
            Log.i(tag, msg);
    }

    public void d(String tag, String msg) {
        if (isDebug)
            Log.i(tag, msg);
    }

    public void i(String tag, String msg) {
        if (isDebug)
            Log.i(tag, msg);
    }

    public void w(String tag, String msg) {
        if (isDebug)
            Log.i(tag, msg);
    }

    public void e(String tag, String msg) {
        if (isDebug)
            Log.i(tag, msg);
    }

    /** 写日志 同步 */
    public void writeLog(String msg) {
        writeLog(SF.PATH_FOLDER_LOG, null, msg, true);
    }

    /** 写日志 同步 */
    public void writeLog(String path, String fileNameParam, String msgParam, boolean isSplicingDateParam) {
        if (!isDebug)
            return;
        writeLogFile(path, fileNameParam, msgParam, isSplicingDateParam);
    }

    /** 写日志 异步 */
    public void writeLogAsync(String msg) {
        writeLogAsync(SF.PATH_FOLDER_LOG, null, msg, true);
    }

    /** 写日志 异步 */
    public void writeLogAsync(String path, String fileNameParam, String msgParam, boolean isSplicingDateParam) {
        if (!isDebug)
            return;
//        new Thread(() -> writeLogFile(path, fileNameParam, msgParam, isSplicingDateParam)).start();
    }

    /** 写日志到本地文件 */
    private void writeLogFile(String path, String fileName, String msg, boolean isSplicingDateParam) {
        try {
            boolean isSplicingDate = TextUtils.isEmpty(fileName) || isSplicingDateParam;

            if (!TextUtils.isEmpty(fileName) && isSplicingDate) {
                fileName = fileName + SF.S_UNDERLINE + DateTimeUtils.getInstance().getNowTime(SF.DT_003);
            } else if (TextUtils.isEmpty(fileName) && isSplicingDate) {
                fileName = DateTimeUtils.getInstance().getNowTime(SF.DT_003);
            }

            File file = new File(path + SF.S_FS + fileName + SF.FILE_DT$LOG);
            msg = "【" + DateTimeUtils.getInstance().getNowTime(SF.DT_001) + "】" + SF.S_LN + msg + SF.S_LN + SF.S_LN;
//            FileUtils.writeStringToFile(file, msg, StandardCharsets.UTF_8.name(), true);
            d(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}