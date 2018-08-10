package com.example.infinity.airtop.mocks;

import com.example.infinity.airtop.data.prefs.app.AppPreferencesHelper;

import java.util.HashMap;
import java.util.Map;

public class AppPreferenceMock implements AppPreferencesHelper {
    private Map<String, Integer> map = new HashMap<>();
    private static final int DEFAULT_INT = 0;

    @Override
    public void saveAdapterPosition(String addressee, int position) {
        map.put(addressee, position);
    }

    @Override
    public int getAdapterPosition(String addressee) {
        return map.getOrDefault(addressee, DEFAULT_INT);
    }

    @Override
    public String getCurrentPhone() {
        return null;
    }

    @Override
    public void saveCurrentPhone(String phone) {

    }
}
