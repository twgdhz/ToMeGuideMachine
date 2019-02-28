package com.guidemachine.service.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @Author gjz
 * @Date 2018/9/20 10:08
 **/
public class AppCreateKindOrderCartShopDto implements Serializable {

    //商铺id
  
    private String shopId;

    //商品条件列表
    private List<AppCreateKindOrderCartItemDto> items;

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public List<AppCreateKindOrderCartItemDto> getItems() {
        return items;
    }

    public void setItems(List<AppCreateKindOrderCartItemDto> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "AppCreateKindOrderCartShopDto{" +
                "shopId='" + shopId + '\'' +
                ", items=" + items +
                '}';
    }
}