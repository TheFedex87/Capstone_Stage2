package com.udacity.thefedex87.takemyorder.room.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by federico.creti on 14/06/2018.
 */

@Entity(tableName = "current_order")
public class CurrentOrderEntry {
    @PrimaryKey(autoGenerate = true)
    private long id;

    private String name;
    private double price;
    private FoodTypes foodType;
    private String foodId;

    public CurrentOrderEntry(long id, String name, double price, FoodTypes foodType, String foodId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.foodType = foodType;
        this.foodId = foodId;
    }

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

    public FoodTypes getFoodType() {
        return foodType;
    }

    public void setFoodType(FoodTypes foodType) {
        this.foodType = foodType;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }
}
