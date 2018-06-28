package com.udacity.thefedex87.takemyorder.ui.viewmodels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.udacity.thefedex87.takemyorder.room.AppDatabase;

/**
 * Created by feder on 28/06/2018.
 */

public class FavouritesViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final AppDatabase db;
    private final String restaurantId;

    public FavouritesViewModelFactory(AppDatabase db, String restaurantId){
        this.db = db;
        this.restaurantId = restaurantId;
    }

    public <T extends ViewModel>T create(Class<T> modelClass){
        return (T) new FavouritesViewModel(db, restaurantId);
    }
}
