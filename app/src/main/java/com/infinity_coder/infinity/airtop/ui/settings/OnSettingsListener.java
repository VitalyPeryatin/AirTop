package com.infinity_coder.infinity.airtop.ui.settings;

import com.infinity_coder.infinity.airtop.data.network.response.ResponseModel;

public interface OnSettingsListener<R extends ResponseModel> {
    void onUpdateSettings(R response);
}
