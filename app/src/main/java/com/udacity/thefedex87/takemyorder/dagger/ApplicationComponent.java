package com.udacity.thefedex87.takemyorder.dagger;

import com.udacity.thefedex87.takemyorder.ui.activities.LoginMapsActivity;
import com.udacity.thefedex87.takemyorder.ui.activities.RestaurantInfoActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Federico on 09/06/2018.
 */

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(LoginMapsActivity loginMapsActivity);
    void inject(RestaurantInfoActivity restaurantInfoActivity);
}
