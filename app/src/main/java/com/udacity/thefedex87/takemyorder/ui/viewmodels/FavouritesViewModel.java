package com.udacity.thefedex87.takemyorder.ui.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.udacity.thefedex87.takemyorder.room.AppDatabase;
import com.udacity.thefedex87.takemyorder.room.entity.FavouriteMeal;
import com.udacity.thefedex87.takemyorder.room.entity.Meal;

import java.util.List;

/**
 * Created by feder on 28/06/2018.
 */

public class FavouritesViewModel extends ViewModel {
    private LiveData<List<FavouriteMeal>> favouriteMeals;
    private LiveData<List<Meal>> currentOrderList;

    public FavouritesViewModel(AppDatabase db, String restaurantId){
        favouriteMeals = db.favouriteMealsDao().getFavouriteMeals();
        currentOrderList = db.currentOrderDao().getCurrentOrderList();
    }

    public LiveData<List<FavouriteMeal>> getFavouriteMeals() { return favouriteMeals; }
    public LiveData<List<Meal>> getCurrentOrderList() {
        return currentOrderList;
    }
}
