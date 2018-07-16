package com.udacity.thefedex87.takemyorder.room.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.udacity.thefedex87.takemyorder.room.entity.FavouriteMeal;
import com.udacity.thefedex87.takemyorder.room.entity.FavouriteMealIngredientJoin;
import com.udacity.thefedex87.takemyorder.room.entity.FavouriteMealUserJoin;
import com.udacity.thefedex87.takemyorder.room.entity.Ingredient;

import java.util.List;

import dagger.internal.GwtIncompatible;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;

/**
 * Created by federico.creti on 28/06/2018.
 */

@Dao
public interface FavouriteMealsDao {
    @Query("SELECT * FROM favourite_meals JOIN favouritemeal_user_join ON favourite_meals.id = favouritemeal_user_join.favouriteMealId " +
            "WHERE favouritemeal_user_join.userId = :userRoomId GROUP BY name")
    LiveData<List<FavouriteMeal>> getAllFavouriteMealsOfUser(long userRoomId);

    //Get the list of user's favourites meals for specific restaurant
    @Query("SELECT * FROM favourite_meals JOIN favouritemeal_user_join ON favourite_meals.id = favouritemeal_user_join.favouriteMealId " +
            "WHERE favouritemeal_user_join.userId = :userRoomId AND favourite_meals.restaurantId = :restaurantId")
    LiveData<List<FavouriteMeal>> getFavouriteMealsOfUser(long userRoomId, String restaurantId);

    //Get a user favourite meal by it's id. This is used to check if a specific food is a favourite for the logged user
    @Query("SELECT * FROM favourite_meals JOIN favouritemeal_user_join ON favourite_meals.id = favouritemeal_user_join.favouriteMealId " +
            "WHERE favourite_meals.mealId = :mealId AND favouritemeal_user_join.userId = :userRoomId AND favourite_meals.restaurantId = :restaurantId")
    LiveData<FavouriteMeal> getUserFavouriteMealById(String mealId, long userRoomId, String restaurantId);

    //Get the food with specific foodId from the favourite list, this is used to see if a food is already inside the table of
    //favourites meals
    @Query("SELECT * FROM favourite_meals WHERE mealId = :mealId AND favourite_meals.restaurantId = :restaurantId")
    LiveData<FavouriteMeal> getFavouriteMealById(String mealId, String restaurantId);

    //Get an ingredient by its name
    @Query("SELECT * FROM ingredient WHERE ingredientName = :ingredientName")
    LiveData<Ingredient> getIngredientByName(String ingredientName);

    //Get the ingredient list of a specific meal
    @Query("SELECT ingredient.ingredientName FROM ingredient JOIN favouritemeal_ingredient_join ON ingredientName = ingredientId " +
            "JOIN favourite_meals ON favourite_meals.mealId = favouritemeal_ingredient_join.mealId " +
            "WHERE favourite_meals.mealId = :favouriteMealId")
    LiveData<List<Ingredient>> ingredientsOfMeal(String favouriteMealId);


    @Query("DELETE from favouritemeal_user_join WHERE userId = :userId AND favouriteMealId = :favouriteMealId")
    int deleteFavouriteMealUserJoin(long userId, long favouriteMealId);

    @Insert
    long insertFavouriteMeal(FavouriteMeal food);

    @Insert(onConflict = IGNORE)
    void insertIngredient(Ingredient ingredient);

    @Insert(onConflict = IGNORE)
    void insertMealIngredient(FavouriteMealIngredientJoin favouriteMealIngredientJoin);

    @Insert(onConflict = IGNORE)
    void insertMealUser(FavouriteMealUserJoin favouriteMealUserJoin);

    @Delete
    void deleteFavouriteMeal(FavouriteMeal food);
}
