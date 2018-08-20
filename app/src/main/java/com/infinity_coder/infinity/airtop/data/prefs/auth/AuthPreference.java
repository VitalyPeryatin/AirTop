package com.infinity_coder.infinity.airtop.data.prefs.auth;

import android.content.Context;
import android.content.SharedPreferences;

public class AuthPreference {

    SharedPreferences sPref;

    private static final String
        FIRST_ENTER_KEY = "firstEnter",
        CURRENT_PHONE_KEY = "currentUserPhone",
        NICKNAME_KEY = "haveNickname";

    public AuthPreference(Context context){
        sPref = context.getSharedPreferences("authPref", Context.MODE_PRIVATE);
    }

    public boolean isEntered() {
        return sPref.getBoolean(FIRST_ENTER_KEY, false);
    }

    public void saveEnter(boolean isEnter) {
        SharedPreferences.Editor editor = sPref.edit();
        editor.putBoolean(FIRST_ENTER_KEY, isEnter);
        editor.apply();
    }

    public String getCurrentPhone() {
        return sPref.getString(CURRENT_PHONE_KEY, null);
    }

    public void saveCurrentPhone(String phone) {
        SharedPreferences.Editor editor = sPref.edit();
        editor.putString(CURRENT_PHONE_KEY, phone);
        editor.apply();
    }

    public boolean haveNickname() {
        return sPref.getBoolean(NICKNAME_KEY, false);
    }

    public void saveUserHasNickname(boolean has) {
        SharedPreferences.Editor editor = sPref.edit();
        editor.putBoolean(NICKNAME_KEY, has);
        editor.apply();
    }
}
