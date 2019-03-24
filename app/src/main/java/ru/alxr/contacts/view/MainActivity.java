package ru.alxr.contacts.view;

import ru.alxr.contacts.ContactsApplication;
import ru.alxr.contacts.R;
import ru.alxr.contacts.base.ActivityBase;

import android.os.Bundle;

import javax.inject.Inject;

public class MainActivity extends ActivityBase {

    @Inject
    IPresenterMain presenterActivityMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ContactsApplication.getComponent().inject(this);
        setContentView(R.layout.activity_main);
        presenterActivityMain.onCreate(getSupportFragmentManager(), R.id.container_view);
        presenterActivityMain.setCallback(new PresenterActivityMainCallback());
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenterActivityMain.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenterActivityMain.onStart(getSupportFragmentManager(), R.id.container_view);
    }

    @Override
    public void onBackPressed() {
        if (!presenterActivityMain.onBackPressed()) super.onBackPressed();
    }

    private class PresenterActivityMainCallback implements IPresenterMainCallback {

    }

}