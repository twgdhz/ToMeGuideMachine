package com.guidemachine.service.entity;

import java.io.Serializable;
import java.util.List;

public class GoodSpecBean implements Serializable{

    /**
     * goodsId : 7f2e6562dab34be9a8128b0a2ffc0598
     * imageUrl : http://pcpw0wxxu.bkt.clouddn.com/hotel_01.png
     * goodsPriceId : 5c277136941949c5807908bea1df75f7
     * minPrice : 30
     * maxPrice : 60
     * minMarketPrice : 50
     * maxMarketPrice : 80
     * stock : 50
     * flag : 1
     * parentSpecList : [{"name":"颜色","childSpecList":[{"name":"红色"},{"name":"黄色"}]},{"name":"尺码","childSpecList":[{"name":"M"},{"name":"L"}]}]
     * goodsSkuList : [{"goodsSkuId":"3f89ef09f79a4774b9355e901596a437","skuName":"颜色:红色,尺码:M","skuNum":"38","price":30,"marketPrice":50},{"goodsSkuId":"46d2921eaadd427495cbab8a08aa7c86","skuName":"颜色:黄色,尺码:L","skuNum":"36","price":60,"marketPrice":60},{"goodsSkuId":"99fe1cbe39154d749b8c0d8aa614c244","skuName":"颜色:黄色,尺码:M","skuNum":"38","price":50,"marketPrice":50},{"goodsSkuId":"e1ff1f94192f43d69d8f20d77420c584","skuName":"颜色:红色,尺码:L","skuNum":"38","price":40,"marketPrice":60}]
     */

    private String goodsId;
    private String imageUrl;
    private String goodsPriceId;
    private double minPrice;
    private double maxPrice;
    private double minMarketPrice;
    private double maxMarketPrice;
    private int stock;
    private int flag;
    private List<ParentSpecListBean> parentSpecList;
    private List<GoodsSkuListBean> goodsSkuList;

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

    public String getGoodsPriceId() {
        return goodsPriceId;
    }

    public void setGoodsPriceId(String goodsPriceId) {
        this.goodsPriceId = goodsPriceId;
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

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public List<ParentSpecListBean> getParentSpecList() {
        return parentSpecList;
    }

    public void setParentSpecList(List<ParentSpecListBean> parentSpecList) {
        this.parentSpecList = parentSpecList;
    }

    public List<GoodsSkuListBean> getGoodsSkuList() {
        return goodsSkuList;
    }

    public void setGoodsSkuList(List<GoodsSkuListBean> goodsSkuList) {
        this.goodsSkuList = goodsSkuList;
    }

    public static class ParentSpecListBean {
        /**
         * name : 颜色
         * childSpecList : [{"name":"红色"},{"name":"黄色"}]
         */

        private String name;
        private List<ChildSpecListBean> childSpecList;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<ChildSpecListBean> getChildSpecList() {
            return childSpecList;
        }

        public void setChildSpecList(List<ChildSpecListBean> childSpecList) {
            this.childSpecList = childSpecList;
        }

        public static class ChildSpecListBean {
            /**
             * name : 红色
             */

            private String name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            @Override
            public String toString() {
                return "ChildSpecListBean{" +
                        "name='" + name + '\'' +
                        '}';
            }
        }
    }

    public static class GoodsSkuListBean {
        /**
         * goodsSkuId : 3f89ef09f79a4774b9355e901596a437
         * skuName : 颜色:红色,尺码:M
         * skuNum : 38
         * price : 30
         * marketPrice : 50
         */

        private String goodsSkuId;
        private String skuName;
        private String skuNum;
        private int price;
        private int marketPrice;

        public String getGoodsSkuId() {
            return goodsSkuId;
        }

        public void setGoodsSkuId(String goodsSkuId) {
            this.goodsSkuId = goodsSkuId;
        }

        public String getSkuName() {
            return skuName;
        }

        public void setSkuName(String skuName) {
            this.skuName = skuName;
        }

        public String getSkuNum() {
            return skuNum;
        }

        public void setSkuNum(String skuNum) {
            this.skuNum = skuNum;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public int getMarketPrice() {
            return marketPrice;
        }

        public void setMarketPrice(int marketPrice) {
            this.marketPrice = marketPrice;
        }

        @Override
        public String toString() {
            return "GoodsSkuListBean{" +
                    "goodsSkuId='" + goodsSkuId + '\'' +
                    ", skuName='" + skuName + '\'' +
                    ", skuNum='" + skuNum + '\'' +
                    ", price=" + price +
                    ", marketPrice=" + marketPrice +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "GoodSpecBean{" +
                "goodsId='" + goodsId + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", goodsPriceId='" + goodsPriceId + '\'' +
                ", minPrice=" + minPrice +
                ", maxPrice=" + maxPrice +
                ", minMarketPrice=" + minMarketPrice +
                ", maxMarketPrice=" + maxMarketPrice +
                ", stock=" + stock +
                ", flag=" + flag +
                ", parentSpecList=" + parentSpecList +
                ", goodsSkuList=" + goodsSkuList +
                '}';
    }
}
