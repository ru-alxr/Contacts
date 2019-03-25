package ru.alxr.contacts.features.contacts;

import ru.alxr.contacts.features.details.IContact;

public interface IPresenterContacts {

    void setCallback(IPresenterContactsCallback callback);

    void onStart();

    void onPermissionGranted();

    void onPermissionDenied();

    void onContactSelected(IContact contact);

    void onRationaleRequired();

    void onRationaleShowing();

    void onRationaleShown(boolean positive);

    void onPermanentlyDeniedDialogShowing();

    void onPermanentlyDeniedDialogShown(boolean positive);

    void onShowPermanentlyDeniedDialogRequest();

    boolean handleResult(int requestCode, String[] permissions,
                         int[] grantResults,
                         boolean shouldShowRationale);

    void onPermissionUnknown();

}