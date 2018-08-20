package com.infinity_coder.infinity.airtop.data.prefs.auth;

import android.content.Context;

public class FakeAuthPreference extends AuthPreference {

    public FakeAuthPreference(Context context) {
        super(context);
        sPref = context.getSharedPreferences("authPref_test", Context.MODE_PRIVATE);
    }
}
