package com.udacity.thefedex87.takemyorder.ui.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.udacity.thefedex87.takemyorder.room.AppDatabase;
import com.udacity.thefedex87.takemyorder.room.entity.CurrentOrderEntry;

import java.util.List;

/**
 * Created by federico.creti on 14/06/2018.
 */

public class CustomerMainViewModel extends AndroidViewModel {
    private LiveData<List<CurrentOrderEntry>> currentOrderList;

    public CustomerMainViewModel(@NonNull Application application) {
        super(application);

        AppDatabase db = AppDatabase.getInstance(application);
        currentOrderList = db.currentOrderDao().getCurrentOrderList();
    }

    public LiveData<List<CurrentOrderEntry>> getCurrentOrderList() {
        return currentOrderList;
    }
}
