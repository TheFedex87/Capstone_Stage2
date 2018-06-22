package com.udacity.thefedex87.takemyorder.models;

import com.udacity.thefedex87.takemyorder.room.entity.Meal;

import java.util.List;

/**
 * Created by feder on 07/06/2018.
 */

public class Food extends Meal {

    String description;
    List<Ingredient> ingredients;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
}
