package ru.alxr.contacts.features.details;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class ContactImpl implements IContact {

    public ContactImpl() {
    }

    private String name;
    private String phone;
    private String email;
    private String image;
    private String account;


    public static class Builder {
        private ContactImpl contact;

        public Builder() {
            contact = new ContactImpl();
        }

        public Builder setName(String value) {
            contact.name = value;
            return this;
        }

        public Builder setPhone(String value) {
            contact.phone = value;
            return this;
        }

        public Builder setEmail(String value) {
            contact.email = value;
            return this;
        }

        public Builder setImage(String value) {
            contact.image = value;
            return this;
        }

        public Builder setAccount(String value) {
            contact.account = value;
            return this;
        }

        public ContactImpl get() {
            return contact;
        }
    }

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

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPhone() {
        return phone;
    }

    @Override
    public String getAccount() {
        return account;
    }

    @Override
    public String getImageUri() {
        return image;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("%s (%s) phone = [%s] email = [%s] image = [%s]", name, account, phone, email, image);
    }
}