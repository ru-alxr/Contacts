package ru.alxr.contacts.view;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import androidx.fragment.app.FragmentManager;
import ru.alxr.contacts.di.MainViewComponent;
import ru.alxr.contacts.features.navigation.INavigator;

public class PresenterMain implements IPresenterMain {

    @Inject
    INavigator navigator;

    private WeakReference<IPresenterMainCallback> mainCallbackReference;

    public PresenterMain() {
        MainViewComponent.Holder.get().inject(this);
    }

    private boolean isJustCreated = true;

    @Override
    public void onCreate(FragmentManager fragmentManager, int containerId) {
        navigator.set(fragmentManager, containerId);
        if (isJustCreated) navigator.navigateContacts();
        isJustCreated = false;
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