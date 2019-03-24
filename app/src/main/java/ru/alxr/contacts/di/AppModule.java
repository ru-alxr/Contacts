package ru.alxr.contacts.di;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.alxr.contacts.features.contacts.IPresenterContacts;
import ru.alxr.contacts.features.contacts.PresenterContacts;
import ru.alxr.contacts.features.details.IPresenterDetails;
import ru.alxr.contacts.features.details.PresenterDetails;
import ru.alxr.contacts.features.navigation.INavigator;
import ru.alxr.contacts.features.navigation.Navigator;
import ru.alxr.contacts.view.IPresenterMain;
import ru.alxr.contacts.view.PresenterMain;

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
    IPresenterMain getPresenterActivityMain() {
        return new PresenterMain();
    }

    @Provides
    @Singleton
    IPresenterContacts getPresenterContacts(){
        return new PresenterContacts();
    }

    @Provides
    @Singleton
    IPresenterDetails getPresenterDetails(){
        return new PresenterDetails();
    }

}