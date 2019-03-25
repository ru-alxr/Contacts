package ru.alxr.contacts.di;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.alxr.contacts.features.navigation.INavigator;
import ru.alxr.contacts.features.navigation.Navigator;
import ru.alxr.contacts.features.permissions.IPermissionChecker;
import ru.alxr.contacts.features.permissions.PermissionChecker;
import ru.alxr.contacts.features.storage.ISimpleStorage;
import ru.alxr.contacts.features.storage.SimpleStorage;

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

    @Provides
    @Singleton
    ISimpleStorage provideStorage(Context context){
        return new SimpleStorage(context);
    }

    @Provides
    @Singleton
    IPermissionChecker provideChecker(Context context){
        return new PermissionChecker(context);
    }

}