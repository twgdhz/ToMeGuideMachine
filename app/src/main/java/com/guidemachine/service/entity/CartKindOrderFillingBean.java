package com.guidemachine.service.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class CartKindOrderFillingBean implements Serializable {

    /**
     * id : 83487b2bcf3f4992bdf0d63fa18fa0cf
     * name : 新天地
     * kindOrderItems : [{"goodsItemId":"e72ec7ad618a4f6e96e658773dd3849b","id":"83487b2bcf3f4992bdf0d63fa18fa0cf","goodsId":"61a24faba4d54556b6c7005b746a6be9","goodsName":"实体商品011","imageUrl":"http://pcpw0wxxu.bkt.clouddn.com/hotel_01.png","flag":1,"goodsPriceId":null,"goodsSkuId":"55686a297aea42c39d41495f705203fa","goodsSkuName":"颜色:红色,尺码:M","saleAmount":30,"goodsNum":2,"discountsAmountFlag":1,"discountsAmount":10,"selfType":0,"address":null,"telephone":null,"isOrderDue":null,"orderDue":null,"isExpress":0,"expressAmount":10,"centerTransportFlag":1,"centerTransport":{"id":"3e18870efa664b8284d56e4c7ee18431","linkName":"陈大头","telephone":"15828472420","province":"山东省","city":"淄博市","area":"张店区","address":"天字第一号大街","trpDefault":1,"sign":"家"}},{"goodsItemId":"4078d52bc8ca44549297833e4dc86915","id":"83487b2bcf3f4992bdf0d63fa18fa0cf","goodsId":"aeead27c083946cf9324e8ef3bc2ae65","goodsName":"实体商品031","imageUrl":"http://pcpw0wxxu.bkt.clouddn.com/hotel_01.png","flag":1,"goodsPriceId":null,"goodsSkuId":"0acae78ee6754c079f1a84ee9b046200","goodsSkuName":"颜色:黄色,尺码:L","saleAmount":60,"goodsNum":1,"discountsAmountFlag":1,"discountsAmount":10,"selfType":1,"address":"四川省成都市屯留县伯阳西路","telephone":"17310515059","isOrderDue":1,"orderDue":7,"isExpress":null,"expressAmount":0,"centerTransportFlag":null,"centerTransport":null},{"goodsItemId":"e55287c8b20c4c0598b3ccbb7b7107ea","id":"83487b2bcf3f4992bdf0d63fa18fa0cf","goodsId":"87160324f3ca406c9b3dd492b7bdd48e","goodsName":"实体商品022","imageUrl":"http://pcpw0wxxu.bkt.clouddn.com/hotel_01.png","flag":1,"goodsPriceId":null,"goodsSkuId":"3a2513fb36a34e5d9660e43e55637181","goodsSkuName":"颜色:红色,尺码:M","saleAmount":30,"goodsNum":2,"discountsAmountFlag":1,"discountsAmount":10,"selfType":0,"address":null,"telephone":null,"isOrderDue":null,"orderDue":null,"isExpress":0,"expressAmount":10,"centerTransportFlag":1,"centerTransport":{"id":"3e18870efa664b8284d56e4c7ee18431","linkName":"陈大头","telephone":"15828472420","province":"山东省","city":"淄博市","area":"张店区","address":"天字第一号大街","trpDefault":1,"sign":"家"}}]
     */

    private String id;
    private String name;
    private String isBill;
    private List<KindOrderItemsBean> cartItems;

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

    public String getIsBill() {
        return isBill;
    }

    public void setIsBill(String isBill) {
        this.isBill = isBill;
    }

    public List<KindOrderItemsBean> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<KindOrderItemsBean> cartItems) {
        this.cartItems = cartItems;
    }

    public List<KindOrderItemsBean> getKindOrderItems() {
        return cartItems;
    }

    public void setKindOrderItems(List<KindOrderItemsBean> kindOrderItems) {
        this.cartItems = kindOrderItems;
    }

    public static class KindOrderItemsBean {
        /**
         * goodsItemId : e72ec7ad618a4f6e96e658773dd3849b
         * id : 83487b2bcf3f4992bdf0d63fa18fa0cf
         * goodsId : 61a24faba4d54556b6c7005b746a6be9
         * goodsName : 实体商品011
         * imageUrl : http://pcpw0wxxu.bkt.clouddn.com/hotel_01.png
         * flag : 1
         * goodsPriceId : null
         * goodsSkuId : 55686a297aea42c39d41495f705203fa
         * goodsSkuName : 颜色:红色,尺码:M
         * saleAmount : 30
         * goodsNum : 2
         * discountsAmountFlag : 1
         * discountsAmount : 10
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

        private String goodsItemId;
        private String id;
        private String goodsId;
        private String goodsName;
        private String imageUrl;
        private int flag;
        private Object goodsPriceId;
        private String goodsSkuId;
        private String goodsSkuName;
        private BigDecimal unitPrice;
        private BigDecimal goodsNum;
        private int isDiscountsPrice;
        private BigDecimal discountsPrice;
        private int selfType;
        private String address;
        private String telephone;
        private int isOrderDue;
        private int orderDue;
        private int isExpress;
        private int expressAmount;
        private int centerTransportFlag;
        private Integer userSelfType;
        private CenterTransportBean centerTransport;

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


        public int getSelfType() {
            return selfType;
        }

        public void setSelfType(int selfType) {
            this.selfType = selfType;
        }

        public Object getAddress() {
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

        public int getIsExpress() {
            return isExpress;
        }

        public void setIsExpress(int isExpress) {
            this.isExpress = isExpress;
        }

        public int getExpressAmount() {
            return expressAmount;
        }

        public void setExpressAmount(int expressAmount) {
            this.expressAmount = expressAmount;
        }

        public int getCenterTransportFlag() {
            return centerTransportFlag;
        }

        public void setCenterTransportFlag(int centerTransportFlag) {
            this.centerTransportFlag = centerTransportFlag;
        }

        public CenterTransportBean getCenterTransport() {
            return centerTransport;
        }

        public void setCenterTransport(CenterTransportBean centerTransport) {
            this.centerTransport = centerTransport;
        }

        public Integer getUserSelfType() {
            return userSelfType;
        }

        public void setUserSelfType(Integer userSelfType) {
            this.userSelfType = userSelfType;
        }

        public static class CenterTransportBean {
            /**
             * id : 3e18870efa664b8284d56e4c7ee18431
             * linkName : 陈大头
             * telephone : 15828472420
             * province : 山东省
             * city : 淄博市
             * area : 张店区
             * address : 天字第一号大街
             * trpDefault : 1
             * sign : 家
             */

            private String id;
            private String linkName;
            private String telephone;
            private String province;
            private String city;
            private String area;
            private String address;
            private int trpDefault;
            private String sign;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getLinkName() {
                return linkName;
            }

            public void setLinkName(String linkName) {
                this.linkName = linkName;
            }

            public String getTelephone() {
                return telephone;
            }

            public void setTelephone(String telephone) {
                this.telephone = telephone;
            }

            public String getProvince() {
                return province;
            }

            public void setProvince(String province) {
                this.province = province;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getArea() {
                return area;
            }

            public void setArea(String area) {
                this.area = area;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public int getTrpDefault() {
                return trpDefault;
            }

            public void setTrpDefault(int trpDefault) {
                this.trpDefault = trpDefault;
            }

            public String getSign() {
                return sign;
            }

            public void setSign(String sign) {
                this.sign = sign;
            }

            @Override
            public String toString() {
                return "CenterTransportBean{" +
                        "id='" + id + '\'' +
                        ", linkName='" + linkName + '\'' +
                        ", telephone='" + telephone + '\'' +
                        ", province='" + province + '\'' +
                        ", city='" + city + '\'' +
                        ", area='" + area + '\'' +
                        ", address='" + address + '\'' +
                        ", trpDefault=" + trpDefault +
                        ", sign='" + sign + '\'' +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "KindOrderItemsBean{" +
                    "goodsItemId='" + goodsItemId + '\'' +
                    ", id='" + id + '\'' +
                    ", goodsId='" + goodsId + '\'' +
                    ", goodsName='" + goodsName + '\'' +
                    ", imageUrl='" + imageUrl + '\'' +
                    ", flag=" + flag +
                    ", goodsPriceId=" + goodsPriceId +
                    ", goodsSkuId='" + goodsSkuId + '\'' +
                    ", goodsSkuName='" + goodsSkuName + '\'' +
                    ", unitPrice=" + unitPrice +
                    ", goodsNum=" + goodsNum +
                    ", isDiscountsPrice=" + isDiscountsPrice +
                    ", discountsPrice=" + discountsPrice +
                    ", selfType=" + selfType +
                    ", address='" + address + '\'' +
                    ", telephone='" + telephone + '\'' +
                    ", isOrderDue=" + isOrderDue +
                    ", orderDue=" + orderDue +
                    ", isExpress=" + isExpress +
                    ", expressAmount=" + expressAmount +
                    ", centerTransportFlag=" + centerTransportFlag +
                    ", userSelfType=" + userSelfType +
                    ", centerTransport=" + centerTransport +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "CartKindOrderFillingBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", isBill='" + isBill + '\'' +
                ", cartItems=" + cartItems +
                '}';
    }
}
