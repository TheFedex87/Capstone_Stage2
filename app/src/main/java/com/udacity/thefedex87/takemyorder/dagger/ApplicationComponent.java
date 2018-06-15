package com.udacity.thefedex87.takemyorder.dagger;

import com.udacity.thefedex87.takemyorder.ui.activities.LoginMapsActivity;
import com.udacity.thefedex87.takemyorder.ui.activities.RestaurantDetailsActivity;
import com.udacity.thefedex87.takemyorder.ui.fragments.FoodListFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Federico on 09/06/2018.
 */

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(LoginMapsActivity loginMapsActivity);
    void inject(RestaurantDetailsActivity restaurantInfoActivity);

    void inject(FoodListFragment foodListFragment);
}
