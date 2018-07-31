package com.example.infinity.airtop.presentation.presenters;

import android.app.Activity;

public interface Presenter<A extends Activity> {
    void attachActivity(A activity);
    void detachActivity();
}
