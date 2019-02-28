package com.guidemachine.util;

import android.widget.Toast;

import com.App;


/**
 * Created by clw on 15.12.17.
 */
public class ToastUtils {

    public static void msg(String msg) {
        Toast.makeText(App.getAppContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public static void longMsg(String msg) {
        Toast.makeText(App.getAppContext(), msg, Toast.LENGTH_LONG).show();
    }
}
