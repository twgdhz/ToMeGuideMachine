package com.guidemachine.event;


import com.guidemachine.service.entity.GoodSpecBean;

/**
 * Description:eventBus的事件
 */

public class MessageEventStock {

    public double longitude;

    public double latitude;
    GoodSpecBean.GoodsSkuListBean goodsSkuListBean;

    public MessageEventStock(GoodSpecBean.GoodsSkuListBean goodsSkuListBean) {
        this.goodsSkuListBean = goodsSkuListBean;
    }

    public MessageEventStock(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

}