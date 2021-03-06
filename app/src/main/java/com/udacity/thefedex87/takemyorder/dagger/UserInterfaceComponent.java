package com.udacity.thefedex87.takemyorder.dagger;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;

import com.udacity.thefedex87.takemyorder.ui.adapters.CheckoutOrderAdapter;
import com.udacity.thefedex87.takemyorder.ui.adapters.DishIngredientsAdapter;
import com.udacity.thefedex87.takemyorder.ui.adapters.FoodInMenuAdapter;
import com.udacity.thefedex87.takemyorder.ui.adapters.FoodTypePagerAdapter;
import com.udacity.thefedex87.takemyorder.ui.adapters.PhotoIndicatorContainerAdapter;
import com.udacity.thefedex87.takemyorder.ui.adapters.RestaurantPhotoAdapter;
import com.udacity.thefedex87.takemyorder.ui.adapters.RestaurantReviewsAdapter;
import com.udacity.thefedex87.takemyorder.ui.adapters.WaiterCallsAdapter;
import com.udacity.thefedex87.takemyorder.ui.adapters.WaiterPagerAdapter;
import com.udacity.thefedex87.takemyorder.ui.adapters.WaiterReadyOrdersAdapter;

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
    DishIngredientsAdapter getDishIngredientsAdapter();
    CheckoutOrderAdapter getCheckoutOrderAdapter();

    FoodTypePagerAdapter getFoodTypePagerAdapter();

    LinearLayoutManager getLinearLayoutManager();
    GridLayoutManager getGridLayoutManager();

    WaiterPagerAdapter getWaiterPagerAdapter();
    WaiterCallsAdapter getWaiterCallsAdapter();
    WaiterReadyOrdersAdapter getWaiterReadyOrdersAdapter();
}
