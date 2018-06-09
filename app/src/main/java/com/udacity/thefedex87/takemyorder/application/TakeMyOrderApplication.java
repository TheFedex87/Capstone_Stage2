package com.udacity.thefedex87.takemyorder.application;

import android.app.Application;

import com.udacity.thefedex87.takemyorder.dagger.ApplicationComponent;
import com.udacity.thefedex87.takemyorder.dagger.ApplicationModule;
import com.udacity.thefedex87.takemyorder.dagger.DaggerApplicationComponent;

/**
 * Created by Federico on 09/06/2018.
 */

public class TakeMyOrderApplication extends Application {
    private static ApplicationComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this.getApplicationContext())).build();
    }

    public static ApplicationComponent appComponent(){
        return appComponent;
    }
}
