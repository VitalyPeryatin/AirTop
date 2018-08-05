package com.example.infinity.airtop.data.prefs.auth;

interface AuthPreferencesHelper {
    boolean isEntered();
    void saveEnter(boolean isEnter);
    String getCurrentPhone();
    void saveCurrentPhone(String phone);
    boolean haveNickname();
    void saveHaveNickname(boolean nickname);
}
