package ru.alxr.contacts.features.navigation;

import android.os.Bundle;
import android.util.Log;

import java.lang.ref.WeakReference;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import ru.alxr.contacts.features.contacts.FragmentContacts;
import ru.alxr.contacts.features.details.FragmentDetails;
import ru.alxr.contacts.features.details.IContact;

public class Navigator implements INavigator {

    private WeakReference<FragmentManager> refFragmentManager;
    private int containerId;

    @Override
    public void set(FragmentManager fragmentManager, int containerId) {
        this.refFragmentManager = new WeakReference<>(fragmentManager);
        this.containerId = containerId;
    }

    @Override
    public void navigateContacts() {
        Log.d("alxr_debug", "Navigator: navigateContacts");
        FragmentManager manager;
        if (refFragmentManager == null || (manager = refFragmentManager.get()) == null) return;
        Fragment fragment = manager.findFragmentByTag(TAG_CONTACTS_LIST);
        Log.d("alxr_debug", "Navigator: navigateContacts...fragment = " + fragment);
        if (fragment == null) fragment = new FragmentContacts();
        manager
                .beginTransaction()
                .replace(containerId, fragment, TAG_CONTACTS_LIST)
                .commit();
    }

    @Override
    public void navigate(@NonNull IContact contact) {
        Log.d("alxr_debug", "Navigator: navigate");
        FragmentManager manager;
        if (refFragmentManager == null || (manager = refFragmentManager.get()) == null) return;
        FragmentDetails fragment = new FragmentDetails();
        Bundle args = new Bundle();
        args.putParcelable(PAYLOAD, contact);
        fragment.setArguments(args);
        manager
                .beginTransaction()
                .replace(containerId, fragment, TAG_CONTACT_DETAILS)
                .commit();
    }

    @Override
    public boolean navigateBack() {
        Log.d("alxr_debug", "Navigator: navigateBack");
        FragmentManager manager;
        if (refFragmentManager == null || (manager = refFragmentManager.get()) == null)
            return false;
        Log.d("alxr_debug", "Navigator: navigateBack..." + manager.findFragmentByTag(TAG_CONTACT_DETAILS));
        return manager.findFragmentByTag(TAG_CONTACT_DETAILS) != null;
    }

}