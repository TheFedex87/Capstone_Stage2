package com.udacity.thefedex87.takemyorder.model;

/**
 * Created by feder on 07/06/2018.
 */

public class Drink {
    public enum SIZES{
        LITTLE,
        MEDIUM,
        BIG
    }

    String name;
    double price;
    SIZES size;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SIZES getSize() {
        return size;
    }

    public void setSize(SIZES size) {
        this.size = size;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
