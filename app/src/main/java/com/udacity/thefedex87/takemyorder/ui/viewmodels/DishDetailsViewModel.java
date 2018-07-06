package com.udacity.thefedex87.takemyorder.ui.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.udacity.thefedex87.takemyorder.room.AppDatabase;
import com.udacity.thefedex87.takemyorder.room.entity.FavouriteMeal;
import com.udacity.thefedex87.takemyorder.room.entity.Ingredient;

/**
 * Created by federico.creti on 28/06/2018.
 */

public class DishDetailsViewModel extends ViewModel {
    private LiveData<FavouriteMeal> userFavouriteMealByMealId;
    private LiveData<FavouriteMeal> favouriteMealByMealId;
    private LiveData<Ingredient> ingredient;

    private AppDatabase db;

    public DishDetailsViewModel(AppDatabase db, String mealId, String restaurantId, long userRoomId){
        this.db = db;

        userFavouriteMealByMealId = db.favouriteMealsDao().getUserFavouriteMealById(mealId, userRoomId);
        favouriteMealByMealId = db.favouriteMealsDao().getFavouriteMealById(mealId);
    }

    public void setIngredientName(String ingredientName){
        ingredient = db.favouriteMealsDao().getIngredientByName(ingredientName);
    }

    public LiveData<FavouriteMeal> getUserFavouriteMealByMealId() {
        return userFavouriteMealByMealId;
    }

    public LiveData<FavouriteMeal> getFavouriteMealByMealId() {
        return favouriteMealByMealId;
    }

    public LiveData<Ingredient> getIngredient() {return ingredient;}
}
