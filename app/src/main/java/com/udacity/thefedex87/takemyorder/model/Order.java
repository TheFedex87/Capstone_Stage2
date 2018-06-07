package com.udacity.thefedex87.takemyorder.model;

import java.util.Date;
import java.util.List;

/**
 * Created by feder on 07/06/2018.
 */

public class Order {
    String userId;
    int tableId;
    List<Food> foods;
    List<Drink> drinks;
    Date orderTime;
}
