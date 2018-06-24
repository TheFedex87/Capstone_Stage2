package com.udacity.thefedex87.takemyorder.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.thefedex87.takemyorder.R;
import com.udacity.thefedex87.takemyorder.room.entity.Meal;
import com.udacity.thefedex87.takemyorder.room.entity.FoodTypes;
import com.udacity.thefedex87.takemyorder.ui.adapters.FoodTypePagerAdapter;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by feder on 16/06/2018.
 */

public class MenuCompleteFragment extends Fragment {
    private FoodTypePagerAdapter foodTypePagerAdapter;

    @BindView(R.id.food_types_tabs)
    TabLayout foodTypesTabs;

    @BindView(R.id.food_types_pager)
    ViewPager foodTypesPager;

    public MenuCompleteFragment(){

    }

    public void setCurrentOrder(List<Meal> currentOrder){
        foodTypePagerAdapter.setCurrentOrder(currentOrder);
    }

    public void setMenu(HashMap<FoodTypes, List<Meal>> menu){
        LinkedHashMap<FoodTypes, List<Meal>> orderedMenu = new LinkedHashMap<>();
        if (menu.containsKey(FoodTypes.STARTER)){
            orderedMenu.put(FoodTypes.STARTER, menu.get(FoodTypes.STARTER));
            foodTypesTabs.addTab(foodTypesTabs.newTab().setText(getString(R.string.starter_dishes)));
        }
        if (menu.containsKey(FoodTypes.MAINDISH)){
            orderedMenu.put(FoodTypes.MAINDISH, menu.get(FoodTypes.MAINDISH));
            foodTypesTabs.addTab(foodTypesTabs.newTab().setText(getString(R.string.main_dishes)));
        }
        if (menu.containsKey(FoodTypes.SIDEDISH)){
            orderedMenu.put(FoodTypes.SIDEDISH, menu.get(FoodTypes.SIDEDISH));
            foodTypesTabs.addTab(foodTypesTabs.newTab().setText(getString(R.string.side_dishes)));
        }
        if (menu.containsKey(FoodTypes.DESSERT)){
            orderedMenu.put(FoodTypes.DESSERT, menu.get(FoodTypes.DESSERT));
            foodTypesTabs.addTab(foodTypesTabs.newTab().setText(getString(R.string.desserts)));
        }
        if (menu.containsKey(FoodTypes.DRINK)){
            orderedMenu.put(FoodTypes.DRINK, menu.get(FoodTypes.DRINK));
            foodTypesTabs.addTab(foodTypesTabs.newTab().setText(getString(R.string.drinks)));
        }

        foodTypePagerAdapter = new FoodTypePagerAdapter(getFragmentManager(), orderedMenu, getContext());
        foodTypesPager.setAdapter(foodTypePagerAdapter);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.menu_full_fragment, container, false);

        ButterKnife.bind(this, viewRoot);

        foodTypesTabs.setTabGravity(TabLayout.GRAVITY_FILL);

        foodTypesPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(foodTypesTabs));
        foodTypesTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                foodTypesPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return viewRoot;
    }
}
