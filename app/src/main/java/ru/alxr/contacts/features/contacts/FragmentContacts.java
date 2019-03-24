package ru.alxr.contacts.features.contacts;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import ru.alxr.contacts.R;
import ru.alxr.contacts.base.FragmentBase;
import ru.alxr.contacts.di.MainViewComponent;

public class FragmentContacts extends FragmentBase implements View.OnClickListener {

    private static final int REQUEST_PERMISSION = 1;
    private static final int REQUEST_SETTINGS = 2;

    private static final String STATE_VIEW_VISIBLE = "isRegretViewVisible";
    private static final String STATE_DIALOG_RATIONALE = "isRationaleShowing";
    private static final String STATE_DIALOG_BAN = "isPermanentBanShowing";

    @Inject
    IPresenterContacts mPresenterContacts;

    @SuppressWarnings("FieldCanBeLocal")//must keep to avoid garbage collected
    private PresenterContactsCallback mCallback;

    private View mRegretView;
    private boolean isRegretViewVisible;
    private boolean isRationaleShowing;
    private boolean isPermanentBanShowing;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainViewComponent.Holder.get().inject(this);
        mCallback = new PresenterContactsCallback();
        mPresenterContacts.setCallback(mCallback);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contacts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isRegretViewVisible = savedInstanceState != null && savedInstanceState.getBoolean(STATE_VIEW_VISIBLE);
        isRationaleShowing = savedInstanceState != null && savedInstanceState.getBoolean(STATE_DIALOG_RATIONALE);
        isPermanentBanShowing = savedInstanceState != null && savedInstanceState.getBoolean(STATE_DIALOG_BAN);
        setOnClickListener(view, R.id.temp_label, this);
        mRegretView = view.findViewById(R.id.reload_view);
        mRegretView.setVisibility(isRegretViewVisible ? View.VISIBLE : View.INVISIBLE);
        setOnClickListener(view, R.id.reload_view, this);
        if (isRationaleShowing) {
            showRationale();
        }
        if (isPermanentBanShowing) {
            showPermanentlyDeniedDialog();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_VIEW_VISIBLE, isRegretViewVisible);
        outState.putBoolean(STATE_DIALOG_RATIONALE, isRationaleShowing);
        outState.putBoolean(STATE_DIALOG_BAN, isPermanentBanShowing);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.temp_label:
                mPresenterContacts.onDebug();
                break;

            case R.id.reload_view:
                performPermissionRequest();
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenterContacts.onStart();
    }

    private class PresenterContactsCallback implements IPresenterContactsCallback {

        @Override
        public void requestPermission() {
            Activity activity = getActivity();
            if (activity == null) return;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                    PackageManager.PERMISSION_GRANTED == activity.checkSelfPermission(Manifest.permission.RECORD_AUDIO)
            ) {
                mPresenterContacts.onPermissionGranted();
            } else {
                performPermissionRequest();
            }
        }

        @Override
        public void hideButton() {
            isRegretViewVisible = false;
            mRegretView.setVisibility(View.INVISIBLE);
        }

        @Override
        public void showButton() {
            isRegretViewVisible = true;
            mRegretView.setVisibility(View.VISIBLE);
        }

    }

    private void performPermissionRequest() {
        Activity activity = getActivity();
        if (activity == null) return;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            mPresenterContacts.onPermissionGranted();
        } else {
            if (activity.checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                mPresenterContacts.onPermissionGranted();
                return;
            }
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String saved = preferences.getString("shouldShowRequestPermissionRationale", null);
            if (Boolean.parseBoolean(saved)) {
                showRationale();
                return;
            }
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != REQUEST_PERMISSION) return;
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mPresenterContacts.onPermissionGranted();
        } else {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String saved = preferences.getString("shouldShowRequestPermissionRationale", null);
            boolean actual = shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS);
            preferences.edit().putString("shouldShowRequestPermissionRationale", Boolean.toString(actual)).apply();
            if (saved == null) {
                mPresenterContacts.onPermissionDenied();
                return;
            }
            if (!Boolean.parseBoolean(saved) && !actual) {
                showPermanentlyDeniedDialog();
                return;
            }
            mPresenterContacts.onPermissionDenied();
        }
    }

    private void showPermanentlyDeniedDialog() {
        Activity activity = getActivity();
        if (activity == null) return;
        isPermanentBanShowing = true;
        showDualSelectorDialog(
                activity,
                R.string.permission_permanently_denied_message,
                R.string.permission_permanently_denied_negative,
                R.string.permission_permanently_denied_positive,
                () -> {
                    isPermanentBanShowing = false;
                    mPresenterContacts.onPermissionDenied();
                },
                () -> {
                    isPermanentBanShowing = false;
                    goAppSettings(activity);
                },
                null
        );
    }

    private class Selection {
        boolean performed;
    }

    private void showRationale() {
        Activity activity = getActivity();
        if (activity == null) return;
        Selection selection = new Selection();
        isRationaleShowing = true;
        showDualSelectorDialog(
                activity,
                R.string.permission_rationale,
                R.string.permission_rationale_negative,
                R.string.permission_rationale_positive,
                () -> {
                    isRationaleShowing = false;
                    selection.performed = true;
                    mPresenterContacts.onPermissionDenied();
                },
                () -> {
                    isRationaleShowing = false;
                    requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_PERMISSION);
                },
                null
        );
    }

    private void goAppSettings(Activity activity) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        if (intent.resolveActivity(activity.getPackageManager()) == null) {
            Toast.makeText(activity, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
            return;
        }
        startActivityForResult(intent, REQUEST_SETTINGS);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Activity activity = getActivity();
        if (activity == null) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean granted = activity.checkSelfPermission(Manifest.permission.READ_CONTACTS)
                    == PackageManager.PERMISSION_GRANTED;
            if (granted) mPresenterContacts.onPermissionGranted();
            else mPresenterContacts.onPermissionDenied();
        }
    }

    @Override
    protected void onComponentShouldBeDestroyed() {
        // Main activity should handle this event
    }

}