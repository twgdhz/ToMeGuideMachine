package com.guidemachine.ui.superadmin.data;

/**
 * Created by xhu_ww on 2018/6/1.
 * description:沪深创指数
 */
public class CompositeIndexBean {
    private int rate;
    private String tradeDate;

    public double getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(String tradeDate) {
        this.tradeDate = tradeDate;
    }
}
