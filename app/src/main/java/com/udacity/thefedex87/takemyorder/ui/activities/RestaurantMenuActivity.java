package com.udacity.thefedex87.takemyorder.ui.activities;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.udacity.thefedex87.takemyorder.R;
import com.udacity.thefedex87.takemyorder.room.entity.Meal;
import com.udacity.thefedex87.takemyorder.room.AppDatabase;
import com.udacity.thefedex87.takemyorder.room.entity.FoodTypes;
import com.udacity.thefedex87.takemyorder.ui.fragments.MenuCompleteFragment;
import com.udacity.thefedex87.takemyorder.ui.viewmodels.RestaurantMenuViewModel;
import com.udacity.thefedex87.takemyorder.ui.viewmodels.RestaurantMenuViewModelFactory;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static android.view.View.SCALE_X;
import static android.view.View.SCALE_Y;

public class RestaurantMenuActivity extends AppCompatActivity implements UserRoomContainer {
    private String restaurantId;
    private MenuCompleteFragment menuCompleteFragment;
    private List<Meal> currentOrder;
    private long userRoomId;

    @BindView(R.id.menu_icon_container)
    FrameLayout menuIconContainer;

    @BindView(R.id.counter_container)
    FrameLayout counterContainer;

    @BindView(R.id.couter_value)
    TextView counterValue;

    @BindView(R.id.toolbar_container)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.activity_title)
    TextView toolbarTitle;

    @BindView(R.id.menu_loading)
    ProgressBar menuLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_menu);

        //getMenu = new HashMap<FoodTypes, List<Meal>>();

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(LoginMapsActivity.USER_RESTAURANT_KEY)){
            ButterKnife.bind(this);

            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbarTitle.setText(getString(R.string.menu));

            restaurantId = intent.getStringExtra(LoginMapsActivity.USER_RESTAURANT_KEY);
            userRoomId = intent.getLongExtra(CustomerMainActivity.USER_ID_KEY, -1);

            setupViewModel(restaurantId);

            menuIconContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

        } else {
            Timber.d("No intent provided, or missing key");
        }
    }

    private void setupViewModel(final String restaurantId){
        //Retreive the ViewModel for this activity
        RestaurantMenuViewModelFactory restaurantMenuViewModelFactory = new RestaurantMenuViewModelFactory(AppDatabase.getInstance(this), restaurantId);
        RestaurantMenuViewModel restaurantMenuViewModel = ViewModelProviders.of(this, restaurantMenuViewModelFactory).get(RestaurantMenuViewModel.class);

        //Get the current order from ViewModel
        restaurantMenuViewModel.getCurrentOrderList().observe(this, new Observer<List<Meal>>() {
            @Override
            public void onChanged(@Nullable List<Meal> currentOrderEntries) {
                //Set the current order to the fragment
                if (menuCompleteFragment != null) menuCompleteFragment.setCurrentOrder(currentOrderEntries);
                currentOrder = currentOrderEntries;

                //If there is something in the order show the counter into the menu icon in the toolbar
                if (currentOrderEntries.size() > 0) {
                    counterContainer.setVisibility(View.VISIBLE);
                }
                else {
                    counterContainer.setVisibility(View.GONE);
                    return;
                }

                //Set the counter text
                counterValue.setText(String.valueOf(currentOrderEntries.size() <= 99 ? currentOrderEntries.size() : 99));

                //Animate the counter
                AnimatorSet counterAnimation = (AnimatorSet) AnimatorInflater
                        .loadAnimator(RestaurantMenuActivity.this, R.animator.food_counter_animation);
                counterAnimation.setTarget(counterContainer);

                counterAnimation.start();

//                AnimatorSet counterAnimationIncrease = new AnimatorSet();
//                counterAnimationIncrease.playTogether(
//                        ObjectAnimator.ofFloat(counterContainer, SCALE_X, 2.0f),
//                        ObjectAnimator.ofFloat(counterContainer, SCALE_Y, 2.0f)
//                );
//                counterAnimationIncrease.setDuration(75);
//                counterAnimationIncrease.setInterpolator(AnimationUtils.loadInterpolator(RestaurantMenuActivity.this, android.R
//                        .interpolator.fast_out_slow_in));
//
//                AnimatorSet counterAnimationDecrease = new AnimatorSet();
//                counterAnimationDecrease.playTogether(
//                        ObjectAnimator.ofFloat(counterContainer, SCALE_X, 1.0f),
//                        ObjectAnimator.ofFloat(counterContainer, SCALE_Y, 1.0f)
//                );
//                counterAnimationDecrease.setDuration(75);
//                counterAnimationDecrease.setStartDelay(75);
//                counterAnimationDecrease.setInterpolator(AnimationUtils.loadInterpolator(RestaurantMenuActivity.this, android.R
//                        .interpolator.fast_out_slow_in));

//                AnimatorSet counterAnimation = new AnimatorSet();
//                counterAnimation.playSequentially(counterAnimationIncrease, counterAnimationDecrease);


            }
        });

        //Get the menu of the restaurant from the ViewModel
        restaurantMenuViewModel.getMenu().observe(this, new Observer<HashMap<FoodTypes, List<Meal>>>() {
            @Override
            public void onChanged(@Nullable HashMap<FoodTypes, List<Meal>> foodTypesListHashMap) {
                menuLoading.setVisibility(View.GONE);
                menuCompleteFragment = (MenuCompleteFragment) getSupportFragmentManager().findFragmentById(R.id.restaurant_menu);
                menuCompleteFragment.setMenu(foodTypesListHashMap, restaurantId);
                if (currentOrder != null) menuCompleteFragment.setCurrentOrder(currentOrder);
            }
        });
    }

    public long getUserRoomId(){
        return userRoomId;
    }
}
