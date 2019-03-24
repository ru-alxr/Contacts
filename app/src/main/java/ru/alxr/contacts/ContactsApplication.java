package ru.alxr.contacts;

import android.app.Application;

import ru.alxr.contacts.di.AppComponent;
import ru.alxr.contacts.di.AppModule;
import ru.alxr.contacts.di.DaggerAppComponent;

public class ContactsApplication extends Application {

    private static AppComponent component;

    public static AppComponent getComponent() {
        return component;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        component = buildComponent();
    }

    private AppComponent buildComponent() {
        return DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))
                .build();
    }

}