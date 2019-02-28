package com.guidemachine.service.entity;

import java.io.Serializable;

public class LocationBean implements Serializable{
    double longitude;
    double latitude;

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "LocationBean{" +
                "longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }
}
