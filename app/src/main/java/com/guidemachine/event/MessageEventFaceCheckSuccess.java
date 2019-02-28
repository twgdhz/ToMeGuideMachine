package com.guidemachine.event;


/**
 * Description:eventBus的事件
 */

public class MessageEventFaceCheckSuccess {

    public double longitude;

    public double latitude;
    public String citName;
    public String province;

    public MessageEventFaceCheckSuccess() {
    }

    public MessageEventFaceCheckSuccess(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public MessageEventFaceCheckSuccess(double longitude, double latitude, String citName, String province) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.citName = citName;
        this.province = province;
    }
}