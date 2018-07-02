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
    private final long userRoomId;

    public FavouritesViewModelFactory(AppDatabase db, String restaurantId, long userId){
        this.db = db;
        this.restaurantId = restaurantId;
        this.userRoomId = userId;
    }

    public <T extends ViewModel>T create(Class<T> modelClass){
        return (T) new FavouritesViewModel(db, restaurantId, userRoomId);
    }
}
