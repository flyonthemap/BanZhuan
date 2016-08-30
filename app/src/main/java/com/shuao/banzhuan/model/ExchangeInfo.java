package com.shuao.banzhuan.model;

/**
 * Created by flyonthemap on 16/8/13.
 */
public class ExchangeInfo {
    private String exchangeName;
    private String require;
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
