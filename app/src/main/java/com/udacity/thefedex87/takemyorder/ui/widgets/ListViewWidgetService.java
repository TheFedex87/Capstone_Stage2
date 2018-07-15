package com.udacity.thefedex87.takemyorder.ui.widgets;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.thefedex87.takemyorder.R;
import com.udacity.thefedex87.takemyorder.room.entity.Meal;

import java.util.List;

/**
 * Created by feder on 15/07/2018.
 */

public class ListViewWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        List<Meal> meals = intent.getParcelableArrayListExtra(FavouritesDishesWidget.MEALS_LIST_KEY);
        return new ListViewWidgetFactory(getApplicationContext(), meals);
    }
}

class ListViewWidgetFactory implements RemoteViewsService.RemoteViewsFactory {
    private List<Meal> meals;
    private Context context;

    public ListViewWidgetFactory(Context context, List<Meal> meals){
        this.context = context;
        this.meals = meals;
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

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
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
