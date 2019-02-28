package com.guidemachine.service.entity;

import java.io.Serializable;

public class AdminStatisticsBean implements Serializable{

    /**
     * date : 2018-12-26
     * time : 10
     * totalNum : 30
     * onlineNum : 1
     * offlineNum : 29
     * rentNum : 0
     */

    private String totalRentNum;
    private String date;
    private String time;
    private int totalNum;
    private int onlineNum;
    private int offlineNum;
    private int rentNum;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public int getOnlineNum() {
        return onlineNum;
    }

    public void setOnlineNum(int onlineNum) {
        this.onlineNum = onlineNum;
    }

    public int getOfflineNum() {
        return offlineNum;
    }

    public void setOfflineNum(int offlineNum) {
        this.offlineNum = offlineNum;
    }

    public int getRentNum() {
        return rentNum;
    }

    public void setRentNum(int rentNum) {
        this.rentNum = rentNum;
    }

    public String getTotalRentNum() {
        return totalRentNum;
    }

    public void setTotalRentNum(String totalRentNum) {
        this.totalRentNum = totalRentNum;
    }

    @Override
    public String toString() {
        return "AdminStatisticsBean{" +
                "totalRentNum='" + totalRentNum + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", totalNum=" + totalNum +
                ", onlineNum=" + onlineNum +
                ", offlineNum=" + offlineNum +
                ", rentNum=" + rentNum +
                '}';
    }
}
