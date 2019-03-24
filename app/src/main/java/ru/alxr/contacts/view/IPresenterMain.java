package ru.alxr.contacts.view;

import androidx.fragment.app.FragmentManager;

public interface IPresenterMain {

    void setCallback(IPresenterMainCallback callback);

    void onCreate(FragmentManager fragmentManager, int containerId);

    void onStart(FragmentManager fragmentManager, int containerId);

    boolean onBackPressed();

}