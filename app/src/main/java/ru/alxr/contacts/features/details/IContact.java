package ru.alxr.contacts.features.details;

import android.os.Parcelable;

public interface IContact extends Parcelable {

    String getName();

    String getPhone();

    String getAccount();

    String getImageUri();

    String getEmail();

}