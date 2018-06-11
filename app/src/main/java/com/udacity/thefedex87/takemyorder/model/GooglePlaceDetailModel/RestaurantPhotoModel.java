package com.udacity.thefedex87.takemyorder.model.GooglePlaceDetailModel;

import com.google.gson.annotations.SerializedName;

/**
 * Created by federico.creti on 11/06/2018.
 */

public class RestaurantPhotoModel {
    @SerializedName("photo_reference")
    String photoReference;

    public String getPhotoReference() {
        return photoReference;
    }

    public void setPhotoReference(String photoReference) {
        this.photoReference = photoReference;
    }
}
