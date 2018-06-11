package com.udacity.thefedex87.takemyorder.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.Toast;

import com.udacity.thefedex87.takemyorder.R;
import com.udacity.thefedex87.takemyorder.application.TakeMyOrderApplication;
import com.udacity.thefedex87.takemyorder.dagger.ApplicationModule;
import com.udacity.thefedex87.takemyorder.dagger.DaggerNetworkComponent;
import com.udacity.thefedex87.takemyorder.model.GooglePlaceDetailModel.GooglePlaceResultModel;
import com.udacity.thefedex87.takemyorder.model.GooglePlaceDetailModel.RestaurantPhotoModel;
import com.udacity.thefedex87.takemyorder.retrofit.GooglePlacesApiInterface;
import com.udacity.thefedex87.takemyorder.ui.adapters.RestaurantPhotoAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class RestaurantDetailsActivity extends AppCompatActivity {
    private String GOOGLE_PLACES_API_KEY = "";

    @BindView(R.id.photo_pager)
    ViewPager photoPager;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.toolbar_container)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @Inject
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);

        ButterKnife.bind(this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




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
                                collapsingToolbarLayout.setTitle(response.body().getGooglePlaceDetailsModel().getName());

                                List<String> photos = new ArrayList<>();
                                for(RestaurantPhotoModel photoReference : response.body().getGooglePlaceDetailsModel().getPhotos()){
                                    String photoUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=1000&photoreference=" + photoReference.getPhotoReference() + "&key=" + GOOGLE_PLACES_API_KEY;
                                    photos.add(photoUrl);
                                }

                                RestaurantPhotoAdapter adapter = new RestaurantPhotoAdapter(photos, context);
                                photoPager.setAdapter(adapter);
                            }

                            @Override
                            public void onFailure(Call<GooglePlaceResultModel> call, Throwable t) {
                                Timber.e("Error retrieving restaurant detials: " + t.getMessage());
                                Toast.makeText(RestaurantDetailsActivity.this, getString(R.string.error_retrieving_restaurant_details), Toast.LENGTH_LONG);
                                finish();
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
