package com.guidemachine.service.entity;

import java.io.Serializable;

public class AdminTouristTeamBean implements Serializable{

    /**
     * id : 1
     * name : 旅游团测试1
     * guideName : 导游10
     * guideTelephone : 15282297723
     * account : user21
     * totalNum : 8
     * onlineNum : 0
     * offlineNum : 8
     */

    private int id;
    private String name;
    private String guideName;
    private String guideTelephone;
    private String account;
    private int totalNum;
    private int onlineNum;
    private int offlineNum;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGuideName() {
        return guideName;
    }

    public void setGuideName(String guideName) {
        this.guideName = guideName;
    }

    public String getGuideTelephone() {
        return guideTelephone;
    }

    public void setGuideTelephone(String guideTelephone) {
        this.guideTelephone = guideTelephone;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
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

    @Override
    public String toString() {
        return "AdminTouristTeamBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", guideName='" + guideName + '\'' +
                ", guideTelephone='" + guideTelephone + '\'' +
                ", account='" + account + '\'' +
                ", totalNum=" + totalNum +
                ", onlineNum=" + onlineNum +
                ", offlineNum=" + offlineNum +
                '}';
    }
}
