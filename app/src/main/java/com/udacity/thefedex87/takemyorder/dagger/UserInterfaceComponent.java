package com.udacity.thefedex87.takemyorder.dagger;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;

import com.udacity.thefedex87.takemyorder.ui.adapters.FoodInMenuAdapter;
import com.udacity.thefedex87.takemyorder.ui.adapters.PhotoIndicatorContainerAdapter;
import com.udacity.thefedex87.takemyorder.ui.adapters.RestaurantPhotoAdapter;
import com.udacity.thefedex87.takemyorder.ui.adapters.RestaurantReviewsAdapter;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by feder on 12/06/2018.
 */

@Singleton
@Component(modules = {UserInterfaceModule.class, ApplicationModule.class})
public interface UserInterfaceComponent {
    PhotoIndicatorContainerAdapter getPhotoIndicatorContainerAdapter();
    RestaurantPhotoAdapter getRestaurantPhotoAdapter();
    RestaurantReviewsAdapter getRestaurantReviewsAdapter();
    FoodInMenuAdapter getFoodInMenuAdapter();

    LinearLayoutManager getLinearLayoutManager();
    GridLayoutManager getGridLayoutManager();
}
