package ru.alxr.contacts.features.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SimpleStorage implements ISimpleStorage {

    public SimpleStorage(Context context){
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    private final SharedPreferences mPreferences;

    @Override
    public String getValue(String key, String defValue) {
        return mPreferences.getString(key, defValue);
    }

    @Override
    public void putValue(String key, String value) {
        mPreferences.edit().putString(key, value).apply();
    }

}