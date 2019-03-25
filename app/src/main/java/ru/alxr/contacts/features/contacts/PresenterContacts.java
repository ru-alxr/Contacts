package ru.alxr.contacts.features.contacts;

import android.util.Log;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import ru.alxr.contacts.di.MainViewComponent;
import ru.alxr.contacts.features.details.IContact;
import ru.alxr.contacts.features.navigation.INavigator;

public class PresenterContacts implements IPresenterContacts {

    public PresenterContacts() {
        MainViewComponent.Holder.get().inject(this);
    }

    private WeakReference<IPresenterContactsCallback> mCallbackReference;

    private boolean permissionRequested;

    @Inject
    INavigator navigator;

    @Override
    public void setCallback(IPresenterContactsCallback callback) {
        mCallbackReference = new WeakReference<>(callback);
    }

    @Override
    public void onStart() {
        IPresenterContactsCallback callback = mCallbackReference != null ? mCallbackReference.get() : null;
        if (callback == null) return;
        if (permissionRequested) {
            return;
        }
        permissionRequested = true;
        callback.requestPermission();
    }

    @Override
    public void onPermissionGranted() {
        permissionRequested = false;
        IPresenterContactsCallback callback = mCallbackReference != null ? mCallbackReference.get() : null;
        if (callback == null) return;
        callback.hideButton();
        callback.loadContacts();
    }

    @Override
    public void onPermissionDenied() {
        IPresenterContactsCallback callback = mCallbackReference != null ? mCallbackReference.get() : null;
        if (callback == null) return;
        callback.showButton();
    }

    @Override
    public void onContactSelected(IContact contact) {
        navigator.navigate(contact);
    }

}