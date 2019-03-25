package ru.alxr.contacts.di;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import ru.alxr.contacts.ContactsApplication;
import ru.alxr.contacts.di.scopes.MainViewScope;
import ru.alxr.contacts.features.contacts.FragmentContacts;
import ru.alxr.contacts.features.contacts.PresenterContacts;
import ru.alxr.contacts.view.MainActivity;
import ru.alxr.contacts.view.PresenterMain;

@MainViewScope
@Subcomponent(modules = {
        MainViewModule.class
})
public interface MainViewComponent extends AndroidInjector<MainActivity> {

    void inject(FragmentContacts fragment);

    void inject(PresenterMain presenter);

    void inject(PresenterContacts presenter);

    DetailsViewComponent plusComponent(DetailsViewModule module);

    final class Holder {
        private static volatile MainViewComponent instance;

        public static synchronized MainViewComponent get() {
            if (instance == null) {
                instance = ContactsApplication
                        .getApplicationComponent()
                        .plusComponent(new MainViewModule());
            }
            return instance;
        }

        public static synchronized void finalizeComponent() {
            instance = null;
        }

    }
}