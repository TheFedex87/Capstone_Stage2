package com.udacity.thefedex87.takemyorder.room.converter;

import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;

import com.udacity.thefedex87.takemyorder.room.entity.FoodTypes;

/**
 * Created by federico.creti on 14/06/2018.
 */
//This class converts a FoodTypes into String and a String into a FoodTypes
public class FoodTypeConverter {
    @TypeConverter
    public static String toString(FoodTypes foodType){
        return foodType.toString();
    }

    @TypeConverter
    public static FoodTypes toFoodType(String stringFoodType){
        return FoodTypes.valueOf(stringFoodType);
    }
}
