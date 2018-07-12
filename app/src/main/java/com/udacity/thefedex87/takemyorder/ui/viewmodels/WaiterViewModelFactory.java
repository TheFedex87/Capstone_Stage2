package com.udacity.thefedex87.takemyorder.ui.viewmodels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.udacity.thefedex87.takemyorder.room.AppDatabase;

/**
 * Created by federico.creti on 12/07/2018.
 */

public class WaiterViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final String restaurantId;

    public WaiterViewModelFactory(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new WaiterViewModel(restaurantId);
    }
}
