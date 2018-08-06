package com.example.infinity.airtop.data.prefs.app;

import android.content.SharedPreferences;

public interface AppPreferencesHelper {
    void saveAdapterPosition(String addressee, int position);
    int getAdapterPosition(String addressee);
    String getCurrentPhone();
    void saveCurrentPhone(String phone);
}
