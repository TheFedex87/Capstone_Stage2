package com.udacity.thefedex87.takemyorder.ui.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdate;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class RestaurantsMapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, LocationListener {
    public static final String PLACE_ID_KEY = "PLACE_ID_KEY";
    public static final String RESTAURANT_NAME_KEY = "RESTAURANT_NAME_KEY";

    private static final int PERMISSION_LOCATION_CODE = 1;

    private GoogleMap mMap;
    private List<Restaurant> restaurantList;

    private LocationManager locationManager;
    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;

    private View mapView;

    @BindView(R.id.root_container)
    ConstraintLayout rootContainer;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);

        ButterKnife.bind(this);

        toolbar.setTitle(getString(R.string.title_activity_restaurants));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mapView = mapFragment.getView();

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
            Snackbar.make(rootContainer, getString(R.string.error_places_api_key_not_provided), Snackbar.LENGTH_LONG).show();
        }


        int i = 0;
        for (Restaurant restaurant : restaurantList) {
            LatLng restaurantPlace = new LatLng(restaurant.getLat(), restaurant.getLng());
            if (i == restaurantList.size() - 1){
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(restaurantPlace, 10));
            }
            i++;

            mMap.addMarker(new MarkerOptions().position(restaurantPlace).title(restaurant.getName())).setTag(restaurant.getPlaceId());
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, PERMISSION_LOCATION_CODE);
        } else {
            setCurrentUserLocation();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == PERMISSION_LOCATION_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                setCurrentUserLocation();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void setCurrentUserLocation(){
        mMap.setMyLocationEnabled(true);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, this);

        View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();

        //Position My Location button on bottom right
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        rlp.setMargins(0, 0, (int)getResources().getDimension(R.dimen.map_my_location_button_margin), (int)getResources().getDimension(R.dimen.map_my_location_button_margin));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if(marker.getTag() == null || marker.getTag().toString() == "") return false;

        //Run restaurant details activity when click on a marker
        Intent intent = new Intent(this, RestaurantDetailsActivity.class);
        intent.putExtra(PLACE_ID_KEY, marker.getTag().toString());
        intent.putExtra(RESTAURANT_NAME_KEY, marker.getTitle());
        startActivity(intent);

        return false;
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);
        mMap.animateCamera(cameraUpdate);
        locationManager.removeUpdates(this);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
