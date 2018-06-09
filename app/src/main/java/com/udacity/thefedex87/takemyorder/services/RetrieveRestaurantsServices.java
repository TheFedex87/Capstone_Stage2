package com.udacity.thefedex87.takemyorder.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.udacity.thefedex87.takemyorder.model.Restaurant;
import com.udacity.thefedex87.takemyorder.retrofit.LocationsApiInterface;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Federico on 09/06/2018.
 */


public class RetrieveRestaurantsServices extends IntentService {
    @Inject
    LocationsApiInterface locationsApiInterface;

    public RetrieveRestaurantsServices(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
//        locationsApiInterface.restaurants().enqueue(new Callback<List<Restaurant>>() {
//            @Override
//            public void onResponse(Call<List<Restaurant>> call, Response<List<Restaurant>> response) {
//
//            }
//
//            @Override
//            public void onFailure(Call<List<Restaurant>> call, Throwable t) {
//
//            }
//        });
    }
}
