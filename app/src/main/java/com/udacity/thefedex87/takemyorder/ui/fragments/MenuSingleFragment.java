package com.udacity.thefedex87.takemyorder.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.transition.Scene;
import android.support.transition.TransitionManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.thefedex87.takemyorder.R;
import com.udacity.thefedex87.takemyorder.application.TakeMyOrderApplication;
import com.udacity.thefedex87.takemyorder.dagger.ApplicationModule;
import com.udacity.thefedex87.takemyorder.dagger.DaggerUserInterfaceComponent;
import com.udacity.thefedex87.takemyorder.dagger.UserInterfaceComponent;
import com.udacity.thefedex87.takemyorder.dagger.UserInterfaceModule;
import com.udacity.thefedex87.takemyorder.models.Meal;
import com.udacity.thefedex87.takemyorder.ui.adapters.FoodInMenuAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by feder on 16/06/2018.
 */

public class MenuSingleFragment extends Fragment implements FoodInMenuAdapter.FoodInMenuActionClick {
    private List<Meal> meals;

    @BindView(R.id.foods_in_menu_container)
    RecyclerView foodInMenuContainer;

    private FoodInMenuAdapter foodInMenuAdapter;

    @Inject
    Context applicationContext;

    private ViewGroup container;

    public MenuSingleFragment(){

    }

    public void setMeals(List<Meal> meals){
        this.meals = meals;
        if (foodInMenuAdapter != null)
            foodInMenuAdapter.setMeals(meals);
    }

    @Override
    public void onAttach(Context context) {
        TakeMyOrderApplication.appComponent().inject(this);
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.menu_single_fragment, container, false);

        ButterKnife.bind(this, viewRoot);

        this.container = container;

        UserInterfaceComponent userInterfaceComponent = DaggerUserInterfaceComponent.builder()
                .applicationModule(new ApplicationModule(applicationContext))
                .userInterfaceModule(
                        new UserInterfaceModule(LinearLayoutManager.VERTICAL, this))
                .build();

        foodInMenuAdapter = new FoodInMenuAdapter(applicationContext, this);//userInterfaceComponent.getFoodInMenuAdapter();
        foodInMenuContainer.setAdapter(foodInMenuAdapter);
        foodInMenuContainer.setLayoutManager(userInterfaceComponent.getGridLayoutManager());
        foodInMenuAdapter.setMeals(meals);

        return viewRoot;
    }

    @Override
    public void addOrderClick() {
        //TransitionManager.go(Scene.getSceneForLayout((ViewGroup)getActivity().findViewById(R.id.food_in_menu_container), R.layout.food_in_menu_scene_2, getActivity()));
    }
}
