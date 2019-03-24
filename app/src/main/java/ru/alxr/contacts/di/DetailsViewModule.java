package ru.alxr.contacts.di;

import dagger.Module;
import dagger.Provides;
import ru.alxr.contacts.di.scopes.DetailsViewScope;
import ru.alxr.contacts.features.details.IPresenterDetails;
import ru.alxr.contacts.features.details.PresenterDetails;

@Module
public class DetailsViewModule {

    @Provides
    @DetailsViewScope
    IPresenterDetails getPresenterDetails(){
        return new PresenterDetails();
    }

}
