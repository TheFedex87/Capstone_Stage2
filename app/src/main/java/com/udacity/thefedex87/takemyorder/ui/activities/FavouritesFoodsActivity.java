package com.udacity.thefedex87.takemyorder.ui.activities;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.udacity.thefedex87.takemyorder.R;
import com.udacity.thefedex87.takemyorder.models.Food;
import com.udacity.thefedex87.takemyorder.room.AppDatabase;
import com.udacity.thefedex87.takemyorder.room.entity.FavouriteMeal;
import com.udacity.thefedex87.takemyorder.room.entity.FoodTypes;
import com.udacity.thefedex87.takemyorder.room.entity.Ingredient;
import com.udacity.thefedex87.takemyorder.room.entity.Meal;
import com.udacity.thefedex87.takemyorder.ui.fragments.MenuCompleteFragment;
import com.udacity.thefedex87.takemyorder.ui.viewmodels.FavouritesViewModel;
import com.udacity.thefedex87.takemyorder.ui.viewmodels.FavouritesViewModelFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavouritesFoodsActivity extends AppCompatActivity implements UserRoomContainer {
    private String restaurantId;
    private MenuCompleteFragment menuCompleteFragment;
    private List<Meal> currentOrder;
    private long userRoomId;

    private HashMap<FoodTypes, List<Meal>> favourites;

    @BindView(R.id.menu_icon_container)
    FrameLayout menuIconContainer;

    @BindView(R.id.counter_container)
    FrameLayout counterContainer;

    @BindView(R.id.couter_value)
    TextView counterValue;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.toolbar_container)
    CollapsingToolbarLayout
    collapsingToolbarLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites_foods);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(CustomerMainActivity.RESTAURANT_ID_KEY)  && intent.hasExtra(CustomerMainActivity.USER_ID_KEY)){
            restaurantId = intent.getStringExtra(CustomerMainActivity.RESTAURANT_ID_KEY);
            userRoomId = intent.getLongExtra(CustomerMainActivity.USER_ID_KEY, -1);

            ButterKnife.bind(this);

            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            collapsingToolbarLayout.setTitle(getString(R.string.favourites));

            favourites = new HashMap<>();
            favourites.put(FoodTypes.STARTER, new ArrayList<Meal>());
            favourites.put(FoodTypes.MAINDISH, new ArrayList<Meal>());
            favourites.put(FoodTypes.SIDEDISH, new ArrayList<Meal>());
            favourites.put(FoodTypes.DESSERT, new ArrayList<Meal>());

            menuIconContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

            setupViewModel();

            //This activity use the same fragment of the menu
            menuCompleteFragment = (MenuCompleteFragment)getSupportFragmentManager().findFragmentById(R.id.restaurant_menu);
        }
    }

    public long getUserRoomId(){
        return userRoomId;
    }

    private void setupViewModel() {
        FavouritesViewModelFactory favouritesViewModelFactory = new FavouritesViewModelFactory(AppDatabase.getInstance(this), restaurantId, userRoomId);
        final FavouritesViewModel favouritesViewModel = ViewModelProviders.of(this, favouritesViewModelFactory).get(FavouritesViewModel.class);

        //Using the viewmodel of this activity retrieve the list of user favourites
        favouritesViewModel.getFavouriteMealsOfUser().observe(this, new Observer<List<FavouriteMeal>>() {
            @Override
            public void onChanged(@Nullable List<FavouriteMeal> favouriteMeals) {

                //Clean the list of favourites, otherwise if remove a favourite the whole list will be added again to the current favourites causing duplicates
                favourites.get(FoodTypes.STARTER).clear();
                favourites.get(FoodTypes.MAINDISH).clear();
                favourites.get(FoodTypes.SIDEDISH).clear();
                favourites.get(FoodTypes.DESSERT).clear();

                //For each favourite, create the Food entity which will be passed to the meal list
                for(final FavouriteMeal favouriteMeal : favouriteMeals){
                    final Food food = new Food();
                    food.setFoodType(favouriteMeal.getFoodType());
                    food.setImageName(favouriteMeal.getImageName());
                    food.setMealId(favouriteMeal.getMealId());
                    food.setName(favouriteMeal.getName());
                    food.setPrice(favouriteMeal.getPrice());
                    food.setDescription(favouriteMeal.getDescription());

                    //Using the viewmodle, extract the list of the ingredient of this meal, and assign it the food entity
                    favouritesViewModel.setMealId(favouriteMeal.getMealId());
                    final LiveData<List<Ingredient>> ingredientsLiveData = favouritesViewModel.getIngredientsOfMeal();
                    ingredientsLiveData.observe(FavouritesFoodsActivity.this, new Observer<List<Ingredient>>() {
                        @Override
                        public void onChanged(@Nullable List<Ingredient> ingredients) {
                            ingredientsLiveData.removeObserver(this);
                            food.setIngredients(ingredients);
                        }
                    });
                    favourites.get(favouriteMeal.getFoodType()).add(food);
                }

                menuCompleteFragment.setMenu(favourites, restaurantId);
                if(currentOrder != null) menuCompleteFragment.setCurrentOrder(currentOrder);
            }
        });

        //Retrieve the current order from ViewModel
        favouritesViewModel.getCurrentOrderList().observe(this, new Observer<List<Meal>>() {
            @Override
            public void onChanged(@Nullable List<Meal> currentOrderEntries) {
                //TODO: gestire se menuCompleteFragment fosse null perchè non ancora creato e/o agganciato
                //if (menuCompleteFragment != null) menuCompleteFragment.setCurrentOrder(currentOrderEntries);
                menuCompleteFragment.setCurrentOrder(currentOrderEntries);
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
