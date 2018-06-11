package com.udacity.thefedex87.takemyorder.model.GooglePlaceDetailModel;

import com.google.gson.annotations.SerializedName;

/**
 * Created by federico.creti on 11/06/2018.
 */

public class GooglePlaceDetailsModel {
    @SerializedName("name")
    String name;
    @SerializedName("place_id")
    String placeId;
    @SerializedName("rating")
    double rating;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
