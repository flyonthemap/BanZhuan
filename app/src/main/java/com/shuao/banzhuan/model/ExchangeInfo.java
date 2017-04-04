package com.shuao.banzhuan.model;

/**
 * Created by flyonthemap on 16/8/13.
 */
public class ExchangeInfo {
    // 兑换方式的名字
    private String exchangeName;
    // 兑换按钮的名字
    private String require;
    // 兑换图片的id
    private int imageId;

    public ExchangeInfo(String exchangeName, String require, int imageId) {
        this.exchangeName = exchangeName;
        this.require = require;
        this.imageId = imageId;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public String getRequire() {
        return require;
    }

    public int getImageId() {
        return imageId;
    }
}
