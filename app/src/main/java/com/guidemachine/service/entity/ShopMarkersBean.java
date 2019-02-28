package com.guidemachine.service.entity;

import java.io.Serializable;
import java.util.List;

public class ShopMarkersBean implements Serializable{

    private List<ShopInfoBean> shopInfo;

    public List<ShopInfoBean> getShopInfo() {
        return shopInfo;
    }

    public void setShopInfo(List<ShopInfoBean> shopInfo) {
        this.shopInfo = shopInfo;
    }

    public static class ShopInfoBean implements Serializable{
        /**
         * id : 0da6f3fb859240c087928bd15cbd488e
         * name : 青城山门店
         * lon : 103.624112
         * lat : 30.938505
         */

        private String id;
        private String name;
        private double lon;
        private double lat;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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

        @Override
        public String toString() {
            return "ShopInfoBean{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", lon=" + lon +
                    ", lat=" + lat +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ShopMarkersBean{" +
                "shopInfo=" + shopInfo +
                '}';
    }
}
