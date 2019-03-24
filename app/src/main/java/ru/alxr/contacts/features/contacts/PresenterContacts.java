package ru.alxr.contacts.features.contacts;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import ru.alxr.contacts.ContactsApplication;
import ru.alxr.contacts.features.details.ContactImpl;
import ru.alxr.contacts.features.navigation.INavigator;

public class PresenterContacts implements IPresenterContacts {

    public PresenterContacts() {
        ContactsApplication.getComponent().inject(this);
    }

    private WeakReference<IPresenterContactsCallback> mCallbackReference;

    @Inject
    INavigator navigator;

    @Override
    public void setCallback(IPresenterContactsCallback callback) {
        mCallbackReference = new WeakReference<>(callback);
    }

    @Override
    public void onDebug() {
        navigator.navigate(new ContactImpl());
    }

}