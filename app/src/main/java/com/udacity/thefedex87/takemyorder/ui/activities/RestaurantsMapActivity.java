package com.udacity.thefedex87.takemyorder.ui.activities;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.udacity.thefedex87.takemyorder.R;
import com.udacity.thefedex87.takemyorder.models.Restaurant;

import java.util.List;

import timber.log.Timber;

public class RestaurantsMapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    public static final String PLACE_ID_KEY = "PLACE_ID_KEY";
    public static final String RESTAURANT_NAME_KEY = "RESTAURANT_NAME_KEY";

    private GoogleMap mMap;
    private List<Restaurant> restaurantList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(LoginMapsActivity.RESTAURANTS_INFO_KEY)) {
            restaurantList = intent.getParcelableArrayListExtra(LoginMapsActivity.RESTAURANTS_INFO_KEY);
        }


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (getString(R.string.GOOGLE_PLACES_API_KEY) != null && !getString(R.string.GOOGLE_PLACES_API_KEY).isEmpty())
            mMap.setOnMarkerClickListener(this);
        else {
            Timber.e(getString(R.string.error_places_api_key_not_provided));
            Toast.makeText(this, getString(R.string.error_places_api_key_not_provided), Toast.LENGTH_LONG).show();
        }

        //TODO: implement controls to retrieve current user position
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        mMap.setMyLocationEnabled(true);

        int i = 0;
        for (Restaurant restaurant : restaurantList) {
            LatLng restaurantPlace = new LatLng(restaurant.getLat(), restaurant.getLng());
            if (i == 0){
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(restaurantPlace, 10));
                i++;
            }


            mMap.addMarker(new MarkerOptions().position(restaurantPlace).title(restaurant.getName())).setTag(restaurant.getPlaceId());
        }



    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if(marker.getTag() == null || marker.getTag().toString() == "") return false;

        Intent intent = new Intent(this, RestaurantDetailsActivity.class);
        intent.putExtra(PLACE_ID_KEY, marker.getTag().toString());
        intent.putExtra(RESTAURANT_NAME_KEY, marker.getTitle());
        startActivity(intent);

        return false;
    }
}
