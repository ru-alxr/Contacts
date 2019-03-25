package ru.alxr.contacts.features.storage;

public interface ISimpleStorage {

    String getValue(String key, String defValue);

    void putValue(String key, String value);

}
