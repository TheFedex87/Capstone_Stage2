package com.udacity.thefedex87.takemyorder.ui.widgets;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.thefedex87.takemyorder.R;
import com.udacity.thefedex87.takemyorder.dagger.ApplicationModule;
import com.udacity.thefedex87.takemyorder.dagger.DaggerViewModelComponent;
import com.udacity.thefedex87.takemyorder.dagger.ViewModelModule;
import com.udacity.thefedex87.takemyorder.models.Food;
import com.udacity.thefedex87.takemyorder.room.entity.FavouriteMeal;
import com.udacity.thefedex87.takemyorder.room.entity.FoodTypes;
import com.udacity.thefedex87.takemyorder.room.entity.Ingredient;
import com.udacity.thefedex87.takemyorder.room.entity.Meal;
import com.udacity.thefedex87.takemyorder.ui.activities.CustomerMainActivity;
import com.udacity.thefedex87.takemyorder.ui.activities.DishDescriptionActivity;
import com.udacity.thefedex87.takemyorder.ui.activities.FavouritesFoodsActivity;
import com.udacity.thefedex87.takemyorder.ui.viewmodels.FavouritesViewModel;
import com.udacity.thefedex87.takemyorder.ui.viewmodels.FavouritesViewModelFactory;

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
        long userRoomId = b.getLong(FavouritesDishesWidget.USER_ID);
        return new ListViewWidgetFactory(getApplicationContext(), meals, userRoomId);
    }
}

class ListViewWidgetFactory implements RemoteViewsService.RemoteViewsFactory {
    private List<FavouriteMeal> meals;
    private Context context;
    private long userRoomId;

    public ListViewWidgetFactory(Context context, List<FavouriteMeal> meals, long userRoomId){
        this.context = context;
        this.userRoomId = userRoomId;

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

        Intent intent = new Intent(context, DishDescriptionActivity.class);
        Bundle b = new Bundle();

        FavouriteMeal meal = meals.get(position);
        Food food = new Food();
        food.setUserId(userRoomId);
        food.setMealId(meal.getMealId());
        food.setImageName(meal.getImageName());
        food.setDescription(meal.getDescription());
        food.setName(meal.getName());
        food.setPrice(meal.getPrice());
        food.setFoodType(meal.getFoodType());
        food.setRestaurantId(meal.getRestaurantId());

        b.putParcelable(CustomerMainActivity.FOOD_DESCRIPTION_KEY, food);
        b.putString(CustomerMainActivity.RESTAURANT_ID_KEY, meals.get(position).getRestaurantId());
        b.putLong(CustomerMainActivity.USER_ID_KEY, userRoomId);
        intent.putExtras(b);

        remoteViews.setOnClickFillInIntent(R.id.dish_name, intent);

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
