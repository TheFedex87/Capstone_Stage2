package com.udacity.thefedex87.takemyorder.models;

import android.os.Parcel;

import com.udacity.thefedex87.takemyorder.room.entity.Meal;

/**
 * Created by feder on 07/06/2018.
 */

//This class is used to instanciate a Drink Meal
public class Drink extends Meal {

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    public Drink() {
    }

    protected Drink(Parcel in) {
        super(in);
    }

    public static final Creator<Drink> CREATOR = new Creator<Drink>() {
        @Override
        public Drink createFromParcel(Parcel source) {
            return new Drink(source);
        }

        @Override
        public Drink[] newArray(int size) {
            return new Drink[size];
        }
    };
}
