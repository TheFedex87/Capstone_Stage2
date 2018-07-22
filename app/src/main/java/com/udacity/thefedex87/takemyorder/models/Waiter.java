package com.udacity.thefedex87.takemyorder.models;

/**
 * Created by feder on 07/06/2018.
 */
//Specific class which rapresent a Waiter UserBase
public class Waiter extends UserBase {
    String restaurantId;

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }
}
