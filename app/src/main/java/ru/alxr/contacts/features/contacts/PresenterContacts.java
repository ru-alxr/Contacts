package ru.alxr.contacts.features.contacts;

import android.util.Log;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import ru.alxr.contacts.di.MainViewComponent;
import ru.alxr.contacts.features.details.ContactImpl;
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
        Log.d("alxr_debug", "PresenterContacts: setCallback");
        mCallbackReference = new WeakReference<>(callback);
    }

    @Override
    public void onDebug() {
        navigator.navigate(new ContactImpl());
    }

    @Override
    public void onStart() {
        Log.d("alxr_debug", "PresenterContacts: onStart permissionRequested=" + permissionRequested);
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
        Log.d("alxr_debug", "PresenterContacts: onPermissionGranted");
        IPresenterContactsCallback callback = mCallbackReference != null ? mCallbackReference.get() : null;
        if (callback == null) return;
        callback.hideButton();
    }

    @Override
    public void onPermissionDenied() {
        Log.d("alxr_debug", "PresenterContacts: onPermissionDenied");
        IPresenterContactsCallback callback = mCallbackReference != null ? mCallbackReference.get() : null;
        if (callback == null) return;
        callback.showButton();
    }

}