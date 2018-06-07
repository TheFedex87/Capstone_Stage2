package com.udacity.thefedex87.takemyorder.model;

/**
 * Created by federico.creti on 07/06/2018.
 */

public class Restaurant {
    private String name;
    private double lat;
    private double lng;


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
}
