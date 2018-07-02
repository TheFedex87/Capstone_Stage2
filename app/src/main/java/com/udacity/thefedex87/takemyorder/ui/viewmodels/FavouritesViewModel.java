package com.udacity.thefedex87.takemyorder.ui.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.udacity.thefedex87.takemyorder.room.AppDatabase;
import com.udacity.thefedex87.takemyorder.room.entity.FavouriteMeal;
import com.udacity.thefedex87.takemyorder.room.entity.Ingredient;
import com.udacity.thefedex87.takemyorder.room.entity.Meal;

import java.util.List;

/**
 * Created by feder on 28/06/2018.
 */

public class FavouritesViewModel extends ViewModel {
    private LiveData<List<FavouriteMeal>> favouriteMeals;
    private LiveData<List<FavouriteMeal>> favouriteMealsOfUser;
    private LiveData<List<Meal>> currentOrderList;
    private LiveData<List<Ingredient>> ingredientsOfMeal;

    private AppDatabase db;
    private String restaurantId;

    public FavouritesViewModel(AppDatabase db, String restaurantId, long userId){
        this.db = db;
        this.restaurantId = restaurantId;
        favouriteMeals = db.favouriteMealsDao().getFavouriteMeals();
        favouriteMealsOfUser = db.favouriteMealsDao().getFavouriteMealsOfUser(userId);
        currentOrderList = db.currentOrderDao().getCurrentOrderList();
    }

    public void setMealId(String mealId){
        ingredientsOfMeal = db.favouriteMealsDao().ingredientsOfMeal(mealId, restaurantId);
    }

    public LiveData<List<FavouriteMeal>> getFavouriteMeals() { return favouriteMeals; }
    public LiveData<List<FavouriteMeal>> getFavouriteMealsOfUser() { return favouriteMealsOfUser; }
    public LiveData<List<Meal>> getCurrentOrderList() {
        return currentOrderList;
    }
    public LiveData<List<Ingredient>> getIngredientsOfMeal() { return ingredientsOfMeal; }
}
