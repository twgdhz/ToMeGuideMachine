package com.guidemachine.service.entity;

import java.io.Serializable;

public class CreateOrderRefundReserveBean implements Serializable{

    /**
     * goodsName : 实体商品019
     * orderId : 24a33635e7dc43c08f5a040f59396ced
     * realPrice : 40
     * payType : 1
     */

    private String goodsName;
    private String orderId;
    private int realPrice;
    private int payType;

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getRealPrice() {
        return realPrice;
    }

    public void setRealPrice(int realPrice) {
        this.realPrice = realPrice;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    @Override
    public String toString() {
        return "CreateOrderRefundReserveBean{" +
                "goodsName='" + goodsName + '\'' +
                ", orderId='" + orderId + '\'' +
                ", realPrice=" + realPrice +
                ", payType=" + payType +
                '}';
    }
}
