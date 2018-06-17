package com.udacity.thefedex87.takemyorder.models;

/**
 * Created by feder on 16/06/2018.
 */

public abstract class Meal {
    String name;
    double price;
    String imageName;

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
}
