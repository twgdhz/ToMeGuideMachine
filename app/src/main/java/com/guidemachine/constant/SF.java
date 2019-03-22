package com.guidemachine.constant;

import android.os.Environment;

public interface SF {

    /** 文件 日志 XXX.log */
    String FILE_DT$LOG = ".log";

    // <editor-fold> ===== 文件夹 ==================================================================
    /** 文件夹 SD 卡根目录 */
    String FOLDER_SD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();

    /** 文件夹 APP 外部存储 */
    String FOLDER_APP = "." + "com.guidemachine";

    /** 文件夹 日志存储 */
    String FOLDER_LOG = "log";

    /** 文件夹 图片存储 */
    String FOLDER_IMAGE_CACHE = "image";

    /** 文件夹 照片缓存 */
    String FOLDER_PHOTO_CACHE = "photo";

    /** 文件夹 下载存储 */
    String FOLDER_DOWNLOAD = "download";

    /** 文件夹 导出的apk目录 */
    String FOLDER_AAAAA_APK = "aaaaa_apk";
    // <editor-fold> ===== 符号 ====================================================================
    /** 符号 正斜杠 【forward slash = /】 */
    String S_FS = "/";

    /** 符号 换行 */
    String S_LN = "\n";

    /** 符号 换行 */
    String S_LNx10 = "\n\n\n\n\n\n\n\n\n\n";

    /** 符号 下划线 */
    String S_UNDERLINE = "_";

    /** 文件夹路径 日志目录 */
   String PATH_FOLDER_LOG = FOLDER_SD_PATH + S_FS + FOLDER_APP + S_FS + FOLDER_LOG;

    // <editor-fold> ===== 日期时间 格式 DateTime ===================================================
    /** DateTime 2016-07-08 09:10:11 */
    String DT_001 = "yyyy-MM-dd HH:mm:ss";

    /** DateTime 2016-07-08 09:10 */
    String DT_002 = "yyyy-MM-dd HH:mm";

    /** DateTime 2016-07-08 */
    String DT_003 = "yyyy-MM-dd";

    /** DateTime 09:10:11 */
    String DT_004 = "HH:mm:ss";

    /** DateTime 2016-07 */
    String DT_005 = "yyyy-MM";

    /** DateTime 07-08 */
    String DT_006 = "MM-dd";

    /** DateTime 09:10 */
    String DT_007 = "HH:mm";

    /** DateTime 10:11 */
    String DT_008 = "mm:ss";

    /** DateTime 2016 */
    String DT_009 = "yyyy";

    /** DateTime 07 */
    String DT_010 = "MM";

    /** DateTime 08 */
    String DT_011 = "dd";

    /** DateTime 09 */
    String DT_012 = "HH";

    /** DateTime 10 */
    String DT_013 = "mm";

    /** DateTime 11 */
    String DT_014 = "ss";

    /** DateTime 2016年07月08日 09时10分11秒 */
    String DT_015 = "yyyy年MM月dd日 HH时mm分ss秒";

    /** DateTime 2016年07月08日 09时10分 */
    String DT_016 = "yyyy年MM月dd日 HH时mm分";

    /** DateTime 2016年07月08日 */
    String DT_017 = "yyyy年MM月dd日";

    /** DateTime 09时10分11秒 */
    String DT_018 = "HH时mm分ss秒";

    /** DateTime 2016年07月 */
    String DT_019 = "yyyy年MM月";

    /** DateTime 07月08日 */
    String DT_020 = "MM月dd日";

    /** DateTime 09时10分 */
    String DT_021 = "HH时mm分";

    /** DateTime 10分11秒 */
    String DT_022 = "mm分ss秒";

    /** DateTime 2016年 */
    String DT_023 = "yyyy年";

    /** DateTime 07月 */
    String DT_024 = "MM月";

    /** DateTime 08日 */
    String DT_025 = "dd日";

    /** DateTime 09时 */
    String DT_026 = "HH时";

    /** DateTime 10分 */
    String DT_027 = "mm分";

    /** DateTime 11秒 */
    String DT_028 = "ss秒";
    // </editor-fold> ==============================================================================


    // <editor-fold> ===== 数字格式 NumberFormat ===================================================
    /** NumberFormat 123456789 */
    String NF_001 = "#0";

    /** NumberFormat 12345678.9 */
    String NF_002 = "#0.0";

    /** NumberFormat 1234567.89 */
    String NF_003 = "#0.00";

    /** NumberFormat 123456.789 */
    String NF_004 = "#0.000";

    /** NumberFormat 12345.6789 */
    String NF_005 = "#0.0000";

    /** NumberFormat 123,456,789 */
    String NF_006 = "#,##0";

    /** NumberFormat 12,345,678.9 */
    String NF_007 = "#,##0.0";

    /** NumberFormat 1,234,567.89 */
    String NF_008 = "#,##0.00";

    /** NumberFormat 123,456.789 */
    String NF_009 = "#,##0.000";

    /** NumberFormat 12,345.6789 */
    String NF_010 = "#,##0.0000";

    /** NumberFormat 00000001 */
    String NF_011 = "00000000";
    // </editor-fold> ==============================================================================


    // <editor-fold> ===== 正则规则 ================================================================
    /** 正则 手机号 */
    String REGEX_PHONE = "^[1][0-9]{10}$";

    /** 正则 银行卡 */
    String REGEX_BANK_CARD = "^[0-9]{16,19}$";

    /** 正则 姓名2-4位 */
    String REGEX_CHINA_NAME = "^([\u4e00-\u9fa5]{2,4})$";

    /** 正则 身份证号码 */
    String REGEX_ID_CARD = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$";

    /** 正则 邮箱 */
    String REGEX_MAILBOX = "^[A-Z0-9a-z._%+-]+@[A-Z0-9a-z.-]+\\.[A-Za-z]{2,4}$";

    /** 正则 数字 */
    String REGEX_NUMBER = "^[0-9]*$";

    /** 正则 字母 */
    String REGEX_ASC = "^[a-zA-Z]*$";
    // </editor-fold> =========================
}
