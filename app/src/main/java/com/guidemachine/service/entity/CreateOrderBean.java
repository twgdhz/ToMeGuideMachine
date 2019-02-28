package com.guidemachine.service.entity;

import java.io.Serializable;
import java.math.BigDecimal;

public class CreateOrderBean implements Serializable {

    /**
     * goodsId : ca2beb73674e4e4f9f857b975f9de3e6
     * goodsName : 套票017
     * goodsOrderId : 35da0c1a994d47f99a69e9c474c9fd89
     * closeTime : 1536896422000
     * saleAmount : 360
     * discountsAmount : 20
     */

    private String goodsId;
    private String goodsName;
    private String goodsOrderId;
    private String kindOrderGoodsId;
    private String orderNo;
    private long closeTime;
    private BigDecimal saleAmount;
    private BigDecimal discountsPrice;
    private int isPay;
    private String orderId;
    private String price;

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsOrderId() {
        return goodsOrderId;
    }

    public void setGoodsOrderId(String goodsOrderId) {
        this.goodsOrderId = goodsOrderId;
    }

    public long getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(long closeTime) {
        this.closeTime = closeTime;
    }

    public BigDecimal getSaleAmount() {
        return saleAmount;
    }

    public void setSaleAmount(BigDecimal saleAmount) {
        this.saleAmount = saleAmount;
    }

    public BigDecimal getDiscountsPrice() {
        return discountsPrice;
    }

    public void setDiscountsPrice(BigDecimal discountsPrice) {
        this.discountsPrice = discountsPrice;
    }

    public int getIsPay() {
        return isPay;
    }

    public void setIsPay(int isPay) {
        this.isPay = isPay;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getKindOrderGoodsId() {
        return kindOrderGoodsId;
    }

    public void setKindOrderGoodsId(String kindOrderGoodsId) {
        this.kindOrderGoodsId = kindOrderGoodsId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    @Override
    public String toString() {
        return "CreateOrderBean{" +
                "goodsId='" + goodsId + '\'' +
                ", goodsName='" + goodsName + '\'' +
                ", goodsOrderId='" + goodsOrderId + '\'' +
                ", kindOrderGoodsId='" + kindOrderGoodsId + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", closeTime=" + closeTime +
                ", saleAmount=" + saleAmount +
                ", discountsPrice=" + discountsPrice +
                ", isPay=" + isPay +
                ", orderId='" + orderId + '\'' +
                ", price='" + price + '\'' +
                '}';
    }
}
