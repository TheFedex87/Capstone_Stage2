package com.udacity.thefedex87.takemyorder.ui.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.udacity.thefedex87.takemyorder.room.AppDatabase;
import com.udacity.thefedex87.takemyorder.room.entity.FavouriteMeal;
import com.udacity.thefedex87.takemyorder.room.entity.Ingredient;

import java.util.List;

/**
 * Created by federico.creti on 28/06/2018.
 */

public class DishDetailsViewModel extends ViewModel {
    private LiveData<FavouriteMeal> userFavouriteMealByMealId;
    private LiveData<FavouriteMeal> favouriteMealByMealId;
    private LiveData<Ingredient> ingredient;

    private LiveData<List<Ingredient>> ingredientsOfMeal;

    private AppDatabase db;

    public DishDetailsViewModel(AppDatabase db, String mealId, long userRoomId, String restaurantId){
        this.db = db;

        userFavouriteMealByMealId = db.favouriteMealsDao().getUserFavouriteMealById(mealId, userRoomId, restaurantId);
        favouriteMealByMealId = db.favouriteMealsDao().getFavouriteMealById(mealId, restaurantId);
    }

    public void setMealId(String mealId){
        ingredientsOfMeal = db.favouriteMealsDao().ingredientsOfMeal(mealId);
    }

    public void setData(String mealId, long userRoomId, String restaurantId){
        userFavouriteMealByMealId = db.favouriteMealsDao().getUserFavouriteMealById(mealId, userRoomId, restaurantId);
        favouriteMealByMealId = db.favouriteMealsDao().getFavouriteMealById(mealId, restaurantId);
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
    public LiveData<List<Ingredient>> getIngredientsOfMeal() { return ingredientsOfMeal; }
}
