package com.guidemachine.service.entity;

import java.io.Serializable;

/**
 * @Author gjz
 * @Date 2018/9/20 10:11
 **/
public class AppCreateKindOrderCartItemDto implements Serializable {

    //商品明细id
    private String goodsItemId;

    //配送方式
    private Integer selfType;

    //收货人地址id
    private String addressId;

    public String getGoodsItemId() {
        return goodsItemId;
    }

    public void setGoodsItemId(String goodsItemId) {
        this.goodsItemId = goodsItemId;
    }


    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    @Override
    public String toString() {
        return "AppCreateKindOrderCartItemDto{" +
                "goodsItemId='" + goodsItemId + '\'' +
                ", addressId='" + addressId + '\'' +
                '}';
    }
}