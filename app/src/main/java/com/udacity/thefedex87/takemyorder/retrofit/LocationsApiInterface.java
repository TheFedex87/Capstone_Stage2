package com.udacity.thefedex87.takemyorder.retrofit;

import com.udacity.thefedex87.takemyorder.model.Restaurant;
import com.udacity.thefedex87.takemyorder.model.RetrofitResponse;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Federico on 09/06/2018.
 */

public interface LocationsApiInterface {
    @GET("restaurants.json")
    Call<RetrofitResponse> restaurants();
}
