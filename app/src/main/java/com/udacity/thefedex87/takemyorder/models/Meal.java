package com.udacity.thefedex87.takemyorder.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.udacity.thefedex87.takemyorder.room.entity.FoodTypes;

/**
 * Created by feder on 16/06/2018.
 */

@Entity(tableName = "current_order")
public class Meal {
    @PrimaryKey(autoGenerate = true)
    private long id;

    private String name;
    private double price;
    private String imageName;
    private String mealId;
    private FoodTypes foodType;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getMealId() {
        return mealId;
    }

    public void setMealId(String mealId) {
        this.mealId = mealId;
    }

    public FoodTypes getFoodType() {
        return foodType;
    }

    public void setFoodType(FoodTypes foodType) {
        this.foodType = foodType;
    }
}
