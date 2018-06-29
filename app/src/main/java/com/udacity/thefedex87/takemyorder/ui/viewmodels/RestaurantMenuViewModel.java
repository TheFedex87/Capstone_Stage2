package com.udacity.thefedex87.takemyorder.ui.viewmodels;

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
import com.udacity.thefedex87.takemyorder.room.entity.FavouriteMeal;
import com.udacity.thefedex87.takemyorder.room.entity.Meal;
import com.udacity.thefedex87.takemyorder.room.AppDatabase;
import com.udacity.thefedex87.takemyorder.room.entity.CurrentOrderGrouped;
import com.udacity.thefedex87.takemyorder.room.entity.FoodTypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import timber.log.Timber;

/**
 * Created by federico.creti on 21/06/2018.
 */

public class RestaurantMenuViewModel extends ViewModel {
    private AppDatabase db;
    private LiveData<List<Meal>> currentOrderList;
    private LiveData<List<Meal>> currentOrderListByMealId;
    private LiveData<List<CurrentOrderGrouped>> currentOrderListGrouped;
    private MutableLiveData<HashMap<FoodTypes, List<Meal>>> menuLiveData;

    private LiveData<FavouriteMeal> favouriteMealByeMealId;

    private String restaurantId;


    public RestaurantMenuViewModel(AppDatabase db, String restaurantId) {
        currentOrderList = db.currentOrderDao().getCurrentOrderList();
        currentOrderListGrouped = db.currentOrderDao().getCurrentOrderListGrouped();
        this.db = db;
        this.restaurantId = restaurantId;
        menuLiveData = new MutableLiveData<>();
        retrieveRestaurantMenu();
    }

    public void setFoodId(String mealId){
        currentOrderListByMealId = db.currentOrderDao().getCurrentOrderListByMealId(mealId);
        favouriteMealByeMealId = db.favouriteMealsDao().getFavouriteMealById(mealId, restaurantId);
    }

    private void retrieveRestaurantMenu(){
        Timber.d("Retrieving restaurant getMenu from firebase");
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
                                    food.setFoodType(getFoodTypeFromKey(foodTypes.getKey()));
                                    foods.add(food);
                                }
                                menu.put(getFoodTypeFromKey(foodTypes.getKey()), foods);
                            }
                        } else if(menuSnapshot.getKey().toLowerCase().equals("drinks")){
                            List<Meal> drinks = new ArrayList();
                            for(DataSnapshot drinkSnapshot : menuSnapshot.getChildren()){
                                Drink drink = drinkSnapshot.getValue(Drink.class);
                                drink.setMealId(drinkSnapshot.getKey());
                                drink.setFoodType(FoodTypes.DRINK);
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

    public LiveData<List<Meal>> getCurrentOrderList() {
        return currentOrderList;
    }

    public LiveData<HashMap<FoodTypes, List<Meal>>> getMenu() { return menuLiveData; }

    public LiveData<List<CurrentOrderGrouped>> getCurrentOrderListGrouped() { return currentOrderListGrouped; }

    public LiveData<List<Meal>> getCurrentOrdserListByMealId() {return currentOrderListByMealId; }

    public LiveData<FavouriteMeal> getFavouriteMealByeMealId(){
        return favouriteMealByeMealId;
    }
}
