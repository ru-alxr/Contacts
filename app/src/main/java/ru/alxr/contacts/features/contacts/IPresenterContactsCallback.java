package ru.alxr.contacts.features.contacts;

public interface IPresenterContactsCallback {

    void hideButton();

    void showButton();

    void loadContacts();

    void showRationale();

    void showPermanentlyDeniedDialog();

    void requestPermission(String[] permissions, int requestCode);

    void goAppSettings();

}
