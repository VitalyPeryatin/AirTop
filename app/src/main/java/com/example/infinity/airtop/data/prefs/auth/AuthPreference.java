package com.example.infinity.airtop.data.prefs.auth;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.infinity.airtop.App;

public class AuthPreference implements AuthPreferencesHelper {

    private SharedPreferences sPref;
    private static final String
        FIRST_ENTER_KEY = "firstEnter",
        CURRENT_PHONE_KEY = "currentUserPhone",
        NICKNAME_KEY = "haveNickname";

    public AuthPreference(){
        Context context = App.getInstance().getBaseContext();
        sPref = context.getSharedPreferences("authPref", Context.MODE_PRIVATE);
    }

    @Override
    public boolean isEntered() {
        return sPref.getBoolean(FIRST_ENTER_KEY, false);
    }

    @Override
    public void saveEnter(boolean isEnter) {
        SharedPreferences.Editor editor = sPref.edit();
        editor.putBoolean(FIRST_ENTER_KEY, isEnter);
        editor.apply();
    }

    @Override
    public String getCurrentPhone() {
        return sPref.getString(CURRENT_PHONE_KEY, null);
    }

    @Override
    public void saveCurrentPhone(String phone) {
        SharedPreferences.Editor editor = sPref.edit();
        editor.putString(CURRENT_PHONE_KEY, phone);
        editor.apply();
    }

    @Override
    public boolean haveNickname() {
        return sPref.getBoolean(NICKNAME_KEY, false);
    }

    @Override
    public void saveUserHasNickname(boolean has) {
        SharedPreferences.Editor editor = sPref.edit();
        editor.putBoolean(NICKNAME_KEY, has);
        editor.apply();
    }
}
