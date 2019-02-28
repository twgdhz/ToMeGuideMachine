package com.guidemachine.service.entity;
/**
* @author ChenLinWang
* @email 422828518@qq.com
* @create 2018/12/26 0026 11:00
* description: 存储输入的围栏信息
*/
public class FencInfoBean {
    public String fenceName;
    public String fenceTpe;
    public String scenerySpot;
    public String inFenceNotice;
    public String outFenceNotice;

    public String getFenceName() {
        return fenceName;
    }

    public void setFenceName(String fenceName) {
        this.fenceName = fenceName;
    }

    public String getFenceTpe() {
        return fenceTpe;
    }

    public void setFenceTpe(String fenceTpe) {
        this.fenceTpe = fenceTpe;
    }

    public String getScenerySpot() {
        return scenerySpot;
    }

    public void setScenerySpot(String scenerySpot) {
        this.scenerySpot = scenerySpot;
    }

    public String getInFenceNotice() {
        return inFenceNotice;
    }

    public void setInFenceNotice(String inFenceNotice) {
        this.inFenceNotice = inFenceNotice;
    }

    public String getOutFenceNotice() {
        return outFenceNotice;
    }

    public void setOutFenceNotice(String outFenceNotice) {
        this.outFenceNotice = outFenceNotice;
    }

    @Override
    public String toString() {
        return "FencInfoBean{" +
                "fenceName='" + fenceName + '\'' +
                ", fenceTpe='" + fenceTpe + '\'' +
                ", scenerySpot='" + scenerySpot + '\'' +
                ", inFenceNotice='" + inFenceNotice + '\'' +
                ", outFenceNotice='" + outFenceNotice + '\'' +
                '}';
    }
}
