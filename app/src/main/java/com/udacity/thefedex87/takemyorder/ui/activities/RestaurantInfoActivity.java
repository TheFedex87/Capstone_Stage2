package com.udacity.thefedex87.takemyorder.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.udacity.thefedex87.takemyorder.R;
import com.udacity.thefedex87.takemyorder.application.TakeMyOrderApplication;
import com.udacity.thefedex87.takemyorder.dagger.ApplicationModule;
import com.udacity.thefedex87.takemyorder.dagger.DaggerNetworkComponent;
import com.udacity.thefedex87.takemyorder.model.GooglePlaceDetailModel.GooglePlaceResultModel;
import com.udacity.thefedex87.takemyorder.retrofit.GooglePlacesApiInterface;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantInfoActivity extends AppCompatActivity {
    private String GOOGLE_PLACES_API_KEY = "";

    @Inject
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_info);



        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(RestaurantsMapActivity.PLACE_ID_KEY)){
            final String placeId = intent.getStringExtra(RestaurantsMapActivity.PLACE_ID_KEY);

            GOOGLE_PLACES_API_KEY = getString(R.string.GOOGLE_PLACES_API_KEY);

            if (GOOGLE_PLACES_API_KEY != "") {

                TakeMyOrderApplication.appComponent().inject(this);

                ApplicationModule applicationModule = new ApplicationModule(context);

                final GooglePlacesApiInterface googlePlacesApiInterface = DaggerNetworkComponent.builder().applicationModule(applicationModule).build().getGooglePlacesApiInterface();

                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... voids) {
                        googlePlacesApiInterface.placeDetails(placeId, GOOGLE_PLACES_API_KEY).enqueue(new Callback<GooglePlaceResultModel>() {
                            @Override
                            public void onResponse(Call<GooglePlaceResultModel> call, Response<GooglePlaceResultModel> response) {
                                Toast.makeText(RestaurantInfoActivity.this, response.body().getGooglePlaceDetailsModel().getName(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onFailure(Call<GooglePlaceResultModel> call, Throwable t) {
                                int g = 4;
                            }
                        });
                        return null;
                    }

                }.execute();
            }
        } else{
            finish();
        }
    }
}
