package com.udacity.thefedex87.takemyorder.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.udacity.thefedex87.takemyorder.room.dao.FavouriteMealsDao;
import com.udacity.thefedex87.takemyorder.room.dao.UserDao;
import com.udacity.thefedex87.takemyorder.room.entity.FavouriteMeal;
import com.udacity.thefedex87.takemyorder.room.entity.FavouriteMealIngredientJoin;
import com.udacity.thefedex87.takemyorder.room.entity.FavouriteMealUserJoin;
import com.udacity.thefedex87.takemyorder.room.entity.Ingredient;
import com.udacity.thefedex87.takemyorder.room.entity.Meal;
import com.udacity.thefedex87.takemyorder.room.converter.FoodTypeConverter;
import com.udacity.thefedex87.takemyorder.room.dao.CurrentOrderDao;
import com.udacity.thefedex87.takemyorder.room.entity.User;

import timber.log.Timber;

/**
 * Created by federico.creti on 14/06/2018.
 */

@Database(entities = {Meal.class, FavouriteMeal.class, Ingredient.class, FavouriteMealIngredientJoin.class, User.class, FavouriteMealUserJoin.class}, version = 3, exportSchema = false)
@TypeConverters(FoodTypeConverter.class)
public abstract class AppDatabase extends RoomDatabase {
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "takemyorder";
    private static AppDatabase instance;

    public static AppDatabase getInstance(Context context){
        if(instance == null){
            synchronized (LOCK) {
                Timber.d("Creating new DB instance");
                instance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class,
                        AppDatabase.DATABASE_NAME)
                        .build();
            }
        }
        return instance;
    }

    public abstract CurrentOrderDao currentOrderDao();
    public abstract FavouriteMealsDao favouriteMealsDao();
    public abstract UserDao userDao();
}
