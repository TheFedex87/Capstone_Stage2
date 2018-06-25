package com.udacity.thefedex87.takemyorder.room.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.udacity.thefedex87.takemyorder.room.entity.FoodTypes;

/**
 * Created by feder on 16/06/2018.
 */

@Entity(tableName = "current_order")
public class Meal implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private long id;

    private String name;
    private double price;
    private String imageName;
    private String mealId;
    private FoodTypes foodType;

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

    public static final Creator<Meal> CREATOR = new Creator<Meal>() {
        @Override
        public Meal createFromParcel(Parcel in) {
            return new Meal(in);
        }

        @Override
        public Meal[] newArray(int size) {
            return new Meal[size];
        }
    };

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
    }

    public Meal() {
    }

    protected Meal(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.price = in.readDouble();
        this.imageName = in.readString();
        this.mealId = in.readString();
        int tmpFoodType = in.readInt();
        this.foodType = tmpFoodType == -1 ? null : FoodTypes.values()[tmpFoodType];
    }

}
