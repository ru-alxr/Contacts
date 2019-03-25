package ru.alxr.contacts.features.contacts;

import ru.alxr.contacts.features.details.IContact;

public interface IPresenterContacts {

    void setCallback(IPresenterContactsCallback callback);

    void onStart();

    void onPermissionGranted();

    void onPermissionDenied();

    void onContactSelected(IContact contact);

}