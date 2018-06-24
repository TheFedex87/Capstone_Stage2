package com.udacity.thefedex87.takemyorder.models;

import android.os.Parcel;

import com.udacity.thefedex87.takemyorder.room.entity.Meal;

/**
 * Created by feder on 07/06/2018.
 */

public class Drink extends Meal {
    public enum SIZES{
        LITTLE,
        MEDIUM,
        BIG
    }

    SIZES size;

    public SIZES getSize() {
        return size;
    }

    public void setSize(SIZES size) {
        this.size = size;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.size == null ? -1 : this.size.ordinal());
    }

    public Drink() {
    }

    protected Drink(Parcel in) {
        super(in);
        int tmpSize = in.readInt();
        this.size = tmpSize == -1 ? null : SIZES.values()[tmpSize];
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
