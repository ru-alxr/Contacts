package ru.alxr.contacts.features.contacts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import ru.alxr.contacts.ContactsApplication;
import ru.alxr.contacts.R;
import ru.alxr.contacts.base.FragmentBase;

public class FragmentContacts extends FragmentBase implements View.OnClickListener {

    @Inject
    IPresenterContacts mPresenterContacts;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ContactsApplication.getComponent().inject(this);
        mPresenterContacts.setCallback(new PresenterContactsCallback());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contacts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setOnClickListener(view, R.id.temp_label, this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.temp_label: mPresenterContacts.onDebug();
        }
    }

    private class PresenterContactsCallback implements IPresenterContactsCallback{

    }

}