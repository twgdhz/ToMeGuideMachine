package com.guidemachine.service.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class OrderDetailBean implements Serializable{

    /**
     * orderId : 24a33635e7dc43c08f5a040f59396ced
     * orderNo : KO1544082599227
     * orderStatus : 2
     * totalGoodsNum : 1
     * realPrice : 40
     * payType : 1
     * gainType : 1
     * telephone : 15828472427
     * shopAddress : 新大丰  四川省成都市平顺县彩凤大道
     * shopTelephone : 18956234789
     * isBill : null
     * isRefund : 1
     * createTime : 2018-12-06 15:49:59
     * payTime : 2018-11-26 23:31:10
     * checkTime : 2018-11-26 23:31:10
     * finishTime : 2018-11-26 23:31:10
     * closeTimeCurr : 0
     * goodsDetails : [{"goodsId":"7f2e6562dab34be9a8128b0a2ffc0598","goodsImageUrl":"http://pcpw0wxxu.bkt.clouddn.com/hotel_01.png","goodsName":"实体商品019","unitPrice":50,"flag":0,"goodsSkuName":null,"goodsNum":1,"price":40,"isDiscountsPrice":1,"discountsPrice":10}]
     * goodsRefundId : null
     * refundStatus : null
     * refundTime :
     */

    private String orderId;
    private String orderNo;
    private int orderStatus;
    private int totalGoodsNum;
    private BigDecimal realPrice;
    private int payType;
    private int gainType;
    private String telephone;
    private String shopAddress;
    private String shopTelephone;
    private Object isBill;
    private int isRefund;
    private String createTime;
    private String payTime;
    private String checkTime;
    private String finishTime;
    private String closeTimeCurr;
    private Object goodsRefundId;
    private int refundStatus;
    private String refundTime;
    private List<GoodsDetailsBean> goodsDetails;

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

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getTotalGoodsNum() {
        return totalGoodsNum;
    }

    public void setTotalGoodsNum(int totalGoodsNum) {
        this.totalGoodsNum = totalGoodsNum;
    }

    public BigDecimal getRealPrice() {
        return realPrice;
    }

    public void setRealPrice(BigDecimal realPrice) {
        this.realPrice = realPrice;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public int getGainType() {
        return gainType;
    }

    public void setGainType(int gainType) {
        this.gainType = gainType;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public String getShopTelephone() {
        return shopTelephone;
    }

    public void setShopTelephone(String shopTelephone) {
        this.shopTelephone = shopTelephone;
    }

    public Object getIsBill() {
        return isBill;
    }

    public void setIsBill(Object isBill) {
        this.isBill = isBill;
    }

    public int getIsRefund() {
        return isRefund;
    }

    public void setIsRefund(int isRefund) {
        this.isRefund = isRefund;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public String getCloseTimeCurr() {
        return closeTimeCurr;
    }

    public void setCloseTimeCurr(String closeTimeCurr) {
        this.closeTimeCurr = closeTimeCurr;
    }

    public Object getGoodsRefundId() {
        return goodsRefundId;
    }

    public void setGoodsRefundId(Object goodsRefundId) {
        this.goodsRefundId = goodsRefundId;
    }

    public int getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(int refundStatus) {
        this.refundStatus = refundStatus;
    }

    public String getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(String refundTime) {
        this.refundTime = refundTime;
    }

    public List<GoodsDetailsBean> getGoodsDetails() {
        return goodsDetails;
    }

    public void setGoodsDetails(List<GoodsDetailsBean> goodsDetails) {
        this.goodsDetails = goodsDetails;
    }

    public static class GoodsDetailsBean {
        /**
         * goodsId : 7f2e6562dab34be9a8128b0a2ffc0598
         * goodsImageUrl : http://pcpw0wxxu.bkt.clouddn.com/hotel_01.png
         * goodsName : 实体商品019
         * unitPrice : 50
         * flag : 0
         * goodsSkuName : null
         * goodsNum : 1
         * price : 40
         * isDiscountsPrice : 1
         * discountsPrice : 10
         */

        private String goodsId;
        private String goodsImageUrl;
        private String goodsName;
        private BigDecimal unitPrice;
        private int flag;
        private Object goodsSkuName;
        private BigDecimal goodsNum;
        private BigDecimal price;
        private int isDiscountsPrice;
        private BigDecimal discountsPrice;

        public String getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(String goodsId) {
            this.goodsId = goodsId;
        }

        public String getGoodsImageUrl() {
            return goodsImageUrl;
        }

        public void setGoodsImageUrl(String goodsImageUrl) {
            this.goodsImageUrl = goodsImageUrl;
        }

        public String getGoodsName() {
            return goodsName;
        }

        public void setGoodsName(String goodsName) {
            this.goodsName = goodsName;
        }

        public BigDecimal getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(BigDecimal unitPrice) {
            this.unitPrice = unitPrice;
        }

        public int getFlag() {
            return flag;
        }

        public void setFlag(int flag) {
            this.flag = flag;
        }

        public Object getGoodsSkuName() {
            return goodsSkuName;
        }

        public void setGoodsSkuName(Object goodsSkuName) {
            this.goodsSkuName = goodsSkuName;
        }

        public BigDecimal getGoodsNum() {
            return goodsNum;
        }

        public void setGoodsNum(BigDecimal goodsNum) {
            this.goodsNum = goodsNum;
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

        @Override
        public String toString() {
            return "GoodsDetailsBean{" +
                    "goodsId='" + goodsId + '\'' +
                    ", goodsImageUrl='" + goodsImageUrl + '\'' +
                    ", goodsName='" + goodsName + '\'' +
                    ", unitPrice=" + unitPrice +
                    ", flag=" + flag +
                    ", goodsSkuName=" + goodsSkuName +
                    ", goodsNum=" + goodsNum +
                    ", price=" + price +
                    ", isDiscountsPrice=" + isDiscountsPrice +
                    ", discountsPrice=" + discountsPrice +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "OrderDetailBean{" +
                "orderId='" + orderId + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", orderStatus=" + orderStatus +
                ", totalGoodsNum=" + totalGoodsNum +
                ", realPrice=" + realPrice +
                ", payType=" + payType +
                ", gainType=" + gainType +
                ", telephone='" + telephone + '\'' +
                ", shopAddress='" + shopAddress + '\'' +
                ", shopTelephone='" + shopTelephone + '\'' +
                ", isBill=" + isBill +
                ", isRefund=" + isRefund +
                ", createTime='" + createTime + '\'' +
                ", payTime='" + payTime + '\'' +
                ", checkTime='" + checkTime + '\'' +
                ", finishTime='" + finishTime + '\'' +
                ", closeTimeCurr=" + closeTimeCurr +
                ", goodsRefundId=" + goodsRefundId +
                ", refundStatus=" + refundStatus +
                ", refundTime='" + refundTime + '\'' +
                ", goodsDetails=" + goodsDetails +
                '}';
    }
}
