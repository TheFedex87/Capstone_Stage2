package com.udacity.thefedex87.takemyorder.dagger;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

import com.udacity.thefedex87.takemyorder.ui.adapters.PhotoIndicatorContainerAdapter;
import com.udacity.thefedex87.takemyorder.ui.adapters.RestaurantPhotoAdapter;

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

    public UserInterfaceModule(List<String> photoUrls){
        this.photoUrls = photoUrls;
    }

    @Singleton
    @Provides
    public PhotoIndicatorContainerAdapter providePhotoIndicatorContainerAdapter(Context context){
        return new PhotoIndicatorContainerAdapter(context, photoUrls.size());
    }

    @Singleton
    @Provides
    public RestaurantPhotoAdapter provideRestaurantPhotoAdapter(Context context){
        return new RestaurantPhotoAdapter(photoUrls, context);
    }

    @Provides
    public LinearLayoutManager provideLinearLayoutManager(Context context){
        return new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
    }
}
