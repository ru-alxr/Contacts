package ru.alxr.contacts.features.details;

import android.os.Parcel;
import android.os.Parcelable;

public class ContactImpl implements IContact {

    public ContactImpl(){}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    private ContactImpl(Parcel in) {
        //todo
    }

    public static final Parcelable.Creator<ContactImpl> CREATOR
            = new Parcelable.Creator<ContactImpl>() {
        public ContactImpl createFromParcel(Parcel in) {
            return new ContactImpl(in);
        }

        public ContactImpl[] newArray(int size) {
            return new ContactImpl[size];
        }
    };

}