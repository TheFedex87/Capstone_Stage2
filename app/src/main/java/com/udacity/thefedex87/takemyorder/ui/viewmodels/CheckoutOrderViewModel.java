package com.udacity.thefedex87.takemyorder.ui.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.udacity.thefedex87.takemyorder.room.AppDatabase;
import com.udacity.thefedex87.takemyorder.room.entity.CurrentOrderGrouped;
import com.udacity.thefedex87.takemyorder.room.entity.FoodTypes;
import com.udacity.thefedex87.takemyorder.room.entity.Meal;

import java.util.List;

/**
 * Created by feder on 03/07/2018.
 */

public class CheckoutOrderViewModel extends ViewModel {
    private AppDatabase db;
    private LiveData<List<CurrentOrderGrouped>> currentOrderByFoodType;
    private LiveData<List<Meal>> currentOrder;
    private long userRoomId;


    public CheckoutOrderViewModel(AppDatabase db, long userRoomId) {
        this.userRoomId = userRoomId;
        this.db = db;
        currentOrder = db.currentOrderDao().getCurrentOrderList(userRoomId);
    }

    public void setFoodType(FoodTypes foodType){
        currentOrderByFoodType = db.currentOrderDao().getCurrentOrderByFoodType(foodType, userRoomId);
    }

    public LiveData<List<CurrentOrderGrouped>> getCurrentOrderByFoodType() {return currentOrderByFoodType;}
    public LiveData<List<Meal>> getCurrentOrder() {return currentOrder;}
}
