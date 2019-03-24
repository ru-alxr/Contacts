package ru.alxr.contacts.di;

import dagger.Module;
import dagger.Provides;
import ru.alxr.contacts.di.scopes.MainViewScope;
import ru.alxr.contacts.features.contacts.IPresenterContacts;
import ru.alxr.contacts.features.contacts.PresenterContacts;
import ru.alxr.contacts.view.IPresenterMain;
import ru.alxr.contacts.view.PresenterMain;

@Module
public class MainViewModule {

    @Provides
    @MainViewScope
    IPresenterMain getPresenterActivityMain() {
        return new PresenterMain();
    }

    @Provides
    @MainViewScope
    IPresenterContacts getPresenterContacts() {
        return new PresenterContacts();
    }

}