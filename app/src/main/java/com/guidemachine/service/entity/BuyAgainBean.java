package com.guidemachine.service.entity;

import java.io.Serializable;

public class BuyAgainBean implements Serializable {

    /**
     * goodsId : 4bb167f9aadb495ebd23698d39a32555
     * goodsName : 1122
     * cityName : 成都
     * goodsTypeName : null
     * level : AAA
     * imageUrl : http://pcpw0wxxu.bkt.clouddn.com/hotel_01.png
     * goodsGroupName : null
     * goodsGrade : 5
     * saleNums : 0
     * price : 0
     * distanceForUser : 1101.44
     * distanceForLandArea : 0
     * flag : 1
     */

    private String goodsId;
    private String goodsName;
    private String imageUrl;
    private int grade;
    private int isGroup;
    private String groupName;
    private String evaluateNum;//评价人数
    private int volume;
    private double distanceFromTerminal;
    private int minPrice;

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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getIsGroup() {
        return isGroup;
    }

    public void setIsGroup(int isGroup) {
        this.isGroup = isGroup;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getEvaluateNum() {
        return evaluateNum;
    }

    public void setEvaluateNum(String evaluateNum) {
        this.evaluateNum = evaluateNum;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public double getDistanceFromTerminal() {
        return distanceFromTerminal;
    }

    public void setDistanceFromTerminal(double distanceFromTerminal) {
        this.distanceFromTerminal = distanceFromTerminal;
    }

    public int getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(int minPrice) {
        this.minPrice = minPrice;
    }

    @Override
    public String toString() {
        return "BuyAgainBean{" +
                "goodsId='" + goodsId + '\'' +
                ", goodsName='" + goodsName + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", grade=" + grade +
                ", isGroup=" + isGroup +
                ", groupName='" + groupName + '\'' +
                ", evaluateNum='" + evaluateNum + '\'' +
                ", volume=" + volume +
                ", distanceFromTerminal=" + distanceFromTerminal +
                ", minPrice=" + minPrice +
                '}';
    }
}
