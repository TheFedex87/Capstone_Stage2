//package com.udacity.thefedex87.takemyorder.ui.viewmodels;
//
//import android.arch.lifecycle.ViewModel;
//import android.arch.lifecycle.ViewModelProvider;
//
//import com.udacity.thefedex87.takemyorder.room.AppDatabase;
//import com.udacity.thefedex87.takemyorder.room.entity.FoodTypes;
//
///**
// * Created by feder on 03/07/2018.
// */
//
//public class CheckoutOrderViewModelFactory extends ViewModelProvider.NewInstanceFactory {
//    private final AppDatabase db;
//    private final FoodTypes foodTypes;
//
//    public CheckoutOrderViewModelFactory(AppDatabase db, FoodTypes foodType){
//        this.db = db;
//        this.foodTypes = foodType;
//    }
//
//    public <T extends ViewModel>T create(Class<T> modelClass){
//        return (T) new CheckoutOrderViewModel(db, foodTypes);
//    }
//}
