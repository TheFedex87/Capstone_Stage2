package com.udacity.thefedex87.takemyorder.ui.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.udacity.thefedex87.takemyorder.models.Drink;
import com.udacity.thefedex87.takemyorder.models.Food;
import com.udacity.thefedex87.takemyorder.models.Meal;
import com.udacity.thefedex87.takemyorder.room.AppDatabase;
import com.udacity.thefedex87.takemyorder.room.entity.CurrentOrderEntry;
import com.udacity.thefedex87.takemyorder.room.entity.FoodTypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import timber.log.Timber;

/**
 * Created by federico.creti on 21/06/2018.
 */

public class RestaurantMenuViewModel extends ViewModel {
    private LiveData<List<CurrentOrderEntry>> currentOrderList;
    private MutableLiveData<HashMap<FoodTypes, List<Meal>>> menuLiveData;
    private String restaurantId;


    public RestaurantMenuViewModel(AppDatabase db, String restaurantId) {


        currentOrderList = db.currentOrderDao().getCurrentOrderList();
        this.restaurantId = restaurantId;
        menuLiveData = new MutableLiveData<>();
        retrieveRestaurantMenu();
    }

    private void retrieveRestaurantMenu(){
        Timber.d("Retrieving restaurant menu from firebase");
        final HashMap<FoodTypes, List<Meal>> menu = new HashMap<>();
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
                                    food.setFoodTypes(getFoodTypeFromKey(foodTypes.getKey()));
                                    foods.add(food);
                                }
                                menu.put(getFoodTypeFromKey(foodTypes.getKey()), foods);
                            }
                        } else if(menuSnapshot.getKey().toLowerCase().equals("drinks")){
                            List<Meal> drinks = new ArrayList();
                            for(DataSnapshot drinkSnapshot : menuSnapshot.getChildren()){
                                Drink drink = drinkSnapshot.getValue(Drink.class);
                                drink.setMealId(drinkSnapshot.getKey());
                                drink.setFoodTypes(FoodTypes.DRINK);
                                drinks.add(drink);
                            }
                            menu.put(FoodTypes.DRINK, drinks);
                        }
                    }
                    menuLiveData.setValue(menu);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
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

    public LiveData<List<CurrentOrderEntry>> getCurrentOrderList() {
        return currentOrderList;
    }

    public LiveData<HashMap<FoodTypes, List<Meal>>> menu() { return menuLiveData; }
}
