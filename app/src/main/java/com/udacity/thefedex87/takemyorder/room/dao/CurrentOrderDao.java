package com.udacity.thefedex87.takemyorder.room.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.udacity.thefedex87.takemyorder.room.entity.FoodTypes;
import com.udacity.thefedex87.takemyorder.room.entity.Meal;
import com.udacity.thefedex87.takemyorder.room.entity.CurrentOrderGrouped;

import java.util.List;

/**
 * Created by federico.creti on 14/06/2018.
 */

@Dao
public interface CurrentOrderDao {
    @Query("SELECT * FROM current_order")
    LiveData<List<Meal>> getCurrentOrderList();

    @Query("SELECT * FROM current_order WHERE mealId = :mealId")
    LiveData<List<Meal>> getCurrentOrderListByMealId(String mealId);

    @Query("SELECT price, mealId, COUNT(mealId) AS count FROM current_order GROUP BY mealId")
    LiveData<List<CurrentOrderGrouped>> getCurrentOrderListGrouped();

    @Query("SELECT *, COUNT(mealId) AS count FROM current_order WHERE foodType = :foodType GROUP BY mealId")
    LiveData<List<CurrentOrderGrouped>> getCurrentOrderByFoodType(FoodTypes foodType);

//    @Query("SELECT * FROM current_order GROUP BY foodType")
//    LiveData<List<CurrentOrderGrouped>> getCurrentOrderGroupedByFoodType();

    @Insert
    void insertFood(Meal food);

    @Query("DELETE FROM current_order")
    void deleteAllFoods();

    @Delete
    void deleteFood(Meal food);
}
