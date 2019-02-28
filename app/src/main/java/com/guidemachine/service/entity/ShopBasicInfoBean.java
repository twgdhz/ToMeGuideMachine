package com.guidemachine.service.entity;

import java.io.Serializable;
import java.util.List;

public class ShopBasicInfoBean implements Serializable {

    /**
     * shopId : 304c4f632607407d857de3d5e5b2a8c9
     * name : 麦田中心
     * imageUrlList : ["http://pcpw0wxxu.bkt.clouddn.com/hotel_01.png"]
     * address : 成纪大道东路
     * distanceFromTerminal : 472.44
     * distanceFromScenery : 1102.24
     * lon : 105.897967
     * lat : 34.569025
     */

    private String shopId;
    private String name;
    private String address;
    private double distanceFromTerminal;
    private double distanceFromScenery;
    private double lon;
    private double lat;
    private List<String> imageUrlList;

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getDistanceFromTerminal() {
        return distanceFromTerminal;
    }

    public void setDistanceFromTerminal(double distanceFromTerminal) {
        this.distanceFromTerminal = distanceFromTerminal;
    }

    public double getDistanceFromScenery() {
        return distanceFromScenery;
    }

    public void setDistanceFromScenery(double distanceFromScenery) {
        this.distanceFromScenery = distanceFromScenery;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public List<String> getImageUrlList() {
        return imageUrlList;
    }

    public void setImageUrlList(List<String> imageUrlList) {
        this.imageUrlList = imageUrlList;
    }

    @Override
    public String toString() {
        return "ShopBasicInfoBean{" +
                "shopId='" + shopId + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", distanceFromTerminal=" + distanceFromTerminal +
                ", distanceFromScenery=" + distanceFromScenery +
                ", lon=" + lon +
                ", lat=" + lat +
                ", imageUrlList=" + imageUrlList +
                '}';
    }
}
