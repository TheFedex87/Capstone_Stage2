package com.udacity.thefedex87.takemyorder.ui.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.udacity.thefedex87.takemyorder.models.Meal;
import com.udacity.thefedex87.takemyorder.room.AppDatabase;

import java.util.List;

/**
 * Created by federico.creti on 14/06/2018.
 */

public class CustomerMainViewModel extends AndroidViewModel {
    private LiveData<List<Meal>> currentOrderList;

    public CustomerMainViewModel(@NonNull Application application) {
        super(application);

        AppDatabase db = AppDatabase.getInstance(application);
        currentOrderList = db.currentOrderDao().getCurrentOrderList();
    }

    public LiveData<List<Meal>> getCurrentOrderList() {
        return currentOrderList;
    }
}
