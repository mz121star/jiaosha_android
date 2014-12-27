package com.inuoer.model;

/**
 * Created by Administrator on 2014/12/26.
 */
public class Shop {
    private String shop_name;
    private String shop_image;
    public void setShopName(String shopname){
        shop_name=shopname;
    }
    public String getShopName(){
        return  shop_name;
    }

    public void setShopImage(String shopimage){
        shop_image=shopimage;
    }
    public String getShopImage(){
        return  shop_image;
    }
}
