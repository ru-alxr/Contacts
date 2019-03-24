package ru.alxr.contacts.features.details;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import ru.alxr.contacts.ContactsApplication;
import ru.alxr.contacts.features.navigation.INavigator;

public class PresenterDetails implements IPresenterDetails {

    public PresenterDetails(){
        ContactsApplication.getComponent().inject(this);
    }

    @Inject
    INavigator navigator;

    private IContact mContact;

    private WeakReference<IPresentDetailsCallback> mCallbackReference;

    @Override
    public void setCallback(IPresentDetailsCallback callback) {
        mCallbackReference = new WeakReference<>(callback);
    }

    @Override
    public void setContact(IContact contact) {
        mContact = contact;
    }

}