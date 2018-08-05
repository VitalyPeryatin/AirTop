package com.example.infinity.airtop.data.prefs.app;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.infinity.airtop.App;

public class AppPreference implements AppPreferencesHelper {

    private SharedPreferences sPref;
    private String prefix = "position_by_";

    public AppPreference(){
        Context context = App.getInstance().getBaseContext();
        sPref = context.getSharedPreferences("mainPref", Context.MODE_PRIVATE);
    }

    @Override
    public void saveAdapterPosition(String addressee, int position){
        SharedPreferences.Editor editor = sPref.edit();
        editor.putInt(prefix + addressee, position);
        editor.apply();
    }

    @Override
    public int getAdapterPosition(String addressee){
        return sPref.getInt(prefix + addressee, 0);
    }


}
