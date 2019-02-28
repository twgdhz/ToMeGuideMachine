package com.guidemachine.service.entity;

import java.io.Serializable;

public class SearchJourneyBean implements Serializable{

    /**
     * id : 28
     * tripType : 2
     * imei : 956680617111262
     * content : 大吉大利，今晚吃鸡！
     * createTime : 2019-01-03 15:31:25
     */

    private int id;
    private int tripType;
    private String imei;
    private String content;
    private String createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTripType() {
        return tripType;
    }

    public void setTripType(int tripType) {
        this.tripType = tripType;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "SearchJourneyBean{" +
                "id=" + id +
                ", tripType=" + tripType +
                ", imei='" + imei + '\'' +
                ", content='" + content + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
