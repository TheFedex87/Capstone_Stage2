package com.udacity.thefedex87.takemyorder.ui.activities;

import android.content.Intent;
import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.transition.Transition;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.udacity.thefedex87.takemyorder.R;
import com.udacity.thefedex87.takemyorder.dagger.ApplicationModule;
import com.udacity.thefedex87.takemyorder.dagger.DaggerNetworkComponent;
import com.udacity.thefedex87.takemyorder.dagger.NetworkComponent;
import com.udacity.thefedex87.takemyorder.models.Food;
import com.udacity.thefedex87.takemyorder.room.entity.Meal;
import com.udacity.thefedex87.takemyorder.ui.fragments.DishDescriptionFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DishDescriptionActivity extends AppCompatActivity {
    @BindView(R.id.toolbar_container)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.dish_description_meal_image)
    ImageView dishDescriptionMealImage;

    @BindView(R.id.dish_description)
    TextView dishDescription;

    private Food food;
    private DishDescriptionFragment dishDescriptionFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_description);

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if(bundle != null && bundle.containsKey(CustomerMainActivity.FOOD_DESCRIPTION_KEY)){
                ButterKnife.bind(this);

                food = bundle.getParcelable(CustomerMainActivity.FOOD_DESCRIPTION_KEY);

                //dishDescriptionFragment = (DishDescriptionFragment) getSupportFragmentManager().findFragmentById(R.id.dish_description);
                //dishDescriptionFragment.setFood((Food)meal);

                initUi();
            }
        }
    }

    private void initUi(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        collapsingToolbarLayout.setTitle(food.getName());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dishDescriptionMealImage.setTransitionName("foodTransition");
            //getWindow().setSharedElementEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.curve));
        }

        ActivityCompat.postponeEnterTransition(this);

        NetworkComponent networkComponent = DaggerNetworkComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .build();

        String imagePath = "https://firebasestorage.googleapis.com/v0/b/takemyorder-8a08a.appspot.com/o/meals_images%2F" + food.getMealId() +  "?alt=media";
        networkComponent.getPicasso().load(imagePath).into(dishDescriptionMealImage, new Callback() {
            @Override
            public void onSuccess() {
                ActivityCompat.startPostponedEnterTransition(DishDescriptionActivity.this);
            }

            @Override
            public void onError() {
                ActivityCompat.startPostponedEnterTransition(DishDescriptionActivity.this);
            }
        });

        dishDescription.setText(food.getDescription());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
