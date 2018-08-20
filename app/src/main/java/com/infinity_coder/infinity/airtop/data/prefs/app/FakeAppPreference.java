package com.infinity_coder.infinity.airtop.data.prefs.app;


import android.content.Context;

public class FakeAppPreference extends AppPreference{

    public FakeAppPreference(Context context) {
        super(context);
        sPref = context.getSharedPreferences("mainPref_test", Context.MODE_PRIVATE);
    }
}
