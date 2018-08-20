package com.infinity_coder.infinity.airtop.ui.settings;

import com.infinity_coder.infinity.airtop.data.network.response.ResponseModel;

import java.util.ArrayList;

public class SettingsBus<R extends ResponseModel> {
    private ArrayList<OnSettingsListener<R>> settingsListeners = new ArrayList<>();

    public void subscribe(OnSettingsListener<R> settingsListener){
        settingsListeners.add(settingsListener);
    }

    public void unsubscribe(OnSettingsListener<R> settingsListener){
        settingsListeners.remove(settingsListener);
    }

    public void onUpdateSettings(R response){
        for (OnSettingsListener<R> settingsListener : settingsListeners) {
            settingsListener.onUpdateSettings(response);
        }
    }
}
