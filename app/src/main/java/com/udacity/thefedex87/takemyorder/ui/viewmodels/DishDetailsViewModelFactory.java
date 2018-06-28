package com.udacity.thefedex87.takemyorder.ui.viewmodels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.udacity.thefedex87.takemyorder.room.AppDatabase;

/**
 * Created by federico.creti on 28/06/2018.
 */

public class DishDetailsViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final AppDatabase db;
    private final String mealId;
    private final String restaurantId;

    public DishDetailsViewModelFactory(AppDatabase db, String mealId, String restaurantId){
        this.db = db;
        this.mealId = mealId;
        this.restaurantId = restaurantId;
    }

    public <T extends ViewModel>T create(Class<T> modelClass){
        return (T) new DishDetailsViewModel(db, mealId, restaurantId);
    }
}