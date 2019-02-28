package com.guidemachine.service.entity;

import java.io.Serializable;

public class GetCodeBean implements Serializable {
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "GetCodeBean{" +
                "key='" + key + '\'' +
                '}';
    }
}
