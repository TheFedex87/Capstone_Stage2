package com.udacity.thefedex87.takemyorder.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import com.udacity.thefedex87.takemyorder.models.Meal;
import com.udacity.thefedex87.takemyorder.room.entity.FoodTypes;
import com.udacity.thefedex87.takemyorder.ui.fragments.MenuSingleFragment;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by feder on 16/06/2018.
 */

public class FoodTypePagerAdapter extends FragmentPagerAdapter {
    private LinkedHashMap<FoodTypes, List<Meal>> meals;

    public FoodTypePagerAdapter(FragmentManager fragmentManager, LinkedHashMap<FoodTypes, List<Meal>> meals){
        super(fragmentManager);
        this.meals = meals;
    }

    @Override
    public Fragment getItem(int position) {
        MenuSingleFragment menuSingleFragment = new MenuSingleFragment();
        List keys = new ArrayList(meals.keySet());
        menuSingleFragment.setMeals(meals.get(keys.get(position)));
        return menuSingleFragment;
    }

    @Override
    public int getCount() {
        return meals.size();
    }
}

