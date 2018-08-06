package com.example.infinity.airtop.data.prefs.app;

import java.util.HashMap;

public class TestPreference implements AppPreferencesHelper {
    private HashMap<String, Integer> map = new HashMap<>();
    private String prefix = "position_by_";
    private int default_value = 0;

    public TestPreference(){
    }

    @Override
    public void saveAdapterPosition(String addressee, int position){
        map.put(prefix + addressee, position);
    }

    @Override
    public int getAdapterPosition(String addressee){
        if(map.get(prefix + addressee) == null) return default_value;
        return map.get(prefix + addressee);
    }

    @Override
    public String getCurrentPhone() {
        return null;
    }

    @Override
    public void saveCurrentPhone(String phone) {

    }
}
