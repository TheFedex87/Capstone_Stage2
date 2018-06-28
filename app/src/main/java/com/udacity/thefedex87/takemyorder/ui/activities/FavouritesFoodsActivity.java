package com.udacity.thefedex87.takemyorder.ui.activities;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.udacity.thefedex87.takemyorder.R;
import com.udacity.thefedex87.takemyorder.models.Food;
import com.udacity.thefedex87.takemyorder.room.AppDatabase;
import com.udacity.thefedex87.takemyorder.room.entity.FavouriteMeal;
import com.udacity.thefedex87.takemyorder.room.entity.FoodTypes;
import com.udacity.thefedex87.takemyorder.room.entity.Meal;
import com.udacity.thefedex87.takemyorder.ui.fragments.MenuCompleteFragment;
import com.udacity.thefedex87.takemyorder.ui.viewmodels.FavouritesViewModel;
import com.udacity.thefedex87.takemyorder.ui.viewmodels.FavouritesViewModelFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavouritesFoodsActivity extends AppCompatActivity {
    private String restaurantId;
    private MenuCompleteFragment menuCompleteFragment;
    private List<Meal> currentOrder;

    private HashMap<FoodTypes, List<Meal>> favourites;

    @BindView(R.id.counter_container)
    FrameLayout counterContainer;

    @BindView(R.id.couter_value)
    TextView counterValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites_foods);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(CustomerMainActivity.RESTAURANT_ID_KEY)){
            restaurantId = intent.getStringExtra(CustomerMainActivity.RESTAURANT_ID_KEY);

            ButterKnife.bind(this);

            favourites = new HashMap<>();
            favourites.put(FoodTypes.STARTER, new ArrayList<Meal>());
            favourites.put(FoodTypes.MAINDISH, new ArrayList<Meal>());
            favourites.put(FoodTypes.SIDEDISH, new ArrayList<Meal>());
            favourites.put(FoodTypes.DESSERT, new ArrayList<Meal>());

            setupViewModel();

            menuCompleteFragment = (MenuCompleteFragment)getSupportFragmentManager().findFragmentById(R.id.restaurant_menu);
        }
    }

    private void setupViewModel() {
        FavouritesViewModelFactory favouritesViewModelFactory = new FavouritesViewModelFactory(AppDatabase.getInstance(this), restaurantId);
        FavouritesViewModel favouritesViewModel = ViewModelProviders.of(this, favouritesViewModelFactory).get(FavouritesViewModel.class);

        favouritesViewModel.getFavouriteMeals().observe(this, new Observer<List<FavouriteMeal>>() {
            @Override
            public void onChanged(@Nullable List<FavouriteMeal> favouriteMeals) {

                favourites.get(FoodTypes.STARTER).clear();
                favourites.get(FoodTypes.MAINDISH).clear();
                favourites.get(FoodTypes.SIDEDISH).clear();
                favourites.get(FoodTypes.DESSERT).clear();

                for(FavouriteMeal favouriteMeal : favouriteMeals){
                    Food food = new Food();
                    food.setFoodType(favouriteMeal.getFoodType());
                    food.setImageName(favouriteMeal.getImageName());
                    food.setMealId(favouriteMeal.getMealId());
                    food.setName(favouriteMeal.getName());
                    food.setPrice(favouriteMeal.getPrice());
                    food.setDescription(favouriteMeal.getDescription());

                    favourites.get(favouriteMeal.getFoodType()).add(food);
                }

                menuCompleteFragment.setMenu(favourites, restaurantId);
                if(currentOrder != null) menuCompleteFragment.setCurrentOrder(currentOrder);
            }
        });


        favouritesViewModel.getCurrentOrderList().observe(this, new Observer<List<Meal>>() {
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

                AnimatorSet counterAnimation = (AnimatorSet) AnimatorInflater
                        .loadAnimator(FavouritesFoodsActivity.this, R.animator.food_counter_animation);
                counterAnimation.setTarget(counterContainer);

                counterAnimation.start();

            }
        });
    }
}
