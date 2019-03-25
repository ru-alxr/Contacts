package ru.alxr.contacts.features.permissions;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

public class PermissionChecker implements IPermissionChecker {

    public PermissionChecker(Context context) {
        mContext = context;
    }

    private final Context mContext;

    @Override
    public boolean selfCheckContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return true;
        return mContext.checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public String getContactsPermissionName() {
        return Manifest.permission.READ_CONTACTS;
    }

}
