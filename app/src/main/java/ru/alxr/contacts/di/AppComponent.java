package ru.alxr.contacts.di;

import javax.inject.Singleton;

import dagger.Component;
import ru.alxr.contacts.features.contacts.FragmentContacts;
import ru.alxr.contacts.features.contacts.PresenterContacts;
import ru.alxr.contacts.features.details.FragmentDetails;
import ru.alxr.contacts.features.details.PresenterDetails;
import ru.alxr.contacts.view.MainActivity;
import ru.alxr.contacts.view.PresenterMain;

@Component(modules = {AppModule.class})
@Singleton
public interface AppComponent {

    void inject(MainActivity activity);

    void inject(FragmentDetails fragment);

    void inject(FragmentContacts fragment);

    void inject(PresenterMain presenter);

    void inject(PresenterDetails presenter);

    void inject(PresenterContacts presenter);

}