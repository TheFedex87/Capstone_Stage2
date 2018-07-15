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
    //Select the current order for the logged user
    @Query("SELECT * FROM current_order WHERE userId = :userRoomId AND restaurantId = :restaurantId")
    LiveData<List<Meal>> getCurrentOrderList(long userRoomId, String restaurantId);

    //Get the list of the order with the same MealId, it used to count the number of meal of the same tyoe
    @Query("SELECT * FROM current_order WHERE mealId = :mealId AND userId = :userRoomId AND restaurantId = :restaurantId")
    LiveData<List<Meal>> getCurrentOrderListByMealId(String mealId, long userRoomId, String restaurantId);

    //Get the list of meal inside current order, by foodType (starter, main dishes,...) and grouped by meal id in order to count
    //the number of meal with the same type
    @Query("SELECT *, COUNT(mealId) AS count FROM current_order WHERE foodType = :foodType AND userId = :userRoomId AND restaurantId = :restaurantId GROUP BY mealId")
    LiveData<List<CurrentOrderGrouped>> getCurrentOrderByFoodType(FoodTypes foodType, long userRoomId, String restaurantId);

    @Insert
    void insertFood(Meal food);

    //Delete all foods inside current order for the logged user
    @Query("DELETE FROM current_order WHERE userId = :userId")
    void deleteAllFoods(long userId);

    @Delete
    void deleteFood(Meal food);
}
