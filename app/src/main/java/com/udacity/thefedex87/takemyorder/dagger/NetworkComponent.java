package com.udacity.thefedex87.takemyorder.dagger;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.udacity.thefedex87.takemyorder.retrofit.GooglePlacesApiInterface;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Federico on 09/06/2018.
 */

@Singleton
@Component(modules = {NetworkModule.class, ApplicationModule.class})
public interface NetworkComponent {
    GooglePlacesApiInterface getGooglePlacesApiInterface();
    Picasso getPicasso();
    Gson getGson();
}
