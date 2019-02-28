package com.guidemachine.service.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class SelectOrderBean implements Serializable{

    /**
     * pageNum : 1
     * pageSize : 15
     * size : 6
     * startRow : 1
     * endRow : 6
     * total : 6
     * pages : 1
     * list : [{"shopId":"bbbda3e9c20647ffbfbdf57a130ffbbc","shopName":"新大丰","orderId":"24a33635e7dc43c08f5a040f59396ced","orderNo":"KO1544082599227","items":[{"goodsId":"7f2e6562dab34be9a8128b0a2ffc0598","goodsName":"实体商品019","goodsImageUrl":"http://pcpw0wxxu.bkt.clouddn.com/hotel_01.png","flag":0,"goodsSkuId":null,"goodsSkuName":null,"goodsNum":1,"unitPrice":50}],"createTime":"2018-12-06 15:49:59","price":40,"isDiscountsPrice":1,"discountsPrice":10,"orderStatus":0,"orderSearchStatus":1,"isRefund":0,"orderType":1,"goodsRefundId":null,"refundStatus":null},{"shopId":"bbbda3e9c20647ffbfbdf57a130ffbbc","shopName":"新大丰","orderId":"735d5353043e4a73aa1e0a45ecce8b26","orderNo":"KO1544087523369","items":[{"goodsId":"7f2e6562dab34be9a8128b0a2ffc0598","goodsName":"实体商品019","goodsImageUrl":"http://pcpw0wxxu.bkt.clouddn.com/hotel_01.png","flag":1,"goodsSkuId":"46d2921eaadd427495cbab8a08aa7c86","goodsSkuName":"颜色:黄色,尺码:L","goodsNum":1,"unitPrice":60}],"createTime":"2018-12-06 17:12:03","price":50,"isDiscountsPrice":1,"discountsPrice":10,"orderStatus":0,"orderSearchStatus":1,"isRefund":0,"orderType":1,"goodsRefundId":null,"refundStatus":null},{"shopId":"bbbda3e9c20647ffbfbdf57a130ffbbc","shopName":"新大丰","orderId":"a94c9de6176842c9bc043efb370b6bb7","orderNo":"KO1544087872441","items":[{"goodsId":"7f2e6562dab34be9a8128b0a2ffc0598","goodsName":"实体商品019","goodsImageUrl":"http://pcpw0wxxu.bkt.clouddn.com/hotel_01.png","flag":1,"goodsSkuId":"46d2921eaadd427495cbab8a08aa7c86","goodsSkuName":"颜色:黄色,尺码:L","goodsNum":1,"unitPrice":60}],"createTime":"2018-12-06 17:17:52","price":50,"isDiscountsPrice":1,"discountsPrice":10,"orderStatus":0,"orderSearchStatus":1,"isRefund":0,"orderType":1,"goodsRefundId":null,"refundStatus":null},{"shopId":"bbbda3e9c20647ffbfbdf57a130ffbbc","shopName":"新大丰","orderId":"c928c25bb24146ceb6b3f9cd32503c90","orderNo":"KO1544087285829","items":[{"goodsId":"7f2e6562dab34be9a8128b0a2ffc0598","goodsName":"实体商品019","goodsImageUrl":"http://pcpw0wxxu.bkt.clouddn.com/hotel_01.png","flag":1,"goodsSkuId":"46d2921eaadd427495cbab8a08aa7c86","goodsSkuName":"颜色:黄色,尺码:L","goodsNum":1,"unitPrice":60}],"createTime":"2018-12-06 17:08:06","price":50,"isDiscountsPrice":1,"discountsPrice":10,"orderStatus":0,"orderSearchStatus":1,"isRefund":0,"orderType":1,"goodsRefundId":null,"refundStatus":null},{"shopId":"bbbda3e9c20647ffbfbdf57a130ffbbc","shopName":"新大丰","orderId":"e4b25bd3f0364a3887a59fae2c2b7a1d","orderNo":"KO1544082520824","items":[{"goodsId":"2851dacbf1044efb9d88108d8c73f9a3","goodsName":"实体商品002","goodsImageUrl":"http://pcpw0wxxu.bkt.clouddn.com/hotel_01.png","flag":0,"goodsSkuId":null,"goodsSkuName":null,"goodsNum":1,"unitPrice":50}],"createTime":"2018-12-06 15:48:41","price":40,"isDiscountsPrice":1,"discountsPrice":10,"orderStatus":0,"orderSearchStatus":1,"isRefund":0,"orderType":1,"goodsRefundId":null,"refundStatus":null},{"shopId":"bbbda3e9c20647ffbfbdf57a130ffbbc","shopName":"新大丰","orderId":"f364663f272f4fbaa70d282a3cb8bdf2","orderNo":"KO1544087952467","items":[{"goodsId":"7f2e6562dab34be9a8128b0a2ffc0598","goodsName":"实体商品019","goodsImageUrl":"http://pcpw0wxxu.bkt.clouddn.com/hotel_01.png","flag":1,"goodsSkuId":"46d2921eaadd427495cbab8a08aa7c86","goodsSkuName":"颜色:黄色,尺码:L","goodsNum":1,"unitPrice":60}],"createTime":"2018-12-06 17:19:12","price":50,"isDiscountsPrice":1,"discountsPrice":10,"orderStatus":0,"orderSearchStatus":1,"isRefund":0,"orderType":1,"goodsRefundId":null,"refundStatus":null}]
     * prePage : 0
     * nextPage : 0
     * isFirstPage : true
     * isLastPage : true
     * hasPreviousPage : false
     * hasNextPage : false
     * navigatePages : 8
     * navigatepageNums : [1]
     * navigateFirstPage : 1
     * navigateLastPage : 1
     * firstPage : 1
     * lastPage : 1
     */

    private int pageNum;
    private int pageSize;
    private int size;
    private int startRow;
    private int endRow;
    private int total;
    private int pages;
    private int prePage;
    private int nextPage;
    private boolean isFirstPage;
    private boolean isLastPage;
    private boolean hasPreviousPage;
    private boolean hasNextPage;
    private int navigatePages;
    private int navigateFirstPage;
    private int navigateLastPage;
    private int firstPage;
    private int lastPage;
    private List<ListBean> list;
    private List<Integer> navigatepageNums;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public int getEndRow() {
        return endRow;
    }

    public void setEndRow(int endRow) {
        this.endRow = endRow;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getPrePage() {
        return prePage;
    }

    public void setPrePage(int prePage) {
        this.prePage = prePage;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public boolean isIsFirstPage() {
        return isFirstPage;
    }

    public void setIsFirstPage(boolean isFirstPage) {
        this.isFirstPage = isFirstPage;
    }

    public boolean isIsLastPage() {
        return isLastPage;
    }

    public void setIsLastPage(boolean isLastPage) {
        this.isLastPage = isLastPage;
    }

    public boolean isHasPreviousPage() {
        return hasPreviousPage;
    }

    public void setHasPreviousPage(boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public int getNavigatePages() {
        return navigatePages;
    }

    public void setNavigatePages(int navigatePages) {
        this.navigatePages = navigatePages;
    }

    public int getNavigateFirstPage() {
        return navigateFirstPage;
    }

    public void setNavigateFirstPage(int navigateFirstPage) {
        this.navigateFirstPage = navigateFirstPage;
    }

    public int getNavigateLastPage() {
        return navigateLastPage;
    }

    public void setNavigateLastPage(int navigateLastPage) {
        this.navigateLastPage = navigateLastPage;
    }

    public int getFirstPage() {
        return firstPage;
    }

    public void setFirstPage(int firstPage) {
        this.firstPage = firstPage;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public List<Integer> getNavigatepageNums() {
        return navigatepageNums;
    }

    public void setNavigatepageNums(List<Integer> navigatepageNums) {
        this.navigatepageNums = navigatepageNums;
    }

    public static class ListBean {
        /**
         * shopId : bbbda3e9c20647ffbfbdf57a130ffbbc
         * shopName : 新大丰
         * orderId : 24a33635e7dc43c08f5a040f59396ced
         * orderNo : KO1544082599227
         * items : [{"goodsId":"7f2e6562dab34be9a8128b0a2ffc0598","goodsName":"实体商品019","goodsImageUrl":"http://pcpw0wxxu.bkt.clouddn.com/hotel_01.png","flag":0,"goodsSkuId":null,"goodsSkuName":null,"goodsNum":1,"unitPrice":50}]
         * createTime : 2018-12-06 15:49:59
         * price : 40
         * isDiscountsPrice : 1
         * discountsPrice : 10
         * orderStatus : 0
         * orderSearchStatus : 1
         * isRefund : 0
         * orderType : 1
         * goodsRefundId : null
         * refundStatus : null
         */

        private String shopId;
        private String shopName;
        private String orderId;
        private String orderNo;
        private String createTime;
        private BigDecimal price;
        private int isDiscountsPrice;
        private BigDecimal discountsPrice;
        private int orderStatus;
        private int orderSearchStatus;
        private int isRefund;
        private int orderType;
        private String goodsRefundId;
        private int refundStatus;
        private List<ItemsBean> items;

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

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
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

        public int getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(int orderStatus) {
            this.orderStatus = orderStatus;
        }

        public int getOrderSearchStatus() {
            return orderSearchStatus;
        }

        public void setOrderSearchStatus(int orderSearchStatus) {
            this.orderSearchStatus = orderSearchStatus;
        }

        public int getIsRefund() {
            return isRefund;
        }

        public void setIsRefund(int isRefund) {
            this.isRefund = isRefund;
        }

        public int getOrderType() {
            return orderType;
        }

        public void setOrderType(int orderType) {
            this.orderType = orderType;
        }

        public String getGoodsRefundId() {
            return goodsRefundId;
        }

        public void setGoodsRefundId(String goodsRefundId) {
            this.goodsRefundId = goodsRefundId;
        }

        public int getRefundStatus() {
            return refundStatus;
        }

        public void setRefundStatus(int refundStatus) {
            this.refundStatus = refundStatus;
        }

        public List<ItemsBean> getItems() {
            return items;
        }

        public void setItems(List<ItemsBean> items) {
            this.items = items;
        }

        public static class ItemsBean {
            /**
             * goodsId : 7f2e6562dab34be9a8128b0a2ffc0598
             * goodsName : 实体商品019
             * goodsImageUrl : http://pcpw0wxxu.bkt.clouddn.com/hotel_01.png
             * flag : 0
             * goodsSkuId : null
             * goodsSkuName : null
             * goodsNum : 1
             * unitPrice : 50
             */

            private String goodsId;
            private String goodsName;
            private String goodsImageUrl;
            private int flag;
            private Object goodsSkuId;
            private Object goodsSkuName;
            private int goodsNum;
            private int unitPrice;

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

            public String getGoodsImageUrl() {
                return goodsImageUrl;
            }

            public void setGoodsImageUrl(String goodsImageUrl) {
                this.goodsImageUrl = goodsImageUrl;
            }

            public int getFlag() {
                return flag;
            }

            public void setFlag(int flag) {
                this.flag = flag;
            }

            public Object getGoodsSkuId() {
                return goodsSkuId;
            }

            public void setGoodsSkuId(Object goodsSkuId) {
                this.goodsSkuId = goodsSkuId;
            }

            public Object getGoodsSkuName() {
                return goodsSkuName;
            }

            public void setGoodsSkuName(Object goodsSkuName) {
                this.goodsSkuName = goodsSkuName;
            }

            public int getGoodsNum() {
                return goodsNum;
            }

            public void setGoodsNum(int goodsNum) {
                this.goodsNum = goodsNum;
            }

            public int getUnitPrice() {
                return unitPrice;
            }

            public void setUnitPrice(int unitPrice) {
                this.unitPrice = unitPrice;
            }

            @Override
            public String toString() {
                return "ItemsBean{" +
                        "goodsId='" + goodsId + '\'' +
                        ", goodsName='" + goodsName + '\'' +
                        ", goodsImageUrl='" + goodsImageUrl + '\'' +
                        ", flag=" + flag +
                        ", goodsSkuId=" + goodsSkuId +
                        ", goodsSkuName=" + goodsSkuName +
                        ", goodsNum=" + goodsNum +
                        ", unitPrice=" + unitPrice +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "ListBean{" +
                    "shopId='" + shopId + '\'' +
                    ", shopName='" + shopName + '\'' +
                    ", orderId='" + orderId + '\'' +
                    ", orderNo='" + orderNo + '\'' +
                    ", createTime='" + createTime + '\'' +
                    ", price=" + price +
                    ", isDiscountsPrice=" + isDiscountsPrice +
                    ", discountsPrice=" + discountsPrice +
                    ", orderStatus=" + orderStatus +
                    ", orderSearchStatus=" + orderSearchStatus +
                    ", isRefund=" + isRefund +
                    ", orderType=" + orderType +
                    ", goodsRefundId=" + goodsRefundId +
                    ", refundStatus=" + refundStatus +
                    ", items=" + items +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "SelectOrderBean{" +
                "pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", size=" + size +
                ", startRow=" + startRow +
                ", endRow=" + endRow +
                ", total=" + total +
                ", pages=" + pages +
                ", prePage=" + prePage +
                ", nextPage=" + nextPage +
                ", isFirstPage=" + isFirstPage +
                ", isLastPage=" + isLastPage +
                ", hasPreviousPage=" + hasPreviousPage +
                ", hasNextPage=" + hasNextPage +
                ", navigatePages=" + navigatePages +
                ", navigateFirstPage=" + navigateFirstPage +
                ", navigateLastPage=" + navigateLastPage +
                ", firstPage=" + firstPage +
                ", lastPage=" + lastPage +
                ", list=" + list +
                ", navigatepageNums=" + navigatepageNums +
                '}';
    }
}
