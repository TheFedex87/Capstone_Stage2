package com.udacity.thefedex87.takemyorder.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by federico.creti on 07/06/2018.
 */
//This class is a model for Restaurant entity
public class Restaurant implements Parcelable {
    private String name;
    private double lat;
    private double lng;
    private String placeId;


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeDouble(this.lat);
        dest.writeDouble(this.lng);
        dest.writeString(this.placeId);
    }

    public Restaurant() {
    }

    protected Restaurant(Parcel in) {
        this.name = in.readString();
        this.lat = in.readDouble();
        this.lng = in.readDouble();
        this.placeId = in.readString();
    }

    public static final Parcelable.Creator<Restaurant> CREATOR = new Parcelable.Creator<Restaurant>() {
        @Override
        public Restaurant createFromParcel(Parcel source) {
            return new Restaurant(source);
        }

        @Override
        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };
}
