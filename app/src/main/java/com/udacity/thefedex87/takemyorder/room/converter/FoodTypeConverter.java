package com.udacity.thefedex87.takemyorder.room.converter;

import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;

import com.udacity.thefedex87.takemyorder.room.entity.CurrentOrderEntry;

/**
 * Created by federico.creti on 14/06/2018.
 */

public class FoodTypeConverter {
    @TypeConverter
    public static String toString(CurrentOrderEntry.FoodTypes foodType){
        return foodType.toString();
    }

    @TypeConverter
    public static CurrentOrderEntry.FoodTypes toFoodType(String stringFoodType){
        return CurrentOrderEntry.FoodTypes.valueOf(stringFoodType);
    }
}
