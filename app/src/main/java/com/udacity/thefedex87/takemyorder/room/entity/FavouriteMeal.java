package com.udacity.thefedex87.takemyorder.room.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by federico.creti on 28/06/2018.
 */

@Entity(tableName = "favourite_meals", indices = {@Index(value = {"mealId"},
        unique = true)})
public class FavouriteMeal implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private long id;

    private String name;
    private double price;
    private String imageName;
    private String mealId;
    private FoodTypes foodType;
    private String restaurantId;
    private String description;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getMealId() {
        return mealId;
    }

    public void setMealId(String mealId) {
        this.mealId = mealId;
    }

    public FoodTypes getFoodType() {
        return foodType;
    }

    public void setFoodType(FoodTypes foodType) {
        this.foodType = foodType;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeDouble(this.price);
        dest.writeString(this.imageName);
        dest.writeString(this.mealId);
        dest.writeInt(this.foodType == null ? -1 : this.foodType.ordinal());
        dest.writeString(this.restaurantId);
        dest.writeString(this.description);
    }

    public FavouriteMeal() {
    }

    protected FavouriteMeal(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.price = in.readDouble();
        this.imageName = in.readString();
        this.mealId = in.readString();
        int tmpFoodType = in.readInt();
        this.foodType = tmpFoodType == -1 ? null : FoodTypes.values()[tmpFoodType];
        this.restaurantId = in.readString();
        this.description = in.readString();
    }

    public static final Parcelable.Creator<FavouriteMeal> CREATOR = new Parcelable.Creator<FavouriteMeal>() {
        @Override
        public FavouriteMeal createFromParcel(Parcel source) {
            return new FavouriteMeal(source);
        }

        @Override
        public FavouriteMeal[] newArray(int size) {
            return new FavouriteMeal[size];
        }
    };
}
