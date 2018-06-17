package com.udacity.thefedex87.takemyorder.models;

/**
 * Created by feder on 16/06/2018.
 */

public abstract class Meal {
    String name;
    double price;

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
}
