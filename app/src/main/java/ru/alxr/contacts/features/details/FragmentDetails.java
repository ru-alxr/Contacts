package ru.alxr.contacts.features.details;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import ru.alxr.contacts.R;
import ru.alxr.contacts.base.FragmentBase;
import ru.alxr.contacts.di.DetailsViewComponent;
import ru.alxr.contacts.features.navigation.INavigator;

public class FragmentDetails extends FragmentBase {

    @Inject
    IPresenterDetails mPresenterDetails;

    private TextView mDetailsView;

    @SuppressWarnings("FieldCanBeLocal")//must keep to avoid garbage collected
    private IPresentDetailsCallback mPresenterDetailsCallback;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DetailsViewComponent.Holder.get().inject(this);
        mPresenterDetailsCallback = new PresenterDetailsCallback();
        mPresenterDetails.setCallback(mPresenterDetailsCallback);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDetailsView = view.findViewById(R.id.raw_data_view);
        Bundle args = getArguments();
        IContact contact;
        if (args == null || (contact = args.getParcelable(INavigator.PAYLOAD)) == null)
            throw new RuntimeException("contact is null");
        mPresenterDetails.setContact(contact);
    }

    private class PresenterDetailsCallback implements IPresentDetailsCallback {

        @Override
        public void setInfo(String value) {
            mDetailsView.setText(value);
        }

    }

    @Override
    protected void onComponentShouldBeDestroyed() {
        DetailsViewComponent.Holder.finalizeComponent();
    }

}