package com.udacity.thefedex87.takemyorder.ui.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.udacity.thefedex87.takemyorder.room.AppDatabase;
import com.udacity.thefedex87.takemyorder.room.entity.FavouriteMeal;
import com.udacity.thefedex87.takemyorder.room.entity.Ingredient;
import com.udacity.thefedex87.takemyorder.room.entity.Meal;

import java.util.List;

/**
 * Created by feder on 28/06/2018.
 */

public class FavouritesViewModel extends ViewModel {
    private LiveData<List<FavouriteMeal>> favouriteMealsOfUser;
    private LiveData<List<Meal>> currentOrderList;

    private AppDatabase db;
    private String restaurantId;

    public FavouritesViewModel(AppDatabase db, String restaurantId, long userRoomId){
        this.db = db;
        this.restaurantId = restaurantId;
        favouriteMealsOfUser = db.favouriteMealsDao().getFavouriteMealsOfUser(userRoomId, restaurantId);
        currentOrderList = db.currentOrderDao().getCurrentOrderList(userRoomId, restaurantId);

    }

    public LiveData<List<FavouriteMeal>> getFavouriteMealsOfUser() { return favouriteMealsOfUser; }
    public LiveData<List<Meal>> getCurrentOrderList() {
        return currentOrderList;
    }
}
