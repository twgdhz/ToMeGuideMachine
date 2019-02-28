package com.guidemachine.util;

import android.content.Context;

import com.guidemachine.util.share.SPHelper;

/**
 * 是否登录工具类
 */

public class IsLoginUtils {//判断是否登录

    private static IsLoginUtils isLoginUtils;


    public static IsLoginUtils getInstence() {
        if (isLoginUtils == null) {
            isLoginUtils = new IsLoginUtils();
        }
        return isLoginUtils;
    }

    public boolean isLogin(Context context) {
        if (SPHelper.getInstance(context).getToken() == null || SPHelper.getInstance(context).getToken().equals("")) {
            return false;
        } else {
            return true;
        }

    }
}
