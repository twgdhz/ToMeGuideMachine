package com.guidemachine.service.entity;

import java.io.Serializable;

public class PersonCenterBean  implements Serializable {
    private int refundStatus;
    private String refundTime;

    public int getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(int refundStatus) {
        this.refundStatus = refundStatus;
    }

    public String getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(String refundTime) {
        this.refundTime = refundTime;
    }

    @Override
    public String toString() {
        return "PersonCenterBean{" +
                "refundStatus=" + refundStatus +
                ", refundTime='" + refundTime + '\'' +
                '}';
    }
}
