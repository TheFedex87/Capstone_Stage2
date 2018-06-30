package com.udacity.thefedex87.takemyorder.dagger;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;

import com.udacity.thefedex87.takemyorder.R;
import com.udacity.thefedex87.takemyorder.models.GooglePlaceDetailModel.RestaurantReviewModel;
import com.udacity.thefedex87.takemyorder.room.entity.FoodTypes;
import com.udacity.thefedex87.takemyorder.room.entity.Ingredient;
import com.udacity.thefedex87.takemyorder.room.entity.Meal;
import com.udacity.thefedex87.takemyorder.ui.adapters.DishIngredientsAdapter;
import com.udacity.thefedex87.takemyorder.ui.adapters.FoodInMenuAdapter;
import com.udacity.thefedex87.takemyorder.ui.adapters.FoodTypePagerAdapter;
import com.udacity.thefedex87.takemyorder.ui.adapters.PhotoIndicatorContainerAdapter;
import com.udacity.thefedex87.takemyorder.ui.adapters.RestaurantPhotoAdapter;
import com.udacity.thefedex87.takemyorder.ui.adapters.RestaurantReviewsAdapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by feder on 11/06/2018.
 */

@Module
public class UserInterfaceModule {
    private List<String> photoUrls;
    private List<RestaurantReviewModel> reviews;
    private RestaurantPhotoAdapter.ImageLoadingState imageLoadingState;
    private int linearLayoutManagerOrientation = LinearLayoutManager.VERTICAL;
    private FoodInMenuAdapter.FoodInMenuActionClick foodInMenuActionClick;
    private AppCompatActivity parentActivity;
    private List<Ingredient> ingredients;

    private FragmentManager fragmentManager;
    private LinkedHashMap<FoodTypes, List<Meal>> meals;
    private String restaurantId;


    public UserInterfaceModule(List<String> photoUrls, List<RestaurantReviewModel> reviews, RestaurantPhotoAdapter.ImageLoadingState imageLoadingState, int linearLayoutManagerOrientation){
        this.photoUrls = photoUrls;
        this.reviews = reviews;
        this.imageLoadingState = imageLoadingState;
        this.linearLayoutManagerOrientation = linearLayoutManagerOrientation;
    }

    public UserInterfaceModule(FragmentManager fragmentManager, LinkedHashMap<FoodTypes, List<Meal>> meals, String restaurantId) {
        this.fragmentManager = fragmentManager;
        this.meals = meals;
        this.restaurantId = restaurantId;
    }

    //TODO: creare un costruttore apposito per FoodInMenuActionClick
    public UserInterfaceModule(int linearLayoutManagerOrientation, FoodInMenuAdapter.FoodInMenuActionClick foodInMenuActionClick, AppCompatActivity parentActivity) {
        this(null, null, null, linearLayoutManagerOrientation);
        this.foodInMenuActionClick = foodInMenuActionClick;
        this.parentActivity = parentActivity;
    }

    public UserInterfaceModule(List<Ingredient> ingredients, int linearLayoutManagerOrientation){
        this.linearLayoutManagerOrientation = linearLayoutManagerOrientation;
        this.ingredients = ingredients;
    }

    public UserInterfaceModule(List<?> list){
        if (list.size() == 0){
            photoUrls = new ArrayList<>();
            reviews = new ArrayList<>();
        } else {
            if (list.get(0) instanceof String){
                photoUrls = (List<String>)list;
            } else {
                reviews = (List<RestaurantReviewModel>)list;
            }
        }
    }

//    public UserInterfaceModule(List<RestaurantReviewModel> reviews){
//        this.reviews = reviews;
//    }

    @Singleton
    @Provides
    public PhotoIndicatorContainerAdapter providePhotoIndicatorContainerAdapter(Context context){
        return new PhotoIndicatorContainerAdapter(context, photoUrls.size());
    }

    @Singleton
    @Provides
    public RestaurantPhotoAdapter provideRestaurantPhotoAdapter(Context context){
        return new RestaurantPhotoAdapter(photoUrls, imageLoadingState, context);
    }

    @Provides
    public LinearLayoutManager provideLinearLayoutManager(Context context){
        return new LinearLayoutManager(context, linearLayoutManagerOrientation, false);
    }

    @Provides
    public GridLayoutManager provideGridLayoutManager(Context context){
        int nOfColumns = context.getResources().getInteger(R.integer.menu_column);
        return new GridLayoutManager(context, nOfColumns);
    }

    @Singleton
    @Provides
    public RestaurantReviewsAdapter provideRestaurantReviewsAdapter(Context context){
        return new RestaurantReviewsAdapter(context, reviews);
    }

    @Singleton
    @Provides
    public FoodInMenuAdapter provideFoodInMenuAdapter(Context context){
        return new FoodInMenuAdapter(context, foodInMenuActionClick, parentActivity);
    }

    @Singleton
    @Provides
    public DishIngredientsAdapter provideDishIngredientsAdapter(){
        return new DishIngredientsAdapter(ingredients);
    }

    @Provides
    public FoodTypePagerAdapter provideFoodTypePagerAdapter(Context context){
        return new FoodTypePagerAdapter(fragmentManager, meals, restaurantId, context);
    }
}
