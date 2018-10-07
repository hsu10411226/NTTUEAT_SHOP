package com.example.pohan_pc.nttueat_shop;

/**
 * Created by POHAN-PC on 2017/12/23.
 */

public class food {
    private String FoodName,FoodValue,FoodImg,id;

    food(){}

    public food(String Name, String Value, String Img){
        FoodName = Name;
        FoodValue = Value;
        FoodImg = Img;
    }

    public String getFoodImg() {
        return FoodImg;
    }

    public void setFoodImg(String foodImg) {
        FoodImg = foodImg;
    }

    public String getFoodValue() {
        return FoodValue;
    }

    public void setFoodValue(String foodValue) {
        FoodValue = foodValue;
    }

    public String getFoodName() {
        return FoodName;
    }

    public void setFoodName(String foodName) {
        FoodName = foodName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
