package com.udacity.thefedex87.takemyorder.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
    private FragmentManager fragmentManager;
    private Context context;

    public FoodTypePagerAdapter(FragmentManager fragmentManager, LinkedHashMap<FoodTypes, List<Meal>> meals, Context context){
        super(fragmentManager);
        this.meals = meals;
        this.fragmentManager = fragmentManager;
        this.context = context;
    }

//    @Override
//    public void destroyItem(ViewGroup container, int position, Object object) {
//        Fragment fragment = fragmentManager.findFragmentByTag("fragment:" + position);
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.remove(fragment);
//        transaction.commit();
//    }

    @Override
    public Fragment instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment)super.instantiateItem(container, position);
        List keys = new ArrayList(meals.keySet());
        ((MenuSingleFragment)fragment).setMeals(meals.get(keys.get(position)));
        //if (fragment == null) fragment = getItem(position);
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.add(container.getId(), fragment, "fragment:" + position);
//        transaction.commit();
        return fragment;
    }

    @Override
    public Fragment getItem(int position) {
        //MenuSingleFragment menuSingleFragment = new MenuSingleFragment();
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
}

