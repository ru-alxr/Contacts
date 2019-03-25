package ru.alxr.contacts.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import ru.alxr.contacts.R;

public abstract class FragmentBase extends Fragment {

    private boolean mIsStateSaved;

    private void setOnClickListener(AppCompatDelegate parent, int resId, View.OnClickListener listener) {
        View view = parent.findViewById(resId);
        if (view == null) return;
        view.setOnClickListener(listener);
    }

    private void setText(AppCompatDelegate parent, int resId, int text) {
        TextView view = parent.findViewById(resId);
        if (view == null) return;
        view.setText(text);
    }

    protected interface OnSelected {
        void perform();
    }

    protected void showDualSelectorDialog(
            @NonNull Activity activity,
            @StringRes int message,
            @StringRes int negativeLabel,
            @StringRes int positiveLabel,
            OnSelected negative,
            OnSelected positive) {

        AlertDialog dialog = new AlertDialog
                .Builder(activity)
                .setView(R.layout.dialog_dual_selection)
                .setCancelable(false)
                .show();
        setText(dialog.getDelegate(), R.id.message, message);
        setText(dialog.getDelegate(), R.id.positive, positiveLabel);
        setText(dialog.getDelegate(), R.id.negative, negativeLabel);
        View.OnClickListener listener = view -> {
            dialog.dismiss();
            switch (view.getId()) {
                case R.id.positive:
                    positive.perform();
                    break;
                case R.id.negative:
                    negative.perform();
                    break;
            }
        };
        setOnClickListener(dialog.getDelegate(), R.id.positive, listener);
        setOnClickListener(dialog.getDelegate(), R.id.negative, listener);
    }

    @Override
    public void onStart() {
        super.onStart();
        mIsStateSaved = false;
    }

    public void onResume() {
        super.onResume();
        mIsStateSaved = false;
    }

    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mIsStateSaved = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Activity activity = getActivity();
        if (activity != null && activity.isFinishing()) {
            onComponentShouldBeDestroyed();
            return;
        }
        if (mIsStateSaved) {
            mIsStateSaved = false;
            return;
        }
        if (isRemoving()) {
            onComponentShouldBeDestroyed();
        }
    }

    protected abstract void onComponentShouldBeDestroyed();

}