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

public class RestaurantMenuActivity extends AppCompatActivity {
    private String restaurantId;
    private MenuCompleteFragment menuCompleteFragment;
    private List<Meal> currentOrder;

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

//            FirebaseDatabase db = FirebaseDatabase.getInstance();
//            db.getReference("restaurants/" + restaurantId + "/getMenu").addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    for(DataSnapshot menuSnapshot : dataSnapshot.getChildren()){
//                        if (menuSnapshot.getKey().toLowerCase().equals("foods")){
//                            for(DataSnapshot foodTypes : menuSnapshot.getChildren()){
//                                List<Meal> foods = new ArrayList();
//                                for(DataSnapshot foodType : foodTypes.getChildren()){
//                                    Food food = foodType.getValue(Food.class);
//                                    food.setMealId(foodType.getKey());
//                                    foods.add(food);
//                                }
//                                getMenu.put(getFoodTypeFromKey(foodTypes.getKey()), foods);
//                            }
//                        } else if(menuSnapshot.getKey().toLowerCase().equals("drinks")){
//                            List<Meal> drinks = new ArrayList();
//                            for(DataSnapshot drinkSnapshot : menuSnapshot.getChildren()){
//                                Drink drink = drinkSnapshot.getValue(Drink.class);
//                                drink.setMealId(drinkSnapshot.getKey());
//                                drinks.add(drink);
//                            }
//                            getMenu.put(FoodTypes.DRINK, drinks);
//                        }
//                    }
//
//                    //TODO: gestire se menuCompleteFragment fosse null perchè non ancora creato e/o agganciato
//                    MenuCompleteFragment menuCompleteFragment = (MenuCompleteFragment) getSupportFragmentManager().findFragmentById(R.id.restaurant_menu);
//                    menuCompleteFragment.setMenu(getMenu);
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });

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
        RestaurantMenuViewModelFactory restaurantMenuViewModelFactory = new RestaurantMenuViewModelFactory(AppDatabase.getInstance(this), restaurantId);
        RestaurantMenuViewModel restaurantMenuViewModel = ViewModelProviders.of(this, restaurantMenuViewModelFactory).get(RestaurantMenuViewModel.class);

        restaurantMenuViewModel.getCurrentOrderList().observe(this, new Observer<List<Meal>>() {
            @Override
            public void onChanged(@Nullable List<Meal> currentOrderEntries) {
                //TODO: gestire se menuCompleteFragment fosse null perchè non ancora creato e/o agganciato
                if (menuCompleteFragment != null) menuCompleteFragment.setCurrentOrder(currentOrderEntries);
                currentOrder = currentOrderEntries;
                if (currentOrderEntries.size() > 0) {
                    counterContainer.setVisibility(View.VISIBLE);
                }
                else {
                    counterContainer.setVisibility(View.GONE);
                    return;
                }

                counterValue.setText(String.valueOf(currentOrderEntries.size() <= 99 ? currentOrderEntries.size() : 99));

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
                AnimatorSet counterAnimation = (AnimatorSet) AnimatorInflater
                        .loadAnimator(RestaurantMenuActivity.this, R.animator.food_counter_animation);
                counterAnimation.setTarget(counterContainer);

                counterAnimation.start();

            }
        });

        restaurantMenuViewModel.getMenu().observe(this, new Observer<HashMap<FoodTypes, List<Meal>>>() {
            @Override
            public void onChanged(@Nullable HashMap<FoodTypes, List<Meal>> foodTypesListHashMap) {
                //TODO: gestire se menuCompleteFragment fosse null perchè non ancora creato e/o agganciato
                menuCompleteFragment = (MenuCompleteFragment) getSupportFragmentManager().findFragmentById(R.id.restaurant_menu);
                menuCompleteFragment.setMenu(foodTypesListHashMap, restaurantId);
                if (currentOrder != null) menuCompleteFragment.setCurrentOrder(currentOrder);
            }
        });
    }


}
