package ru.alxr.contacts.features.contacts;

public interface IPresenterContactsCallback {

    void requestPermission();

    void hideButton();

    void showButton();

    void loadContacts();

}
