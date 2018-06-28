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
    private AppDatabase db;
    private String mealId;

    private LiveData<List<FavouriteMeal>> favouriteMeals;
    private LiveData<FavouriteMeal> favouriteMealByMealId;

    public DishDetailsViewModel(AppDatabase db, String mealId){
        this.db = db;
        this.mealId = mealId;

        favouriteMeals = db.favouriteMealsDao().getFavouriteMeals();
        favouriteMealByMealId = db.favouriteMealsDao().getFavouriteMealById(mealId);
    }

    public LiveData<List<FavouriteMeal>> getFavouriteMeals() { return favouriteMeals; }

    public LiveData<FavouriteMeal> getFavouriteMealByMealId() {
        return favouriteMealByMealId;
    }
}
