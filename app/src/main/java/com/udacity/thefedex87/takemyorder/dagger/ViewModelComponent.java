package com.udacity.thefedex87.takemyorder.dagger;

import com.udacity.thefedex87.takemyorder.ui.viewmodels.CheckoutOrderViewModelFactory;
import com.udacity.thefedex87.takemyorder.ui.viewmodels.CustomerMainViewModelFactory;
import com.udacity.thefedex87.takemyorder.ui.viewmodels.DishDetailsViewModelFactory;
import com.udacity.thefedex87.takemyorder.ui.viewmodels.FavouritesViewModelFactory;
import com.udacity.thefedex87.takemyorder.ui.viewmodels.RestaurantMenuViewModelFactory;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by feder on 07/07/2018.
 */
@Singleton
@Component(modules = { ApplicationModule.class, ViewModelModule.class })
public interface ViewModelComponent {
    CheckoutOrderViewModelFactory getCheckoutOrderViewModelFactory();
    CustomerMainViewModelFactory getCustomerMainViewModelFactory();
    DishDetailsViewModelFactory getDishDetailsViewModelFactory();
    FavouritesViewModelFactory getFavouritesViewModelFactory();
    RestaurantMenuViewModelFactory getRestaurantMenuViewModelFactory();
}
