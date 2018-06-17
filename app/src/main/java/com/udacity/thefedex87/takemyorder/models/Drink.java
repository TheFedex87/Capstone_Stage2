package com.udacity.thefedex87.takemyorder.models;

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
}
