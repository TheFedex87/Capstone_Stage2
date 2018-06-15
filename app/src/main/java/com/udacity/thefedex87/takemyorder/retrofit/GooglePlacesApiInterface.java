package com.udacity.thefedex87.takemyorder.retrofit;

import com.udacity.thefedex87.takemyorder.models.GooglePlaceDetailModel.GooglePlaceResultModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by federico.creti on 11/06/2018.
 */

public interface GooglePlacesApiInterface {
    @GET("maps/api/place/details/json")
    Call<GooglePlaceResultModel> placeDetails(@Query("placeid") String placeId, @Query("key")String key);
}
