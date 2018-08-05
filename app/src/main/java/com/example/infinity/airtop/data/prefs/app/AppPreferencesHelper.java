package com.example.infinity.airtop.data.prefs.app;

public interface AppPreferencesHelper {
    void saveAdapterPosition(String addressee, int position);
    int getAdapterPosition(String addressee);
}
