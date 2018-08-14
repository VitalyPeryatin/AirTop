package com.example.infinity.airtop.ui.settings;

import com.example.infinity.airtop.data.network.response.ResponseModel;

public interface OnSettingsListener<R extends ResponseModel> {
    void onUpdateSettings(R response);
}
