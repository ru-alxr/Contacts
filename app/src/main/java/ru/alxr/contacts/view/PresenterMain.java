package ru.alxr.contacts.view;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import androidx.fragment.app.FragmentManager;
import ru.alxr.contacts.ContactsApplication;
import ru.alxr.contacts.features.navigation.INavigator;

public class PresenterMain implements IPresenterMain {

    @Inject
    INavigator navigator;

    private WeakReference<IPresenterMainCallback> mainCallbackReference;

    public PresenterMain() {
        ContactsApplication.getComponent().inject(this);
    }

    @Override
    public void onCreate(FragmentManager fragmentManager, int containerId) {
        navigator.set(fragmentManager, containerId);
        navigator.navigateContacts();
    }

    @Override
    public void onStop() {

    }

    @Override
    public void onStart(FragmentManager fragmentManager, int containerId) {
        navigator.set(fragmentManager, containerId);
    }

    @Override
    public boolean onBackPressed() {
        if (navigator.navigateBack()) {
            navigator.navigateContacts();
            return true;
        }
        return false;
    }

    @Override
    public void setCallback(IPresenterMainCallback callback) {
        mainCallbackReference = new WeakReference<>(callback);
    }

}