package com.udacity.thefedex87.takemyorder.models;

import com.udacity.thefedex87.takemyorder.room.entity.Meal;

import java.util.Date;
import java.util.List;

/**
 * Created by feder on 07/06/2018.
 */

//This class is a model used to push an order into the Firebase Realtime Database
public class Order {
    private String restaurantId;
    private String userId;
    private String tableId;
    private List<Meal> meals;
    private Date orderTime;

    public Order(String restaurantId, String userId, String tableId, List<Meal> meals, Date orderTime) {
        this.restaurantId = restaurantId;
        this.userId = userId;
        this.tableId = tableId;
        this.meals = meals;
        this.orderTime = orderTime;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public List<Meal> getMeals() {
        return meals;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }
}
