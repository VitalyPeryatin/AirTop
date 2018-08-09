package com.example.infinity.airtop.data.prefs.auth;

public class TestAuthPreference implements AuthPreferencesHelper {
    @Override
    public boolean isEntered() {
        return false;
    }

    @Override
    public void saveEnter(boolean isEnter) {

    }

    @Override
    public String getCurrentPhone() {
        return null;
    }

    @Override
    public void saveCurrentPhone(String phone) {

    }

    @Override
    public boolean haveNickname() {
        return false;
    }

    @Override
    public void saveHaveNickname(boolean nickname) {

    }
}
