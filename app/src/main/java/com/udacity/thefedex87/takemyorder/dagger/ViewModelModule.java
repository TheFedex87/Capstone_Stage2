package com.udacity.thefedex87.takemyorder.dagger;

import android.content.Context;

import com.udacity.thefedex87.takemyorder.room.AppDatabase;
import com.udacity.thefedex87.takemyorder.ui.viewmodels.CheckoutOrderViewModelFactory;
import com.udacity.thefedex87.takemyorder.ui.viewmodels.CustomerMainViewModelFactory;
import com.udacity.thefedex87.takemyorder.ui.viewmodels.DishDetailsViewModelFactory;
import com.udacity.thefedex87.takemyorder.ui.viewmodels.FavouritesViewModelFactory;
import com.udacity.thefedex87.takemyorder.ui.viewmodels.RestaurantMenuViewModelFactory;

import dagger.Module;
import dagger.Provides;

/**
 * Created by feder on 07/07/2018.
 */

@Module
public class ViewModelModule {
    private long userRoomId;
    private String restaurdantId;
    private String userName;
    private String mealId;

    public ViewModelModule(long userRoomId){
        this.userRoomId = userRoomId;
    }

    public ViewModelModule(String restaurantId, String userName, long userRoomId){
        this(userRoomId);
        this.restaurdantId = restaurantId;
        this.userName = userName;
    }

    public ViewModelModule(String mealId, long userRoomId){
        this(userRoomId);
        this.mealId = mealId;
    }

    @Provides
    public CheckoutOrderViewModelFactory provideCheckoutOrderViewModelFactory (Context context){
        return new CheckoutOrderViewModelFactory(AppDatabase.getInstance(context), userRoomId);
    }

    @Provides
    public CustomerMainViewModelFactory provideCustomerMainViewModelFactory(Context context){
        return new CustomerMainViewModelFactory(AppDatabase.getInstance(context), restaurdantId, userName, userRoomId);
    }

    @Provides
    public DishDetailsViewModelFactory provideDishDetailsViewModelFactory(Context context){
        return new DishDetailsViewModelFactory(AppDatabase.getInstance(context), mealId, userRoomId);
    }

    @Provides
    public FavouritesViewModelFactory provideFavouritesViewModelFactory(Context context){
        return new FavouritesViewModelFactory(AppDatabase.getInstance(context), restaurdantId, userRoomId);
    }

    @Provides
    public RestaurantMenuViewModelFactory provideRestaurantMenuViewModelFactory(Context context){
        return new RestaurantMenuViewModelFactory(AppDatabase.getInstance(context), restaurdantId, userRoomId);
    }
}
