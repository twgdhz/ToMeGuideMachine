package com.guidemachine.service.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @Author gjz
 * @Date 2018/9/20 10:32
 **/
public class AppCreateKindOrderCartDto implements Serializable {

    //用户id
    private String userId;

    //商铺信息列表
    private List<AppCreateKindOrderCartShopDto> shops;

    //商品总售价(需支付价格)
    private String price;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<AppCreateKindOrderCartShopDto> getShops() {
        return shops;
    }

    public void setShops(List<AppCreateKindOrderCartShopDto> shops) {
        this.shops = shops;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "AppCreateKindOrderCartDto{" +
                "userId='" + userId + '\'' +
                ", shops=" + shops +
                ", price='" + price + '\'' +
                '}';
    }
}