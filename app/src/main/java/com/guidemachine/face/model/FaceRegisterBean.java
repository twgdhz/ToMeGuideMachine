package com.guidemachine.face.model;

public class FaceRegisterBean {
    private String faceBytes;
    private String name;

    public FaceRegisterBean(String faceBytes, String name) {
        this.faceBytes = faceBytes;
        this.name = name;
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

    @Override
    public String toString() {
        return "FaceRegisterBean{" +
                "faceBytes='" + faceBytes + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
