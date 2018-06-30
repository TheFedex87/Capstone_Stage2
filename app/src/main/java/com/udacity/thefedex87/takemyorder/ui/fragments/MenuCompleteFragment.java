package com.udacity.thefedex87.takemyorder.ui.fragments;

import android.content.Context;
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
import com.udacity.thefedex87.takemyorder.application.TakeMyOrderApplication;
import com.udacity.thefedex87.takemyorder.dagger.ApplicationModule;
import com.udacity.thefedex87.takemyorder.dagger.DaggerUserInterfaceComponent;
import com.udacity.thefedex87.takemyorder.dagger.UserInterfaceComponent;
import com.udacity.thefedex87.takemyorder.dagger.UserInterfaceModule;
import com.udacity.thefedex87.takemyorder.room.entity.Meal;
import com.udacity.thefedex87.takemyorder.room.entity.FoodTypes;
import com.udacity.thefedex87.takemyorder.ui.adapters.FoodTypePagerAdapter;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.inject.Inject;

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

    @Inject
    Context context;

    private UserInterfaceComponent userInterfaceComponent;

    public MenuCompleteFragment(){

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        TakeMyOrderApplication.appComponent().inject(this);

    }

    //Set the current order to the the pageradapter if it has been created
    public void setCurrentOrder(List<Meal> currentOrder){
        if (foodTypePagerAdapter != null)
            foodTypePagerAdapter.setCurrentOrder(currentOrder);
    }


    public void setMenu(HashMap<FoodTypes, List<Meal>> menu, String restaurantId){
        int selectedTab = foodTypesTabs.getSelectedTabPosition();
        foodTypesTabs.removeAllTabs();

        //Create the menu of the restaurant (or the favourite view if we are showing the user's favourite
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

        userInterfaceComponent = DaggerUserInterfaceComponent.builder()
                .applicationModule(new ApplicationModule(context))
                .userInterfaceModule(new UserInterfaceModule(getFragmentManager(), orderedMenu, restaurantId))
                .build();

        foodTypePagerAdapter = userInterfaceComponent.getFoodTypePagerAdapter(); //new FoodTypePagerAdapter(getFragmentManager(), orderedMenu, restaurantId, context);
        foodTypesPager.setAdapter(foodTypePagerAdapter);

        if (selectedTab >= 0)
            foodTypesTabs.getTabAt(selectedTab).select();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.menu_full_fragment, container, false);

        ButterKnife.bind(this, viewRoot);

        //Setup the TabLayout
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
