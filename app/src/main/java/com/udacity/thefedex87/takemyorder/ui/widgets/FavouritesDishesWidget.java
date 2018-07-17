package com.udacity.thefedex87.takemyorder.ui.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;

import com.google.firebase.auth.FirebaseAuth;
import com.udacity.thefedex87.takemyorder.R;
import com.udacity.thefedex87.takemyorder.dagger.ApplicationModule;
import com.udacity.thefedex87.takemyorder.dagger.DaggerNetworkComponent;
import com.udacity.thefedex87.takemyorder.dagger.NetworkComponent;
import com.udacity.thefedex87.takemyorder.dagger.NetworkModule;
import com.udacity.thefedex87.takemyorder.room.AppDatabase;
import com.udacity.thefedex87.takemyorder.room.entity.FavouriteMeal;
import com.udacity.thefedex87.takemyorder.room.entity.Meal;
import com.udacity.thefedex87.takemyorder.ui.activities.CustomerMainActivity;
import com.udacity.thefedex87.takemyorder.ui.activities.DishDescriptionActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Implementation of App Widget functionality.
 */
public class FavouritesDishesWidget extends AppWidgetProvider {
    public static final String MEALS_LIST_KEY = "MEALS_LIST_KEY";
    public static final String USER_ID = "USER_ID";

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        NetworkComponent networkComponent = DaggerNetworkComponent.builder().applicationModule(new ApplicationModule(context)).build();

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_favourites);



        ArrayList<FavouriteMeal> meals = new ArrayList<>();
        SharedPreferences sp = context.getSharedPreferences(CustomerMainActivity.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);

        long userRoomId =  sp.getLong(CustomerMainActivity.SHARED_PREFERENCES_USER_ID, 0);
        Set<String> favouritesSet = sp.getStringSet(CustomerMainActivity.SHARED_PREFERENCES_FAVOURITES_LIST, new HashSet<String>());

        for(String favouriteSerialized : favouritesSet){
            meals.add(networkComponent.getGson().fromJson(favouriteSerialized, FavouriteMeal.class));
        }

        if (meals.size() > 0) {
            Intent intent = new Intent(context, ListViewWidgetService.class);
            Bundle b = new Bundle();
            b.putParcelableArrayList(MEALS_LIST_KEY, meals);
            b.putLong(USER_ID, userRoomId);
            intent.putExtra("BUNDLE", b);
            Random rnd = new Random();
            intent.setData(Uri.fromParts("content", String.valueOf(rnd.nextInt()), null));
            views.setRemoteAdapter(R.id.favourites_list_widget, intent);

            Intent openRecipeDetails = new Intent(context, DishDescriptionActivity.class);
            PendingIntent recipeDetailPendingIntent = PendingIntent.getActivity(context, 0, openRecipeDetails, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.favourites_list_widget, recipeDetailPendingIntent);

            views.setViewVisibility(R.id.no_favourites_widget, View.GONE);
        }else{
            views.setViewVisibility(R.id.no_favourites_widget, View.VISIBLE);
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void updateWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

