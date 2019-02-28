package com.guidemachine.service.entity;

import java.io.Serializable;
import java.util.List;

public class GoodsInfoDetailBean implements Serializable{

    /**
     * goodsId : 4bb167f9aadb495ebd23698d39a32555
     * imageUrlList : [{"imageUrl":"http://pcpw0wxxu.bkt.clouddn.com/hotel_01.png","imageType":0},{"imageUrl":"http://pcpw0wxxu.bkt.clouddn.com/hotel_01.png","imageType":1}]
     * minPrice : 50
     * maxPrice : 50
     * maxDiscountsPrice : 10
     * minMarketPrice : 80
     * maxMarketPrice : 80
     * goodsPriceId : 99a983bb3b7f4ed187ab6c5d09cef685
     * address : 四川省成都市长子县世纪大道8号
     * volume : 10
     * distanceFromTerminal : 1002.44
     * goodsName : 实体商品007
     * shoppingCartNum : 0
     */

    private String goodsId;
    private double minPrice;
    private double maxPrice;
    private double maxDiscountsPrice;
    private double minMarketPrice;
    private double maxMarketPrice;
    private String goodsPriceId;
    private String address;
    private int volume;
    private double distanceFromTerminal;
    private String goodsName;
    private int shoppingCartNum;
    private List<ImageUrlListBean> imageUrlList;

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public double getMaxDiscountsPrice() {
        return maxDiscountsPrice;
    }

    public void setMaxDiscountsPrice(double maxDiscountsPrice) {
        this.maxDiscountsPrice = maxDiscountsPrice;
    }

    public double getMinMarketPrice() {
        return minMarketPrice;
    }

    public void setMinMarketPrice(double minMarketPrice) {
        this.minMarketPrice = minMarketPrice;
    }

    public double getMaxMarketPrice() {
        return maxMarketPrice;
    }

    public void setMaxMarketPrice(double maxMarketPrice) {
        this.maxMarketPrice = maxMarketPrice;
    }

    public String getGoodsPriceId() {
        return goodsPriceId;
    }

    public void setGoodsPriceId(String goodsPriceId) {
        this.goodsPriceId = goodsPriceId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public int getShoppingCartNum() {
        return shoppingCartNum;
    }

    public void setShoppingCartNum(int shoppingCartNum) {
        this.shoppingCartNum = shoppingCartNum;
    }

    public List<ImageUrlListBean> getImageUrlList() {
        return imageUrlList;
    }

    public void setImageUrlList(List<ImageUrlListBean> imageUrlList) {
        this.imageUrlList = imageUrlList;
    }

    public static class ImageUrlListBean {
        /**
         * imageUrl : http://pcpw0wxxu.bkt.clouddn.com/hotel_01.png
         * imageType : 0
         */

        private String imageUrl;
        private int imageType;

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public int getImageType() {
            return imageType;
        }

        public void setImageType(int imageType) {
            this.imageType = imageType;
        }

        @Override
        public String toString() {
            return "ImageUrlListBean{" +
                    "imageUrl='" + imageUrl + '\'' +
                    ", imageType=" + imageType +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "GoodsInfoDetailBean{" +
                "goodsId='" + goodsId + '\'' +
                ", minPrice=" + minPrice +
                ", maxPrice=" + maxPrice +
                ", maxDiscountsPrice=" + maxDiscountsPrice +
                ", minMarketPrice=" + minMarketPrice +
                ", maxMarketPrice=" + maxMarketPrice +
                ", goodsPriceId='" + goodsPriceId + '\'' +
                ", address='" + address + '\'' +
                ", volume=" + volume +
                ", distanceFromTerminal=" + distanceFromTerminal +
                ", goodsName='" + goodsName + '\'' +
                ", shoppingCartNum=" + shoppingCartNum +
                ", imageUrlList=" + imageUrlList +
                '}';
    }
}
