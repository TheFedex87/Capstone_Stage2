package com.udacity.thefedex87.takemyorder.dagger;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

import com.udacity.thefedex87.takemyorder.model.GooglePlaceDetailModel.RestaurantPhotoModel;
import com.udacity.thefedex87.takemyorder.model.GooglePlaceDetailModel.RestaurantReviewModel;
import com.udacity.thefedex87.takemyorder.ui.adapters.PhotoIndicatorContainerAdapter;
import com.udacity.thefedex87.takemyorder.ui.adapters.RestaurantPhotoAdapter;
import com.udacity.thefedex87.takemyorder.ui.adapters.RestaurantReviewsAdapter;

import java.util.ArrayList;
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

    public UserInterfaceModule(List<String> photoUrls, List<RestaurantReviewModel> reviews, RestaurantPhotoAdapter.ImageLoadingState imageLoadingState){
        this.photoUrls = photoUrls;
        this.reviews = reviews;
        this.imageLoadingState = imageLoadingState;
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
        return new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
    }

    @Singleton
    @Provides
    public RestaurantReviewsAdapter provideRestaurantReviewsAdapter(Context context){
        return new RestaurantReviewsAdapter(context, reviews);
    }
}
