package com.example.infinity.airtop.presenters;

import android.app.Activity;

public interface Presenter<A extends Activity> {
    void attachActivity(A activity);
    void detachActivity();
}
