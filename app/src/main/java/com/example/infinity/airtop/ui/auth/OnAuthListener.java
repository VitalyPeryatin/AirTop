package com.example.infinity.airtop.ui.auth;

import com.example.infinity.airtop.data.network.UserRequest;

public interface OnAuthListener {
    void onPhoneAuth(UserRequest user);
}
