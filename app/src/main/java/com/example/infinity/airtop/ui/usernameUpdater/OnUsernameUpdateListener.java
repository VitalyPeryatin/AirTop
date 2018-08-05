package com.example.infinity.airtop.ui.usernameUpdater;

import com.example.infinity.airtop.data.network.CheckingUsername;
import com.example.infinity.airtop.data.network.UserRequest;

public interface OnUsernameUpdateListener {
    void onUpdateUsername(UserRequest user);
    void onResultUsernameCheck(CheckingUsername checkingUsername);
}
