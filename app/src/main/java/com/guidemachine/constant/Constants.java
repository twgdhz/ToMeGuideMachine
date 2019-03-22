package com.guidemachine.constant;

import android.os.Environment;

public class Constants {
    //虹软人脸识别相关
    public static final String APP_ID = "HiAfvuU2Sfrybrj3xPnz5thucEhNexYagW7hCuyJyqcb";
    public static final String SDK_KEY = "4hFSRQB1NXRhfRQDQmkJRa5ZNVBGzUANimV9Vo3zbmog";
    public static final String SUCCESS_CODE = "0000";
    /* 空参数 */
    public static final String RE_CODE_0001 = "0001";
    /* 参数异常 */
    public static final String RE_CODE_0002 = "0002";
    /* 服务内部执行异常 */
    public static final String RE_CODE_0003 = "0003";
    /* 没有业务参数 */
    public static final String RE_CODE_0004 = "0004";
    /* 失败 */
    public static final String RE_CODE_0005 = "0005";
    //网关错误
    public static final String RE_CODE_0006 = "0006";
    //认证失败
    public static final String RE_CODE_0007 = "0007";
    //没有权限
    public static final String RE_CODE_0008 = "0008";
    //feign 出现异常
    public static final String RE_CODE_0009 = "0009";

    /**
     * 轨迹服务ID
     */
    public static final long serviceId = 207386;
//    public static final long serviceId = 208999;//测试

    /**
     * Entity标识
     */
    public static final String entityName = "myTrace";

    //当前手机IMEI号
    public static String mImei = "";
    public static final String EXTRA_KEY_CONFIRM="android.intent.extra.KEY_CONFIRM";
    public static final String ACTION_REQUEST_SHUTDOWN="android.intent.action.ACTION_REQUEST_SHUTDOWN";

    public static final byte VISE_COMMAND_HEAD_FLAG_36 = (byte) 0x36;//帧头

    public static final byte VISE_COMMAND_HEAD_FLAG_77 = (byte) 0x77;//命令类型

    public static final byte VISE_COMMAND_HEAD_FLAG_01 = (byte) 0x01;
    public static final byte VISE_COMMAND_HEAD_FLAG_02 = (byte) 0x02;
    public static final byte VISE_COMMAND_HEAD_FLAG_03 = (byte) 0x03;
    public static final byte VISE_COMMAND_HEAD_FLAG_04 = (byte) 0x04;
    public static final byte VISE_COMMAND_HEAD_FLAG_05 = (byte) 0x05;

    public static final String SERIAL_CODE_01 = "36020101";
    public static final String SERIAL_CODE_03 = "36027703";

}
