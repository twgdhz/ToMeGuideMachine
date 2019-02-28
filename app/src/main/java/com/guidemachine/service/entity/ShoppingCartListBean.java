package com.guidemachine.service.entity;

import java.io.Serializable;
import java.util.List;

public class ShoppingCartListBean implements Serializable {

    /**
     * current : 1
     * pages : 1
     * size : 15
     * total : 1
     * records : [{"id":"83487b2bcf3f4992bdf0d63fa18fa0cf","name":"新天地","kindGoodsItems":[{"goodsItemId":"58d68e2bb9be46bdb01e0185f13768b3","id":"83487b2bcf3f4992bdf0d63fa18fa0cf","goodsId":"aeead27c083946cf9324e8ef3bc2ae65","goodsName":"实体商品031","imageUrl":"http://pcpw0wxxu.bkt.clouddn.com/hotel_01.png","flag":1,"goodsPriceId":null,"goodsSkuId":"0acae78ee6754c079f1a84ee9b046200","goodsSkuName":"颜色:黄色,尺码:L","saleAmount":60,"price":240,"goodsNum":4,"realStockNums":38,"selfType":1},{"goodsItemId":"1750504dbd6c47e1b1c56a41f2630f71","id":"83487b2bcf3f4992bdf0d63fa18fa0cf","goodsId":"61a24faba4d54556b6c7005b746a6be9","goodsName":"实体商品011","imageUrl":"http://pcpw0wxxu.bkt.clouddn.com/hotel_01.png","flag":1,"goodsPriceId":null,"goodsSkuId":"774cd2fe243a4b2b846d701510b188af","goodsSkuName":"颜色:黄色,尺码:M","saleAmount":50,"price":200,"goodsNum":4,"realStockNums":38,"selfType":0}]}]
     */

    private int current;
    private int pages;
    private int size;
    private int total;
    private List<RecordsBean> records;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<RecordsBean> getRecords() {
        return records;
    }

    public void setRecords(List<RecordsBean> records) {
        this.records = records;
    }

    public static class RecordsBean {
        /**
         * id : 83487b2bcf3f4992bdf0d63fa18fa0cf
         * name : 新天地
         * kindGoodsItems : [{"goodsItemId":"58d68e2bb9be46bdb01e0185f13768b3","id":"83487b2bcf3f4992bdf0d63fa18fa0cf","goodsId":"aeead27c083946cf9324e8ef3bc2ae65","goodsName":"实体商品031","imageUrl":"http://pcpw0wxxu.bkt.clouddn.com/hotel_01.png","flag":1,"goodsPriceId":null,"goodsSkuId":"0acae78ee6754c079f1a84ee9b046200","goodsSkuName":"颜色:黄色,尺码:L","saleAmount":60,"price":240,"goodsNum":4,"realStockNums":38,"selfType":1},{"goodsItemId":"1750504dbd6c47e1b1c56a41f2630f71","id":"83487b2bcf3f4992bdf0d63fa18fa0cf","goodsId":"61a24faba4d54556b6c7005b746a6be9","goodsName":"实体商品011","imageUrl":"http://pcpw0wxxu.bkt.clouddn.com/hotel_01.png","flag":1,"goodsPriceId":null,"goodsSkuId":"774cd2fe243a4b2b846d701510b188af","goodsSkuName":"颜色:黄色,尺码:M","saleAmount":50,"price":200,"goodsNum":4,"realStockNums":38,"selfType":0}]
         */

        private String id;
        private String name;
        private boolean isSelect_shop;      //店铺是否在购物车中被选中
        private List<KindGoodsItemsBean> kindGoodsItems;

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

        public List<KindGoodsItemsBean> getKindGoodsItems() {
            return kindGoodsItems;
        }

        public void setKindGoodsItems(List<KindGoodsItemsBean> kindGoodsItems) {
            this.kindGoodsItems = kindGoodsItems;
        }


        public boolean getIsSelect_shop() {
            return isSelect_shop;
        }

        public void setIsSelect_shop(boolean select_shop) {
            isSelect_shop = select_shop;
        }

        public static class KindGoodsItemsBean {
            /**
             * goodsItemId : 58d68e2bb9be46bdb01e0185f13768b3
             * id : 83487b2bcf3f4992bdf0d63fa18fa0cf
             * goodsId : aeead27c083946cf9324e8ef3bc2ae65
             * goodsName : 实体商品031
             * imageUrl : http://pcpw0wxxu.bkt.clouddn.com/hotel_01.png
             * flag : 1
             * goodsPriceId : null
             * goodsSkuId : 0acae78ee6754c079f1a84ee9b046200
             * goodsSkuName : 颜色:黄色,尺码:L
             * saleAmount : 60
             * price : 240
             * goodsNum : 4
             * realStockNums : 38
             * selfType : 1
             */

            private String goodsItemId;
            private String id;
            private String goodsId;
            private String goodsName;
            private String imageUrl;
            private int flag;
            private Object goodsPriceId;
            private String goodsSkuId;
            private String goodsSkuName;
            private int saleAmount;
            private double price;
            private int goodsNum;
            private int realStockNums;
            private int selfType;
            private boolean isSelect;        //商品是否在购物车中被选中
            public String getGoodsItemId() {
                return goodsItemId;
            }

            public void setGoodsItemId(String goodsItemId) {
                this.goodsItemId = goodsItemId;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

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

            public int getFlag() {
                return flag;
            }

            public void setFlag(int flag) {
                this.flag = flag;
            }

            public Object getGoodsPriceId() {
                return goodsPriceId;
            }

            public void setGoodsPriceId(Object goodsPriceId) {
                this.goodsPriceId = goodsPriceId;
            }

            public String getGoodsSkuId() {
                return goodsSkuId;
            }

            public void setGoodsSkuId(String goodsSkuId) {
                this.goodsSkuId = goodsSkuId;
            }

            public String getGoodsSkuName() {
                return goodsSkuName;
            }

            public void setGoodsSkuName(String goodsSkuName) {
                this.goodsSkuName = goodsSkuName;
            }

            public int getSaleAmount() {
                return saleAmount;
            }

            public void setSaleAmount(int saleAmount) {
                this.saleAmount = saleAmount;
            }

            public double getPrice() {
                return price;
            }

            public void setPrice(double price) {
                this.price = price;
            }

            public int getGoodsNum() {
                return goodsNum;
            }

            public void setGoodsNum(int goodsNum) {
                this.goodsNum = goodsNum;
            }

            public int getRealStockNums() {
                return realStockNums;
            }

            public void setRealStockNums(int realStockNums) {
                this.realStockNums = realStockNums;
            }

            public int getSelfType() {
                return selfType;
            }

            public void setSelfType(int selfType) {
                this.selfType = selfType;
            }

            public boolean getIsSelect() {
                return isSelect;
            }

            public void setIsSelect(boolean isSelect) {
                this.isSelect = isSelect;
            }


            @Override
            public String toString() {
                return "KindGoodsItemsBean{" +
                        "goodsItemId='" + goodsItemId + '\'' +
                        ", id='" + id + '\'' +
                        ", goodsId='" + goodsId + '\'' +
                        ", goodsName='" + goodsName + '\'' +
                        ", imageUrl='" + imageUrl + '\'' +
                        ", flag=" + flag +
                        ", goodsPriceId=" + goodsPriceId +
                        ", goodsSkuId='" + goodsSkuId + '\'' +
                        ", goodsSkuName='" + goodsSkuName + '\'' +
                        ", saleAmount=" + saleAmount +
                        ", price=" + price +
                        ", goodsNum=" + goodsNum +
                        ", realStockNums=" + realStockNums +
                        ", selfType=" + selfType +
                        ", isSelect=" + isSelect +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "RecordsBean{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", isSelect_shop=" + isSelect_shop +
                    ", kindGoodsItems=" + kindGoodsItems +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ShoppingCartListBean{" +
                "current=" + current +
                ", pages=" + pages +
                ", size=" + size +
                ", total=" + total +
                ", records=" + records +
                '}';
    }
}
