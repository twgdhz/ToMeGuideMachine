package com.guidemachine.service.entity;

import java.io.Serializable;

public class GetSelfCodeBean implements Serializable{
    private String qrCodeUrl;

    public String getQrCodeUrl() {
        return qrCodeUrl;
    }

    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }

    @Override
    public String toString() {
        return "GetSelfCodeBean{" +
                "qrCodeUrl='" + qrCodeUrl + '\'' +
                '}';
    }
}
