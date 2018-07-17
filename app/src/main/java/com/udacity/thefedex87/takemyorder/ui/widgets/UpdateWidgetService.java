package com.udacity.thefedex87.takemyorder.ui.widgets;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by federico.creti on 17/07/2018.
 */

public class UpdateWidgetService extends IntentService {
    public static final String UPDATE_WIDGET_ACTION = "UPDATE_WIDGET_ACTION";

    public UpdateWidgetService() {
        super(UpdateWidgetService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String action = intent.getAction();
        switch (action){
            case UPDATE_WIDGET_ACTION:
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, FavouritesDishesWidget.class));
                FavouritesDishesWidget.updateWidgets(getApplicationContext(), appWidgetManager, appWidgetIds);
                break;
        }
    }
}
