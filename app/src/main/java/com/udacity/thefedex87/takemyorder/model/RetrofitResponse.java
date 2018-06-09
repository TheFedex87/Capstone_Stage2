package com.udacity.thefedex87.takemyorder.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Federico on 09/06/2018.
 */

public class RetrofitResponse {
    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    @SerializedName("restaurants")
    private List<Restaurant> restaurants;


}
