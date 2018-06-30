package com.udacity.thefedex87.takemyorder.dagger;

import com.udacity.thefedex87.takemyorder.ui.activities.CustomerMainActivity;
import com.udacity.thefedex87.takemyorder.ui.activities.LoginMapsActivity;
import com.udacity.thefedex87.takemyorder.ui.activities.RestaurantDetailsActivity;
import com.udacity.thefedex87.takemyorder.ui.adapters.FoodTypePagerAdapter;
import com.udacity.thefedex87.takemyorder.ui.fragments.DishDescriptionFragment;
import com.udacity.thefedex87.takemyorder.ui.fragments.FoodListFragment;
import com.udacity.thefedex87.takemyorder.ui.fragments.MenuCompleteFragment;
import com.udacity.thefedex87.takemyorder.ui.fragments.MenuSingleFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Federico on 09/06/2018.
 */

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(LoginMapsActivity loginMapsActivity);
    void inject(CustomerMainActivity customerMainActivity);
    void inject(RestaurantDetailsActivity restaurantInfoActivity);
    void inject(MenuSingleFragment menuSingleFragment);

    void inject(FoodListFragment foodListFragment);
    void inject(DishDescriptionFragment dishDescriptionFragment);

    void inject(MenuCompleteFragment menuCompleteFragment);
}
