package com.udacity.thefedex87.takemyorder.room.entity;

/**
 * Created by feder on 21/06/2018.
 */
//This is the model for the Current order table, grouped by mealId
public class CurrentOrderGrouped {
    String name;
    String mealId;
    double price;
    int count;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMealId() {
        return mealId;
    }

    public void setMealId(String mealId) {
        this.mealId = mealId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
