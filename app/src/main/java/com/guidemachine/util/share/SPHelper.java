package com.guidemachine.util.share;

import android.content.Context;

public class SPHelper extends SharedPreferencesWrapper {
    private static SPHelper spHelper;

    SPHelper(Context context) {
        super(context);
    }

    public static SPHelper getInstance(Context context) {
        if (spHelper == null) {
            spHelper = new SPHelper(context);
        }
        return spHelper;
    }

    public boolean isFirst() {
        return getBoolean("isFirst", true);
    }


    public void setIsFirst(boolean isFirst) {
        setBoolean("isFirst", isFirst);
    }

    public void setUserId(String userId) {
        spHelper.putString("userId", userId);
    }

    public String getUserId() {
        return getString("userId");
    }

    public void setToken(String token) {
        spHelper.putString("token", token);
    }

    public String getToken() {
        return getString("token");
    }

    public void setCityName(String cityName) {
        spHelper.putString("cityName", cityName);
    }

    public String getCityName() {
        return getString("cityName");
    }

    public void setLongitude(String longitude) {
        spHelper.putString("longitude", longitude);
    }

    public String getLongitude() {
        return getString("longitude");
    }

    public void setLatitude(String latitude) {
        spHelper.putString("latitude", latitude);
    }

    public String getLatitude() {
        return getString("latitude");
    }

    public void setCodeId(String codeId) {
        spHelper.putString("codeId", codeId);
    }

    public String getCodeId() {
        return getString("codeId");
    }

    public void setOrderType(String orderType) {
        spHelper.putString("orderType", orderType);
    }

    public String getOrderType() {
        return getString("orderType");
    }
    public void setMaxSimilarIndex(String maxSimilarIndex) {
        spHelper.putString("maxSimilarIndex", maxSimilarIndex);
    }

    public String getMaxSimilarIndex() {
        return getString("maxSimilarIndex");
    }
    public String getSceneryId() {
        return getString("sceneryId");
    }
    public void setSceneryId(String sceneryId) {
        spHelper.putString("sceneryId", sceneryId);
    }
    public String getPhone() {
        return getString("phone");
    }
    public void setPhone(String phone) {
        spHelper.putString("phone", phone);
    }
    public String getHead() {
        return getString("head");
    }
    public void setHead(String head) {
        spHelper.putString("head", head);
    }

    public String getLocationMode() {
        return getString("locationMode");
    }
    public void setLocationMode(String locationMode) {
        spHelper.putString("locationMode", locationMode);
    }
    public String getNoticeContent() {
        return getString("noticeContent");
    }
    public void setNoticeContent(String noticeContent) {
        spHelper.putString("noticeContent", noticeContent);
    }

}
