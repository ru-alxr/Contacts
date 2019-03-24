package ru.alxr.contacts.features.details;

public interface IPresenterDetails {

    void setCallback(IPresentDetailsCallback callback);

    void setContact(IContact contact);

}