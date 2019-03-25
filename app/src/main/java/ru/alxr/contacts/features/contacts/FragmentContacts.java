package ru.alxr.contacts.features.contacts;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.alxr.contacts.R;
import ru.alxr.contacts.base.FragmentBase;
import ru.alxr.contacts.di.MainViewComponent;

public class FragmentContacts extends FragmentBase implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final int REQUEST_SETTINGS = 2;

    private static final int LOADER_ID = 3;

    @Inject
    LayoutInflater mLayoutInflater;

    @Inject
    IPresenterContacts mPresenterContacts;

    @SuppressWarnings("FieldCanBeLocal")//must keep to avoid garbage collected
    private PresenterContactsCallback mCallback;

    private View mRegretView;
    private RecyclerView mRecyclerView;

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
        mRegretView = view.findViewById(R.id.reload_view);
        mRegretView.setOnClickListener(this);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ContactsAdapter adapter = new ContactsAdapter(mLayoutInflater, mPresenterContacts::onContactSelected);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reload_view:
                mPresenterContacts.onPermissionUnknown();
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
        public void hideButton() {
            mRegretView.setVisibility(View.GONE);
        }

        @Override
        public void showButton() {
            mRegretView.setVisibility(View.VISIBLE);
        }

        @Override
        public void loadContacts() {
            LoaderManager
                    .getInstance(FragmentContacts.this)
                    .initLoader(LOADER_ID, null, FragmentContacts.this);
        }

        @Override
        public void showRationale() {
            Activity activity = getActivity();
            if (activity == null) return;
            mPresenterContacts.onRationaleShowing();
            showDualSelectorDialog(
                    activity,
                    R.string.permission_rationale,
                    R.string.permission_rationale_negative,
                    R.string.permission_rationale_positive,
                    () -> mPresenterContacts.onRationaleShown(false),
                    () -> mPresenterContacts.onRationaleShown(true)
            );
        }

        @Override
        public void showPermanentlyDeniedDialog() {
            Activity activity = getActivity();
            if (activity == null) return;
            mPresenterContacts.onPermanentlyDeniedDialogShowing();
            showDualSelectorDialog(
                    activity,
                    R.string.permission_permanently_denied_message,
                    R.string.permission_permanently_denied_negative,
                    R.string.permission_permanently_denied_positive,
                    () -> mPresenterContacts.onPermanentlyDeniedDialogShown(false),
                    () -> mPresenterContacts.onPermanentlyDeniedDialogShown(true)
            );
        }

        @Override
        public void goAppSettings() {
            Activity activity = getActivity();
            if (activity == null) return;
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
        public void requestPermission(String[] permissions, int requestCode) {
            FragmentContacts.this.requestPermissions(permissions, requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        boolean flag = shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS);
        if (mPresenterContacts.handleResult(requestCode, permissions, grantResults, flag)) return;
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        Activity activity = getActivity();
        if (activity == null) throw new RuntimeException("activity == null");
        return new CursorLoader(
                activity,
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null,
                "display_name ASC"
        );
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        ContactsAdapter adapter = (ContactsAdapter) mRecyclerView.getAdapter();
        if (adapter == null) return;
        adapter.setCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
    }

    @Override
    public void onStop() {
        super.onStop();
        ContactsAdapter adapter = (ContactsAdapter) mRecyclerView.getAdapter();
        if (adapter == null) return;
        adapter.setCursor(null);
    }

}