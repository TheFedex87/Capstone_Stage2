package com.udacity.thefedex87.takemyorder.room.entity;

/**
 * Created by feder on 21/06/2018.
 */

public class CurrentOrderGrouped {
    String foodId;
    int count;

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
