package com.udacity.thefedex87.takemyorder.ui.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.udacity.thefedex87.takemyorder.R;
import com.udacity.thefedex87.takemyorder.models.Drink;
import com.udacity.thefedex87.takemyorder.models.Food;
import com.udacity.thefedex87.takemyorder.models.Meal;
import com.udacity.thefedex87.takemyorder.room.converter.FoodTypeConverter;
import com.udacity.thefedex87.takemyorder.room.entity.FoodTypes;
import com.udacity.thefedex87.takemyorder.ui.fragments.MenuCompleteFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import timber.log.Timber;

public class RestaurantMenuActivity extends AppCompatActivity {
    private String restaurantId;
    private HashMap<FoodTypes, List<Meal>> menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_menu);

        menu = new HashMap<FoodTypes, List<Meal>>();

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(LoginMapsActivity.USER_RESTAURANT_KEY)){
            restaurantId = intent.getStringExtra(LoginMapsActivity.USER_RESTAURANT_KEY);

            FirebaseDatabase db = FirebaseDatabase.getInstance();
            db.getReference("restaurants/" + restaurantId + "/menu").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot menuSnapshot : dataSnapshot.getChildren()){
                        if (menuSnapshot.getKey().toLowerCase().equals("foods")){
                            for(DataSnapshot foodTypes : menuSnapshot.getChildren()){
                                List<Meal> foods = new ArrayList();
                                for(DataSnapshot foodType : foodTypes.getChildren()){
                                    Food food = foodType.getValue(Food.class);
                                    food.setMealId(foodType.getKey());
                                    foods.add(food);
                                }
                                menu.put(getFoodTypeFromKey(foodTypes.getKey()), foods);
                            }
                        } else if(menuSnapshot.getKey().toLowerCase().equals("drinks")){
                            List<Meal> drinks = new ArrayList();
                            for(DataSnapshot drinkSnapshot : menuSnapshot.getChildren()){
                                Drink drink = drinkSnapshot.getValue(Drink.class);
                                drink.setMealId(drinkSnapshot.getKey());
                                drinks.add(drink);
                            }
                            menu.put(FoodTypes.DRINK, drinks);
                        }
                    }

                    //TODO: gestire se menuCompleteFragment fosse null perchè non ancora creato e/o agganciato
                    MenuCompleteFragment menuCompleteFragment = (MenuCompleteFragment) getSupportFragmentManager().findFragmentById(R.id.restaurant_menu);
                    menuCompleteFragment.setMenu(menu);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            Timber.d("No intent provided, or missing key");
        }
    }

    private FoodTypes getFoodTypeFromKey(String key){
        switch (key.toLowerCase()){
            case "desserts":
                return FoodTypes.DESSERT;
            case "maindishes":
                return FoodTypes.MAINDISH;
            case "sidedishes":
                return  FoodTypes.SIDEDISH;
            case "starters":
                return FoodTypes.STARTER;
            default:
                return FoodTypes.STARTER;
        }
    }
}
