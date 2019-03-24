package ru.alxr.contacts;

import android.app.Application;

import ru.alxr.contacts.di.AppComponent;
import ru.alxr.contacts.di.AppModule;
import ru.alxr.contacts.di.DaggerAppComponent;

public class ContactsApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        init(this);
    }

    private void init(ContactsApplication application) {
        instance = DaggerAppComponent
                .builder()
                .appModule(new AppModule(application))
                .build();
    }

    private static volatile AppComponent instance;

    public static synchronized AppComponent getApplicationComponent() {
        return instance;
    }

}