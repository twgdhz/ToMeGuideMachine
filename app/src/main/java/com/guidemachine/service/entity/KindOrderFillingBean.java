package com.guidemachine.service.entity;

import java.io.Serializable;
import java.math.BigDecimal;

public class KindOrderFillingBean implements Serializable {

    /**
     * id : 83487b2bcf3f4992bdf0d63fa18fa0cf
     * name : 新天地
     * goodsId : 87160324f3ca406c9b3dd492b7bdd48e
     * imageUrl : http://pcpw0wxxu.bkt.clouddn.com/hotel_01.png
     * goodsName : 实体商品022
     * flag : 1
     * goodsSkuId : 71a59f2c48eb4b31957daad01bb948f4
     * goodsSkuName : 颜色:黄色,尺码:M
     * goodsPriceId : null
     * saleAmount : 50
     * discountsAmountFlag : 1
     * discountsAmount : 10
     * goodsNum : 1
     * isBill : 1
     * selfType : 0
     * address : null
     * telephone : null
     * isOrderDue : null
     * orderDue : null
     * isExpress : 0
     * expressAmount : 10
     * centerTransportFlag : 1
     * centerTransport : {"id":"3e18870efa664b8284d56e4c7ee18431","linkName":"陈大头","telephone":"15828472420","province":"山东省","city":"淄博市","area":"张店区","address":"天字第一号大街","trpDefault":1,"sign":"家"}
     */

    private String shopId;
    private String shopName;
    private String goodsId;
    private String imageUrl;
    private String goodsName;
    private int flag;
    private String skuId;
    private String skuName;

    private String goodsPriceId;
    private BigDecimal unitPrice;
    private int isDiscountsPrice;
    private BigDecimal discountsPrice;
    private BigDecimal goodsNum;
    private int isBill;
    private int gainType;
    private String address;
    private String telephone;
    private int isOrderDue;
    private int orderDue;

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getGoodsPriceId() {
        return goodsPriceId;
    }

    public void setGoodsPriceId(String goodsPriceId) {
        this.goodsPriceId = goodsPriceId;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getIsDiscountsPrice() {
        return isDiscountsPrice;
    }

    public void setIsDiscountsPrice(int isDiscountsPrice) {
        this.isDiscountsPrice = isDiscountsPrice;
    }

    public BigDecimal getDiscountsPrice() {
        return discountsPrice;
    }

    public void setDiscountsPrice(BigDecimal discountsPrice) {
        this.discountsPrice = discountsPrice;
    }

    public BigDecimal getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(BigDecimal goodsNum) {
        this.goodsNum = goodsNum;
    }

    public int getIsBill() {
        return isBill;
    }

    public void setIsBill(int isBill) {
        this.isBill = isBill;
    }

    public int getGainType() {
        return gainType;
    }

    public void setGainType(int gainType) {
        this.gainType = gainType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public int getIsOrderDue() {
        return isOrderDue;
    }

    public void setIsOrderDue(int isOrderDue) {
        this.isOrderDue = isOrderDue;
    }

    public int getOrderDue() {
        return orderDue;
    }

    public void setOrderDue(int orderDue) {
        this.orderDue = orderDue;
    }

    @Override
    public String toString() {
        return "KindOrderFillingBean{" +
                "shopId='" + shopId + '\'' +
                ", shopName='" + shopName + '\'' +
                ", goodsId='" + goodsId + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", goodsName='" + goodsName + '\'' +
                ", flag=" + flag +
                ", skuId='" + skuId + '\'' +
                ", skuName='" + skuName + '\'' +
                ", goodsPriceId='" + goodsPriceId + '\'' +
                ", unitPrice='" + unitPrice + '\'' +
                ", isDiscountsPrice=" + isDiscountsPrice +
                ", discountsPrice=" + discountsPrice +
                ", goodsNum=" + goodsNum +
                ", isBill=" + isBill +
                ", gainType=" + gainType +
                ", address='" + address + '\'' +
                ", telephone='" + telephone + '\'' +
                ", isOrderDue=" + isOrderDue +
                ", orderDue=" + orderDue +
                '}';
    }
}
