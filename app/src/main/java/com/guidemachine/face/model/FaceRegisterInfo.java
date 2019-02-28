package com.guidemachine.face.model;

import java.util.Arrays;

public class FaceRegisterInfo {
    private byte[] featureData;
    private String name;

    public FaceRegisterInfo() {
    }

    public FaceRegisterInfo(byte[] faceFeature, String name) {
        this.featureData = faceFeature;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getFeatureData() {
        return featureData;
    }

    public void setFeatureData(byte[] featureData) {
        this.featureData = featureData;
    }

    @Override
    public String toString() {
        return "FaceRegisterInfo{" +
                "featureData=" + Arrays.toString(featureData) +
                ", name='" + name + '\'' +
                '}';
    }
}
