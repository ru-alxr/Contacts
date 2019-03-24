package ru.alxr.contacts.base;

import android.view.View;

import androidx.fragment.app.Fragment;

public class FragmentBase extends Fragment {

    protected void setOnClickListener(View parent, int resId, View.OnClickListener listener){
        View view = parent.findViewById(resId);
        if (view == null) return;
        view.setOnClickListener(listener);
    }

}