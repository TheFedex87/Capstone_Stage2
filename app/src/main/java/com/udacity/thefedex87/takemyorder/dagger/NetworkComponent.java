package com.udacity.thefedex87.takemyorder.dagger;

import com.squareup.picasso.Picasso;
import com.udacity.thefedex87.takemyorder.retrofit.LocationsApiInterface;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Federico on 09/06/2018.
 */

@Singleton
@Component(modules = {NetworkModule.class, ApplicationModule.class})
public interface NetworkComponent {
    LocationsApiInterface getLocationsApiInterface();
    Picasso getPicasso();
}