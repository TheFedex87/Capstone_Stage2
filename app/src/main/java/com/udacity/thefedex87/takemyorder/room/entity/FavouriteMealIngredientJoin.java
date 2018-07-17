package com.udacity.thefedex87.takemyorder.room.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by federico.creti on 29/06/2018.
 */
//Tables which joins the many-to-many relation beetween favourite meal and ingredients
@Entity(tableName = "favouritemeal_ingredient_join",
    primaryKeys = { "ingredientId", "mealId"},
    foreignKeys = {
        @ForeignKey(entity = FavouriteMeal.class,
                    parentColumns = "mealId",
                    childColumns = "mealId",
                    onDelete = CASCADE),
        @ForeignKey(entity = Ingredient.class,
                    parentColumns = "ingredientName",
                    childColumns = "ingredientId",
                    onDelete = CASCADE)
    })
public class FavouriteMealIngredientJoin {
    public @NonNull String ingredientId;
    public @NonNull String mealId;

    public FavouriteMealIngredientJoin(String ingredientId, String mealId) {
        this.ingredientId = ingredientId;
        this.mealId = mealId;
    }
}
