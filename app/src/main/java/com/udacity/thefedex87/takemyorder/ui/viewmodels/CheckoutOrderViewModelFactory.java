package com.udacity.thefedex87.takemyorder.ui.viewmodels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.udacity.thefedex87.takemyorder.room.AppDatabase;
import com.udacity.thefedex87.takemyorder.room.entity.FoodTypes;

/**
 * Created by feder on 03/07/2018.
 */

public class CheckoutOrderViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final AppDatabase db;
    private final long userRoomId;

    public CheckoutOrderViewModelFactory(AppDatabase db, long userRoomId){
        this.db = db;
        this.userRoomId = userRoomId;
    }

    public <T extends ViewModel>T create(Class<T> modelClass){
        return (T) new CheckoutOrderViewModel(db, userRoomId);
    }
}
