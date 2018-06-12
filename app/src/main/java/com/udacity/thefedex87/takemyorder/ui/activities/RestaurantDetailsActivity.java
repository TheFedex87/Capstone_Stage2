package com.udacity.thefedex87.takemyorder.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.LayoutDirection;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.udacity.thefedex87.takemyorder.R;
import com.udacity.thefedex87.takemyorder.application.TakeMyOrderApplication;
import com.udacity.thefedex87.takemyorder.dagger.ApplicationModule;
import com.udacity.thefedex87.takemyorder.dagger.DaggerNetworkComponent;
import com.udacity.thefedex87.takemyorder.dagger.DaggerUserInterfaceComponent;
import com.udacity.thefedex87.takemyorder.dagger.UserInterfaceComponent;
import com.udacity.thefedex87.takemyorder.dagger.UserInterfaceModule;
import com.udacity.thefedex87.takemyorder.model.GooglePlaceDetailModel.GooglePlaceResultModel;
import com.udacity.thefedex87.takemyorder.model.GooglePlaceDetailModel.RestaurantPhotoModel;
import com.udacity.thefedex87.takemyorder.retrofit.GooglePlacesApiInterface;
import com.udacity.thefedex87.takemyorder.ui.adapters.PhotoIndicatorContainerAdapter;
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

    @BindView(R.id.photo_indicator_container)
    RecyclerView photoIndicatorContainer;

    @BindView(R.id.restaurant_address)
    TextView restaurantAddress;

    @BindView(R.id.average_1)
    ImageView average1;

    @BindView(R.id.average_2)
    ImageView average2;

    @BindView(R.id.average_3)
    ImageView average3;

    @BindView(R.id.average_4)
    ImageView average4;

    @BindView(R.id.average_5)
    ImageView average5;

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

                final ApplicationModule applicationModule = new ApplicationModule(context);

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

                                UserInterfaceComponent userInterfaceComponent = DaggerUserInterfaceComponent.builder()
                                        .applicationModule(applicationModule)
                                        .userInterfaceModule(new UserInterfaceModule(photos))
                                        .build();

                                RestaurantPhotoAdapter adapter = userInterfaceComponent.getRestaurantPhotoAdapter();
                                photoPager.setAdapter(adapter);

                                final PhotoIndicatorContainerAdapter indicatorAdapter = userInterfaceComponent.getPhotoIndicatorContainerAdapter();
                                photoIndicatorContainer.setAdapter(indicatorAdapter);
                                photoIndicatorContainer.setLayoutManager(userInterfaceComponent.getLinearLayoutManager());

                                photoPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                    @Override
                                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                                        indicatorAdapter.setActivePhoto(position);
                                    }

                                    @Override
                                    public void onPageSelected(int position) {

                                    }

                                    @Override
                                    public void onPageScrollStateChanged(int state) {

                                    }
                                });

                                restaurantAddress.setText(response.body().getGooglePlaceDetailsModel().getFormattedAddress());

                                int i = 0;
                                double rating = response.body().getGooglePlaceDetailsModel().getRating();
                                int intRating = (int)rating;
                                double decimalRating = rating - intRating;
                                switch ((int)rating){
                                    case 1:
                                        average1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.average_full));
                                        if(decimalRating >= 0.25 && decimalRating <= 0.75){
                                            average2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.average_half));
                                        } else if(decimalRating > 0.75) {
                                            average2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.average_full));
                                        }
                                        break;
                                    case 2:
                                        average1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.average_full));
                                        average2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.average_full));
                                        if(decimalRating >= 0.25 && decimalRating <= 0.75){
                                            average3.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.average_half));
                                        } else if(decimalRating > 0.75) {
                                            average3.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.average_full));
                                        }
                                        break;
                                    case 3:
                                        average1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.average_full));
                                        average2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.average_full));
                                        average3.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.average_full));
                                        if(decimalRating >= 0.25 && decimalRating <= 0.75){
                                            average4.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.average_half));
                                        } else if(decimalRating > 0.75) {
                                            average4.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.average_full));
                                        }
                                        break;
                                    case 4:
                                        average1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.average_full));
                                        average2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.average_full));
                                        average3.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.average_full));
                                        average4.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.average_full));
                                        if(decimalRating >= 0.25 && decimalRating <= 0.75){
                                            average5.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.average_half));
                                        } else if(decimalRating > 0.75) {
                                            average5.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.average_full));
                                        }
                                        break;
                                    case 5:
                                        average1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.average_full));
                                        average2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.average_full));
                                        average3.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.average_full));
                                        average4.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.average_full));
                                        average5.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.average_full));
                                        break;
                                }
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
