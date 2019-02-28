package com.guidemachine.util.share;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by admin on 2015/7/13.
 */
public class SharedPreferencesWrapper {
    private SharedPreferences sharedPreferences;
    SharedPreferencesWrapper(Context context){
        try {
            sharedPreferences=context.getSharedPreferences("guidemachine", Context.MODE_PRIVATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void putString(String key, String value){
        sharedPreferences.edit().putString(key,value).commit();
    }
    public String getString(String key){
        return sharedPreferences.getString(key,"");
    }


    protected void setBoolean(String key, boolean vaule){
        sharedPreferences.edit().putBoolean(key,vaule).commit();
    }

    protected boolean getBoolean(String key, boolean defaultVaule){
        return sharedPreferences.getBoolean(key,defaultVaule);
    }
    /**
     * 清除share文件
     */
    public void clear(){
        sharedPreferences.edit().commit();
    }
}
