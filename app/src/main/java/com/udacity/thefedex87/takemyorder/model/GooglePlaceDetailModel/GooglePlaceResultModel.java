package com.udacity.thefedex87.takemyorder.model.GooglePlaceDetailModel;

import com.google.gson.annotations.SerializedName;

/**
 * Created by federico.creti on 11/06/2018.
 */

public class GooglePlaceResultModel {
    @SerializedName("result")
    private GooglePlaceDetailsModel googlePlaceDetailsModel;

    public GooglePlaceDetailsModel getGooglePlaceDetailsModel() {
        return googlePlaceDetailsModel;
    }

    public void setGooglePlaceDetailsModel(GooglePlaceDetailsModel googlePlaceDetailsModel) {
        this.googlePlaceDetailsModel = googlePlaceDetailsModel;
    }
}
