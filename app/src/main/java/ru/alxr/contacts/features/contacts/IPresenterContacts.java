package ru.alxr.contacts.features.contacts;

public interface IPresenterContacts {

    void setCallback(IPresenterContactsCallback callback);

    void onDebug();

    void onStart();

    void onPermissionGranted();

    void onPermissionDenied();

}