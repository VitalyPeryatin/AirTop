package com.example.infinity.airtop.data.prefs.auth;

public interface AuthPreferencesHelper {
    boolean isEntered();
    void saveEnter(boolean isEnter);
    String getCurrentPhone();
    void saveCurrentPhone(String phone);
    boolean haveNickname();
    void saveUserHasNickname(boolean nickname);
}
