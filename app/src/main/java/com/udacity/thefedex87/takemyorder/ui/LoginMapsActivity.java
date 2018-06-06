package com.udacity.thefedex87.takemyorder.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.udacity.thefedex87.takemyorder.R;

public class LoginMapsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_maps);

        Intent intent = new Intent(this, RestaurantsMapActivity.class);
        startActivity(intent);
    }
}
