package ru.alxr.contacts.di;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjector;
import ru.alxr.contacts.ContactsApplication;

@Component(modules = {AppModule.class})
@Singleton
public interface AppComponent extends AndroidInjector<ContactsApplication> {

    MainViewComponent plusComponent(MainViewModule module);

}