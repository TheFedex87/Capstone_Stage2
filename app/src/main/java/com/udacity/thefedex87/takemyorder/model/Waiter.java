package com.udacity.thefedex87.takemyorder.model;

/**
 * Created by feder on 07/06/2018.
 */

public class Waiter extends User {
    String restaurantId;

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }
}
