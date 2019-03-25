package ru.alxr.contacts.features.permissions;

public interface IPermissionChecker {

    boolean selfCheckContacts();
    String getContactsPermissionName();

}