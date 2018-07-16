package com.udacity.thefedex87.takemyorder.ui.widgets;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.thefedex87.takemyorder.R;
import com.udacity.thefedex87.takemyorder.room.entity.FavouriteMeal;
import com.udacity.thefedex87.takemyorder.room.entity.FoodTypes;
import com.udacity.thefedex87.takemyorder.room.entity.Meal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by feder on 15/07/2018.
 */

public class ListViewWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Bundle b = intent.getBundleExtra("BUNDLE");
        List<FavouriteMeal> meals = b.getParcelableArrayList(FavouritesDishesWidget.MEALS_LIST_KEY);
        return new ListViewWidgetFactory(getApplicationContext(), meals);
    }
}

class ListViewWidgetFactory implements RemoteViewsService.RemoteViewsFactory {
    private List<FavouriteMeal> meals;
    private Context context;

    public ListViewWidgetFactory(Context context, List<FavouriteMeal> meals){
        this.context = context;

        List<FavouriteMeal> starter = new ArrayList<>();
        List<FavouriteMeal> main = new ArrayList<>();
        List<FavouriteMeal> side = new ArrayList<>();
        List<FavouriteMeal> dessert = new ArrayList<>();

        for(FavouriteMeal favouriteMeal : meals){
            if(favouriteMeal.getFoodType() == FoodTypes.STARTER)
                starter.add(favouriteMeal);
            else if(favouriteMeal.getFoodType() == FoodTypes.MAINDISH)
                main.add(favouriteMeal);
            else if(favouriteMeal.getFoodType() == FoodTypes.SIDEDISH)
                side.add(favouriteMeal);
            else if(favouriteMeal.getFoodType() == FoodTypes.DESSERT)
                dessert.add(favouriteMeal);
        }

        List<FavouriteMeal> sortedMeals = new ArrayList<>();

        for(FavouriteMeal favouriteMeal : starter)
            sortedMeals.add(favouriteMeal);

        for(FavouriteMeal favouriteMeal : main)
            sortedMeals.add(favouriteMeal);

        for(FavouriteMeal favouriteMeal : side)
            sortedMeals.add(favouriteMeal);

        for(FavouriteMeal favouriteMeal : dessert)
            sortedMeals.add(favouriteMeal);

        this.meals = sortedMeals;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (meals == null) return 0;
        return meals.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.single_line_dish_widget);

        remoteViews.setTextViewText(R.id.dish_name, meals.get(position).getName());

        if(position == 0){
            remoteViews.setViewVisibility(R.id.dish_category, View.VISIBLE);
            remoteViews.setTextViewText(R.id.dish_category, "STARTER");
        } else if(meals.get(position).getFoodType() != meals.get(position - 1).getFoodType()) {
            remoteViews.setViewVisibility(R.id.dish_category, View.VISIBLE);
            remoteViews.setTextViewText(R.id.dish_category, meals.get(position).getFoodType().toString());
        } else {
            remoteViews.setViewVisibility(R.id.dish_category, View.GONE);
        }

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
