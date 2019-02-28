package com.guidemachine.service.entity;

import java.io.Serializable;

public class VisitHistoryBean implements Serializable{

    /**
     * id : 6
     * sceneryId : 111
     * scenerySpotId : 3
     * no : nono1
     * accessTime : 22222
     * codeMachine : 8758
     * imageUrl : http://pcpw0wxxu.bkt.clouddn.com/icon_20180928104320,http://pcpw0wxxu.bkt.clouddn.com/icon_20180928104320
     * url : www.baidu.com
     * scenerSpotName : 月城湖
     */

    private int id;
    private int sceneryId;
    private int scenerySpotId;
    private String no;
    private int accessTime;
    private String codeMachine;
    private String imageUrl;
    private String url;
    private String scenerSpotName;

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

    public int getScenerySpotId() {
        return scenerySpotId;
    }

    public void setScenerySpotId(int scenerySpotId) {
        this.scenerySpotId = scenerySpotId;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public int getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(int accessTime) {
        this.accessTime = accessTime;
    }

    public String getCodeMachine() {
        return codeMachine;
    }

    public void setCodeMachine(String codeMachine) {
        this.codeMachine = codeMachine;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getScenerSpotName() {
        return scenerSpotName;
    }

    public void setScenerSpotName(String scenerSpotName) {
        this.scenerSpotName = scenerSpotName;
    }

    @Override
    public String toString() {
        return "VisitHistoryBean{" +
                "id=" + id +
                ", sceneryId=" + sceneryId +
                ", scenerySpotId=" + scenerySpotId +
                ", no='" + no + '\'' +
                ", accessTime=" + accessTime +
                ", codeMachine='" + codeMachine + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", url='" + url + '\'' +
                ", scenerSpotName='" + scenerSpotName + '\'' +
                '}';
    }
}
