package ru.alxr.contacts.view;

import android.os.Bundle;
import android.util.Log;

import javax.inject.Inject;

import ru.alxr.contacts.R;
import ru.alxr.contacts.base.ActivityBase;
import ru.alxr.contacts.di.MainViewComponent;

public class MainActivity extends ActivityBase {

    @Inject
    IPresenterMain presenterActivityMain;

    @SuppressWarnings("FieldCanBeLocal")//must keep to avoid garbage collected
    private PresenterActivityMainCallback mCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainViewComponent.Holder.get().inject(this);
        setContentView(R.layout.activity_main);
        presenterActivityMain.onCreate(getSupportFragmentManager(), R.id.container_view);
        mCallback = new PresenterActivityMainCallback();
        presenterActivityMain.setCallback(mCallback);
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

    @Override
    protected void onComponentShouldBeDestroyed() {
        Log.d("alxr_debug", "MainActivity: onComponentShouldBeDestroyed");
        MainViewComponent.Holder.finalizeComponent();
    }

}