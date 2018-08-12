package com.example.infinity.airtop.data.prefs.app;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.infinity.airtop.App;

public class AppPreference {

    private static final String CURRENT_PHONE_KEY = "currentUserPhone";
    private String prefix = "position_by_";

    SharedPreferences sPref;

    public AppPreference(Context context){
        sPref = context.getSharedPreferences("mainPref", Context.MODE_PRIVATE);
    }

    public void saveAdapterPosition(String addressee, int position){
        SharedPreferences.Editor editor = sPref.edit();
        editor.putInt(prefix + addressee, position);
        editor.apply();
    }

    public int getAdapterPosition(String addressee){
        return sPref.getInt(prefix + addressee, 0);
    }

    public String getCurrentPhone() {
        return sPref.getString(CURRENT_PHONE_KEY, null);
    }

    public void saveCurrentPhone(String phone) {
        SharedPreferences.Editor editor = sPref.edit();
        editor.putString(CURRENT_PHONE_KEY, phone);
        editor.apply();
    }

}
