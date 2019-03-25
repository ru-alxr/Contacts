package ru.alxr.contacts.features.details;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import ru.alxr.contacts.di.DetailsViewComponent;
import ru.alxr.contacts.features.navigation.INavigator;

public class PresenterDetails implements IPresenterDetails {

    public PresenterDetails(){
        DetailsViewComponent.Holder.get().inject(this);
    }

    @Inject
    INavigator navigator;

    private WeakReference<IPresentDetailsCallback> mCallbackReference;

    @Override
    public void setCallback(IPresentDetailsCallback callback) {
        mCallbackReference = new WeakReference<>(callback);
    }

    @Override
    public void setContact(IContact contact) {
        IPresentDetailsCallback callback = mCallbackReference != null ? mCallbackReference.get():null;
        if (callback == null) return;
        if (contact == null){
            callback.setInfo("No data");
        }else{
            callback.setInfo(contact.getName() + "\n" + contact.getPhone() + "\n" + contact.getImageUri() + "\nand what ever you need..." );
            //todo
        }
    }

}