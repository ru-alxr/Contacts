package ru.alxr.contacts.base;

import androidx.appcompat.app.AppCompatActivity;

public abstract class ActivityBase extends AppCompatActivity {

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isFinishing()) onComponentShouldBeDestroyed();
    }

    protected abstract void onComponentShouldBeDestroyed();

}