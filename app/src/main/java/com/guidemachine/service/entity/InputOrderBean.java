package com.guidemachine.service.entity;

import java.io.Serializable;

public class InputOrderBean implements Serializable {

    /**
     * goodsId : ca2beb73674e4e4f9f857b975f9de3e6
     * goodsPriceId : 96c968aceebc4b178f4620974a593c24
     * priceDateId : null
     * goodsName : 套票017
     * realStockNums : 10
     * saleAmount : 90
     * discountsAmountFlag : 1
     * discountsAmount : 10
     * isCashpledge : 1
     * cashpledgeAmount : 100
     * price : 190
     * flag : 2
     */

    private String goodsId;
    private String goodsPriceId;
    private Object priceDateId;
    private String goodsName;
    private int realStockNums;
    private int saleAmount;
    private int discountsAmountFlag;
    private int discountsAmount;
    private int isCashpledge;
    private int cashpledgeAmount;
    private int price;
    private int flag;

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsPriceId() {
        return goodsPriceId;
    }

    public void setGoodsPriceId(String goodsPriceId) {
        this.goodsPriceId = goodsPriceId;
    }

    public Object getPriceDateId() {
        return priceDateId;
    }

    public void setPriceDateId(Object priceDateId) {
        this.priceDateId = priceDateId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public int getRealStockNums() {
        return realStockNums;
    }

    public void setRealStockNums(int realStockNums) {
        this.realStockNums = realStockNums;
    }

    public int getSaleAmount() {
        return saleAmount;
    }

    public void setSaleAmount(int saleAmount) {
        this.saleAmount = saleAmount;
    }

    public int getDiscountsAmountFlag() {
        return discountsAmountFlag;
    }

    public void setDiscountsAmountFlag(int discountsAmountFlag) {
        this.discountsAmountFlag = discountsAmountFlag;
    }

    public int getDiscountsAmount() {
        return discountsAmount;
    }

    public void setDiscountsAmount(int discountsAmount) {
        this.discountsAmount = discountsAmount;
    }

    public int getIsCashpledge() {
        return isCashpledge;
    }

    public void setIsCashpledge(int isCashpledge) {
        this.isCashpledge = isCashpledge;
    }

    public int getCashpledgeAmount() {
        return cashpledgeAmount;
    }

    public void setCashpledgeAmount(int cashpledgeAmount) {
        this.cashpledgeAmount = cashpledgeAmount;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "InputOrderBean{" +
                "goodsId='" + goodsId + '\'' +
                ", goodsPriceId='" + goodsPriceId + '\'' +
                ", priceDateId=" + priceDateId +
                ", goodsName='" + goodsName + '\'' +
                ", realStockNums=" + realStockNums +
                ", saleAmount=" + saleAmount +
                ", discountsAmountFlag=" + discountsAmountFlag +
                ", discountsAmount=" + discountsAmount +
                ", isCashpledge=" + isCashpledge +
                ", cashpledgeAmount=" + cashpledgeAmount +
                ", price=" + price +
                ", flag=" + flag +
                '}';
    }
}
