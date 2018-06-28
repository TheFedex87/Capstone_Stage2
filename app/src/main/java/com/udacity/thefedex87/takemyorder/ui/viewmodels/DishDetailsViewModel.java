package com.udacity.thefedex87.takemyorder.ui.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.udacity.thefedex87.takemyorder.room.AppDatabase;
import com.udacity.thefedex87.takemyorder.room.entity.FavouriteMeal;
import com.udacity.thefedex87.takemyorder.room.entity.Meal;

import java.util.List;

/**
 * Created by federico.creti on 28/06/2018.
 */

public class DishDetailsViewModel extends ViewModel {
    private LiveData<List<FavouriteMeal>> favouriteMeals;
    private LiveData<FavouriteMeal> favouriteMealByMealId;

    public DishDetailsViewModel(AppDatabase db, String mealId, String restaurantId){


        favouriteMeals = db.favouriteMealsDao().getFavouriteMeals();
        favouriteMealByMealId = db.favouriteMealsDao().getFavouriteMealById(mealId, restaurantId);
    }

    public LiveData<List<FavouriteMeal>> getFavouriteMeals() { return favouriteMeals; }

    public LiveData<FavouriteMeal> getFavouriteMealByMealId() {
        return favouriteMealByMealId;
    }
}
