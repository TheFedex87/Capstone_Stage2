package com.udacity.thefedex87.takemyorder.dagger;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;
import com.udacity.thefedex87.takemyorder.retrofit.GooglePlacesApiInterface;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Federico on 09/06/2018.
 */

@Module
public class NetworkModule {
    private final String BASE_URL = "https://maps.googleapis.com";

    @Singleton
    @Provides
    public GooglePlacesApiInterface provideGooglePlacesApiInterface(Retrofit retrofit){
        return retrofit.create(GooglePlacesApiInterface.class);
    }

    @Singleton
    @Provides
    public Retrofit provideRetrofit(Gson gson){
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    @Singleton
    @Provides
    public Gson provideGson() {
        return new GsonBuilder().create();
    }

    @Singleton
    @Provides
    public Picasso providePicasso(Context context){
        return Picasso.with(context);
    }
}
