package com.udacity.thefedex87.takemyorder.room.entity;

/**
 * Created by feder on 21/06/2018.
 */

public class CurrentOrderGrouped {
    String mealId;
    int count;

    public String getMealId() {
        return mealId;
    }

    public void setMealId(String mealId) {
        this.mealId = mealId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
