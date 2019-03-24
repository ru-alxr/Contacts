package ru.alxr.contacts.di;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.alxr.contacts.features.navigation.INavigator;
import ru.alxr.contacts.features.navigation.Navigator;

@Module
public class AppModule {

    public AppModule(Application application) {
        mAppContext = application;
    }

    private final Context mAppContext;

    @Provides
    @Singleton
    Context getContext() {
        return mAppContext;
    }

    @Provides
    @Singleton
    INavigator navigator() {
        return new Navigator();
    }

}