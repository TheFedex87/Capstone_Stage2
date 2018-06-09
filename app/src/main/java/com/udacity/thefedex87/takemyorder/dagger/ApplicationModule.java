package com.udacity.thefedex87.takemyorder.dagger;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Federico on 09/06/2018.
 */

@Module
public class ApplicationModule {
    private Context context;

    public ApplicationModule(Context context){
        this.context = context;
    }

    @Singleton
    @Provides
    public Context provideContext(){
        return context;
    }
}
