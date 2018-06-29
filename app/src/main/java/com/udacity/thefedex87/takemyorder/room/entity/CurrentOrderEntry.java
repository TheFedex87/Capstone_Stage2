//package com.udacity.thefedex87.takemyorder.room.entity;
//
//import android.arch.persistence.room.Entity;
//import android.arch.persistence.room.PrimaryKey;
//
///**
// * Created by federico.creti on 14/06/2018.
// */
//
//@Entity(tableName = "current_order")
//public class CurrentOrderEntry {
//    @PrimaryKey(autoGenerate = true)
//    private long id;
//
//    private String name;
//    private double price;
//    private FoodTypes foodType;
//    private String mealId;
//
//    public CurrentOrderEntry(long id, String name, double price, FoodTypes foodType, String mealId) {
//        this.id = id;
//        this.name = name;
//        this.price = price;
//        this.foodType = foodType;
//        this.mealId = mealId;
//    }
//
//    public long getId() {
//        return id;
//    }
//
//    public void setId(long id) {
//        this.id = id;
//    }
//
//    public String getIngredientName() {
//        return name;
//    }
//
//    public void setIngredientName(String name) {
//        this.name = name;
//    }
//
//    public double getPrice() {
//        return price;
//    }
//
//    public void setPrice(double price) {
//        this.price = price;
//    }
//
//    public FoodTypes getFoodType() {
//        return foodType;
//    }
//
//    public void setFoodType(FoodTypes foodType) {
//        this.foodType = foodType;
//    }
//
//    public String getMealId() {
//        return mealId;
//    }
//
//    public void setMealId(String mealId) {
//        this.mealId = mealId;
//    }
//}
