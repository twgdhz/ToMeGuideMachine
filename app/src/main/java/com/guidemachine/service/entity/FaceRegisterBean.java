package com.guidemachine.service.entity;

public class FaceRegisterBean {
    private String faceBytes;
    private String name;
    private String id;
    private String faceUrl;

    public FaceRegisterBean(String faceBytes, String name, String id,String faceUrl) {
        this.faceBytes = faceBytes;
        this.name = name;
        this.id = id;
        this.faceUrl = faceUrl;
    }

    public String getFaceBytes() {
        return faceBytes;
    }

    public void setFaceBytes(String faceBytes) {
        this.faceBytes = faceBytes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFaceUrl() {
        return faceUrl;
    }

    public void setFaceUrl(String faceUrl) {
        this.faceUrl = faceUrl;
    }

    @Override
    public String toString() {
        return "FaceRegisterBean{" +
                "faceBytes='" + faceBytes + '\'' +
                ", name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", faceUrl='" + faceUrl + '\'' +
                '}';
    }
}
