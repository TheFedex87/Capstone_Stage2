package com.udacity.thefedex87.takemyorder.models;

import android.os.Parcel;

import com.udacity.thefedex87.takemyorder.room.entity.Meal;

import java.util.ArrayList;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.description);
        dest.writeList(this.ingredients);
    }

    public Food() {
    }

    protected Food(Parcel in) {
        super(in);
        this.description = in.readString();
        this.ingredients = new ArrayList<Ingredient>();
        in.readList(this.ingredients, Ingredient.class.getClassLoader());
    }

    public static final Creator<Food> CREATOR = new Creator<Food>() {
        @Override
        public Food createFromParcel(Parcel source) {
            return new Food(source);
        }

        @Override
        public Food[] newArray(int size) {
            return new Food[size];
        }
    };
}
