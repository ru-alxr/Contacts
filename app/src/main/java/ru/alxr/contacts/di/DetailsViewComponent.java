package ru.alxr.contacts.di;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import ru.alxr.contacts.di.scopes.DetailsViewScope;
import ru.alxr.contacts.features.details.FragmentDetails;
import ru.alxr.contacts.features.details.PresenterDetails;

@DetailsViewScope
@Subcomponent(modules = {
        DetailsViewModule.class
})
public interface DetailsViewComponent extends AndroidInjector<FragmentDetails> {

    void inject(PresenterDetails presenter);

    final class Holder {
        private static volatile DetailsViewComponent instance;

        public static synchronized DetailsViewComponent get() {
            if (instance == null) {
                instance = MainViewComponent
                        .Holder
                        .get()
                        .plusComponent(new DetailsViewModule());
            }
            return instance;
        }

        public static synchronized void finalizeComponent() {
            instance = null;
        }

    }
}