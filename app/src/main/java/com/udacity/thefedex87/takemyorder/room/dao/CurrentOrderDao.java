package com.udacity.thefedex87.takemyorder.room.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.udacity.thefedex87.takemyorder.room.entity.CurrentOrderEntry;

import java.util.List;

/**
 * Created by federico.creti on 14/06/2018.
 */

@Dao
public interface CurrentOrderDao {
    @Query("SELECT * FROM current_order")
    LiveData<List<CurrentOrderEntry>> getCurrentOrderList();

    @Insert
    void insertFood(CurrentOrderEntry food);

    @Delete
    void deleteFood(CurrentOrderEntry food);
}
