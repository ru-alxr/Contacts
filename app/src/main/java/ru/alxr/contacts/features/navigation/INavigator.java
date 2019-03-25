package ru.alxr.contacts.features.navigation;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import ru.alxr.contacts.features.details.IContact;

public interface INavigator {

    String TAG_CONTACT_DETAILS = "TAG_CONTACT_DETAILS";
    String TAG_CONTACTS_LIST = "TAG_CONTACTS_LIST";
    String PAYLOAD = "PAYLOAD";

    void set(FragmentManager fragmentManager, int containerId);

    void navigate(@NonNull IContact contact);

    void navigateContacts();

    boolean navigateBack();

    // void navigateSettings();
    // todo
    // need to figure out how to implement this feature right way

}
