package com.udacity.thefedex87.takemyorder.ui.viewmodels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.udacity.thefedex87.takemyorder.room.AppDatabase;

/**
 * Created by federico.creti on 29/06/2018.
 */

public class CustomerMainViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final AppDatabase db;
    private final String restaurantId;
    private final String userName;
    private final long userRoomId;

    public CustomerMainViewModelFactory(AppDatabase db, String restaurantId, String userName, long userRoomId){
        this.db = db;
        this.restaurantId = restaurantId;
        this.userName = userName;
        this.userRoomId = userRoomId;
    }

    public <T extends ViewModel>T create(Class<T> modelClass){
        return (T) new CustomerMainViewModel(db, restaurantId, userName, userRoomId);
    }
}
