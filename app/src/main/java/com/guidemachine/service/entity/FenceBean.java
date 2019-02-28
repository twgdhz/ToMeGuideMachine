package com.guidemachine.service.entity;

import java.io.Serializable;

public class FenceBean implements Serializable{

    /**
     * id : 1
     * sceneryId : 1
     * sceneryName : 青城山
     * scenerySpotName : 慈光阁
     * name : 测试围栏1
     * type : 1
     * enabled : 1
     * hintMode : 1
     * inHintWord : 进入提示
     * outHintWord : 退出提示
     * coordinate : 116.291585_39.98109,116.466935_39.981533,116.301359_39.851374,116.470384_39.850045
     */

    private int id;
    private int sceneryId;
    private String sceneryName;
    private String scenerySpotName;
    private String name;
    private int type;
    private int enabled;
    private int hintMode;
    private String inHintWord;
    private String outHintWord;
    private String coordinate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSceneryId() {
        return sceneryId;
    }

    public void setSceneryId(int sceneryId) {
        this.sceneryId = sceneryId;
    }

    public String getSceneryName() {
        return sceneryName;
    }

    public void setSceneryName(String sceneryName) {
        this.sceneryName = sceneryName;
    }

    public String getScenerySpotName() {
        return scenerySpotName;
    }

    public void setScenerySpotName(String scenerySpotName) {
        this.scenerySpotName = scenerySpotName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    public int getHintMode() {
        return hintMode;
    }

    public void setHintMode(int hintMode) {
        this.hintMode = hintMode;
    }

    public String getInHintWord() {
        return inHintWord;
    }

    public void setInHintWord(String inHintWord) {
        this.inHintWord = inHintWord;
    }

    public String getOutHintWord() {
        return outHintWord;
    }

    public void setOutHintWord(String outHintWord) {
        this.outHintWord = outHintWord;
    }

    public String getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate;
    }

    @Override
    public String toString() {
        return "FenceBean{" +
                "id=" + id +
                ", sceneryId=" + sceneryId +
                ", sceneryName='" + sceneryName + '\'' +
                ", scenerySpotName='" + scenerySpotName + '\'' +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", enabled=" + enabled +
                ", hintMode=" + hintMode +
                ", inHintWord='" + inHintWord + '\'' +
                ", outHintWord='" + outHintWord + '\'' +
                ", coordinate='" + coordinate + '\'' +
                '}';
    }
}
