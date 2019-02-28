package com.guidemachine.service.entity;

import java.io.Serializable;

public class TripBean implements Serializable{

    /**
     * service : command
     * concrete : trip
     * content : 天气转冷,多穿衣服
     */

    private String service;
    private String concrete;
    private String content;

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getConcrete() {
        return concrete;
    }

    public void setConcrete(String concrete) {
        this.concrete = concrete;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "TripBean{" +
                "service='" + service + '\'' +
                ", concrete='" + concrete + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
