package com.udacity.thefedex87.takemyorder.ui.activities;

import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.udacity.thefedex87.takemyorder.R;
import com.udacity.thefedex87.takemyorder.room.entity.Meal;
import com.udacity.thefedex87.takemyorder.ui.fragments.DishDescriptionFragment;

public class DishDescriptionActivity extends AppCompatActivity {

    private Meal meal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_description);

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if(bundle != null && bundle.containsKey(CustomerMainActivity.FOOD_DESCRIPTION_KEY)){
                ActivityCompat.postponeEnterTransition(this);
                meal = bundle.getParcelable(CustomerMainActivity.FOOD_DESCRIPTION_KEY);
                fragmentLoaded();
            }
        }
    }

    public void fragmentLoaded(){
        DishDescriptionFragment dishDescriptionFragment = (DishDescriptionFragment) getSupportFragmentManager().findFragmentById(R.id.dish_description);
        dishDescriptionFragment.setMeal(meal);
    }
}
