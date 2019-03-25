package ru.alxr.contacts.features.contacts;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import ru.alxr.contacts.di.MainViewComponent;
import ru.alxr.contacts.features.details.IContact;
import ru.alxr.contacts.features.navigation.INavigator;
import ru.alxr.contacts.features.permissions.IPermissionChecker;
import ru.alxr.contacts.features.storage.ISimpleStorage;

public class PresenterContacts implements IPresenterContacts {

    private static final int REQUEST_PERMISSION = 1;
    private static final String SHOULD_SHOW_RATIONALE = "shouldShowRequestPermissionRationale";

    public PresenterContacts() {
        MainViewComponent.Holder.get().inject(this);
    }

    private WeakReference<IPresenterContactsCallback> mCallbackReference;

    private boolean isPermissionRequested;
    private boolean isRationaleShowing;
    private boolean isBanDialogShowing;
    private boolean isRegretViewVisible;

    @Inject
    INavigator navigator;

    @Inject
    ISimpleStorage mSimpleStorage;

    @Inject
    IPermissionChecker mPermissionChecker;

    @Override
    public void setCallback(IPresenterContactsCallback callback) {
        mCallbackReference = new WeakReference<>(callback);
    }

    @Override
    public void onStart() {
        IPresenterContactsCallback callback = mCallbackReference != null ? mCallbackReference.get() : null;
        if (callback == null) return;
        if (isRationaleShowing) {
            callback.showRationale();
        }
        if (isRegretViewVisible) {
            callback.showButton();
        } else {
            callback.hideButton();
        }
        if (isBanDialogShowing) {
            callback.showPermanentlyDeniedDialog();
        }
        if (isPermissionRequested) {
            return;
        }
        isPermissionRequested = true;
        if (mPermissionChecker.selfCheckContacts()) {
            onPermissionGranted();
        } else {
            callback.requestPermission(new String[]{mPermissionChecker.getContactsPermissionName()}, REQUEST_PERMISSION);
        }
    }

    @Override
    public void onPermissionGranted() {
        isPermissionRequested = false;
        isBanDialogShowing = false;
        IPresenterContactsCallback callback = mCallbackReference != null ? mCallbackReference.get() : null;
        if (callback == null) return;
        callback.hideButton();
        callback.loadContacts();
    }

    @Override
    public void onPermissionDenied() {
        isRegretViewVisible = true;
        IPresenterContactsCallback callback = mCallbackReference != null ? mCallbackReference.get() : null;
        if (callback == null) return;
        callback.showButton();
    }

    @Override
    public void onContactSelected(IContact contact) {
        navigator.navigate(contact);
    }

    @Override
    public void onRationaleRequired() {
        IPresenterContactsCallback callback = mCallbackReference != null ? mCallbackReference.get() : null;
        if (callback == null) return;
        callback.showRationale();
    }

    @Override
    public void onRationaleShowing() {
        isRationaleShowing = true;
    }

    @Override
    public void onRationaleShown(boolean positive) {
        isRationaleShowing = false;
        if (positive) {
            IPresenterContactsCallback callback = mCallbackReference != null ? mCallbackReference.get() : null;
            if (callback == null) return;
            callback.requestPermission(new String[]{mPermissionChecker.getContactsPermissionName()}, REQUEST_PERMISSION);
        } else {
            onPermissionDenied();
        }
    }

    @Override
    public void onPermanentlyDeniedDialogShowing() {
        isBanDialogShowing = true;
    }

    @Override
    public void onPermanentlyDeniedDialogShown(boolean positive) {
        isBanDialogShowing = false;
        if (positive) {
            IPresenterContactsCallback callback = mCallbackReference != null ? mCallbackReference.get() : null;
            if (callback == null) return;
            // todo
            //navigator.navigateSettings();
            callback.goAppSettings();
        } else {
            onPermissionDenied();
        }
    }

    @Override
    public void onShowPermanentlyDeniedDialogRequest() {
        IPresenterContactsCallback callback = mCallbackReference != null ? mCallbackReference.get() : null;
        if (callback == null) return;
        callback.showPermanentlyDeniedDialog();
    }

    @Override
    public boolean handleResult(int requestCode, String[] permissions, int[] grantResults, boolean should) {
        if (requestCode != REQUEST_PERMISSION) return false;
        if (mPermissionChecker.selfCheckContacts()) {
            onPermissionGranted();
        } else {
            String saved = mSimpleStorage.getValue(SHOULD_SHOW_RATIONALE, null);
            mSimpleStorage.putValue(SHOULD_SHOW_RATIONALE, Boolean.toString(should));
            boolean isPermanentlyDenied = !Boolean.parseBoolean(saved) && !should;
            if (saved == null || !isPermanentlyDenied) {
                onPermissionDenied();
            } else {
                onShowPermanentlyDeniedDialogRequest();
            }
        }
        return true;
    }

    @Override
    public void onPermissionUnknown() {
        IPresenterContactsCallback callback = mCallbackReference != null ? mCallbackReference.get() : null;
        if (callback == null) return;
        if (mPermissionChecker.selfCheckContacts()) {
            onPermissionGranted();
        } else {
            String saved = mSimpleStorage.getValue(SHOULD_SHOW_RATIONALE, null);
            if (Boolean.parseBoolean(saved)) {
                onRationaleRequired();
                return;
            }
            callback.requestPermission(new String[]{mPermissionChecker.getContactsPermissionName()}, REQUEST_PERMISSION);
        }
    }

}