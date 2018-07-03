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

public class CheckoutOrderViewModel extends AndroidViewModel {
    private AppDatabase db;
    private  LiveData<List<CurrentOrderGrouped>> currentOrderByFoodType;
    //private final LiveData<List<CurrentOrderGrouped>> currentOrderGroupByFoodType;


    public CheckoutOrderViewModel(@NonNull Application application) {
        super(application);
        db = AppDatabase.getInstance(application.getApplicationContext());
    }

    public void setFoodType(AppDatabase db, FoodTypes foodType){
        currentOrderByFoodType = db.currentOrderDao().getCurrentOrderByFoodType(foodType);
    }

    public LiveData<List<CurrentOrderGrouped>> getCurrentOrderByFoodType() {return currentOrderByFoodType;}
    //public LiveData<List<CurrentOrderGrouped>> getCurrentOrderGroupByFoodType() { return currentOrderGroupByFoodType; }
}
