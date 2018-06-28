package com.udacity.thefedex87.takemyorder.room.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.udacity.thefedex87.takemyorder.room.entity.CurrentOrderGrouped;
import com.udacity.thefedex87.takemyorder.room.entity.FavouriteMeal;
import com.udacity.thefedex87.takemyorder.room.entity.Meal;

import java.util.List;

/**
 * Created by federico.creti on 28/06/2018.
 */

@Dao
public interface FavouriteMealsDao {
    @Query("SELECT * FROM favourite_meals")
    LiveData<List<FavouriteMeal>> getFavouriteMeals();

    @Query("SELECT * FROM favourite_meals WHERE mealId = :mealId AND restaurantId = :restaurantId")
    LiveData<FavouriteMeal> getFavouriteMealById(String mealId, String restaurantId);

    @Insert
    void insertFavouriteMeal(FavouriteMeal food);

    @Delete
    void deleteFavouriteMeal(FavouriteMeal food);
}
