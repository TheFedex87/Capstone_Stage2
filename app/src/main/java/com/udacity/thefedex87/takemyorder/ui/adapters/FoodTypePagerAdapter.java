package com.udacity.thefedex87.takemyorder.ui.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.thefedex87.takemyorder.room.entity.Meal;
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
    private Context context;
    private MenuSingleFragment[] fragments;
    private List<Meal> currentOrder;
    private String restaurantId;

    public FoodTypePagerAdapter(FragmentManager fragmentManager, LinkedHashMap<FoodTypes, List<Meal>> meals, String restaurantId, Context context){
        super(fragmentManager);
        this.meals = meals;
        this.restaurantId = restaurantId;
        this.context = context;
        fragments = new MenuSingleFragment[meals.size()];
    }

    //Set the current order to the adatper, which will setup the current order to every fragmnent it contains
    public void setCurrentOrder(List<Meal> currentOrder){
        this.currentOrder = currentOrder;
        for(MenuSingleFragment menuSingleFragment : fragments){
            setCurrentOrderToFragment(currentOrder, menuSingleFragment);
        }
    }

    @Override
    public Fragment instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment)super.instantiateItem(container, position);
        fragments[position] = (MenuSingleFragment)fragment;
        List keys = new ArrayList(meals.keySet());
        //Set the meals for the current tab (food type)
        ((MenuSingleFragment)fragment).setMeals(meals.get(keys.get(position)));
        ((MenuSingleFragment)fragment).setRestaurantId(restaurantId);
        setCurrentOrderToFragment(currentOrder, (MenuSingleFragment)fragment);
        return fragment;
    }

    @Override
    public Fragment getItem(int position) {
        //Set the fragment to every page of the adapter
        MenuSingleFragment menuSingleFragment = (MenuSingleFragment)Fragment.instantiate(context, MenuSingleFragment.class.getName());
        return menuSingleFragment;
    }

    @Override
    public int getCount() {
        return meals.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object fragment) {
        return ((Fragment) fragment).getView() == view;
    }

    private void setCurrentOrderToFragment(List<Meal> currentOrder, MenuSingleFragment fragment){
        if (fragment != null && currentOrder != null)
            fragment.setCurrentOrder(currentOrder);
    }
}

