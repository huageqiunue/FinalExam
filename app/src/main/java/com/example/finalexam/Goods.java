package com.example.finalexam;

import android.content.Intent;

import java.util.Date;

public class Goods {
    private String Goods_business;
    private String Goods_name;
    private int Goods_price;
    private String Goods_describe;
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Goods(String goods_business, String goods_name, int goods_price, String goods_describe, String data) {
        Goods_business = goods_business;
        Goods_name = goods_name;
        Goods_price = goods_price;
        Goods_describe = goods_describe;
        this.data = data;
    }

    public Goods(String goods_business, String goods_name, int goods_price, String goods_describe) {
        Goods_business = goods_business;
        Goods_name = goods_name;
        Goods_price = goods_price;
        Goods_describe = goods_describe;
    }




    @Override
    public String toString() {
        return "Goods{" +
                ", Goods_business='" + Goods_business + '\'' +
                ", Goods_name='" + Goods_name + '\'' +
                ", Goods_price='" + Goods_price + '\'' +
                ", Goods_describe='" + Goods_describe + '\'' +
                '}';
    }

    public String getGoods_business() {
        return Goods_business;
    }

    public void setGoods_business(String goods_business) {
        Goods_business = goods_business;
    }

    public String getGoods_name() {
        return Goods_name;
    }

    public void setGoods_name(String goods_name) {
        Goods_name = goods_name;
    }

    public Integer getGoods_price() {
        return Goods_price;
    }

    public void setGoods_price(Integer goods_price) {
        Goods_price = goods_price;
    }

    public String getGoods_describe() {
        return Goods_describe;
    }

    public void setGoods_describe(String goods_describe) {
        Goods_describe = goods_describe;
    }

}
