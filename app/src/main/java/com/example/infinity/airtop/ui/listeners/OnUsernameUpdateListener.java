package com.example.infinity.airtop.ui.listeners;

import com.example.infinity.airtop.data.network.CheckingUsername;
import com.example.infinity.airtop.data.network.UserRequest;

public interface OnUsernameUpdateListener {
    void onUpdateUsername(UserRequest user);
    void onResultUsernameCheck(CheckingUsername checkingUsername);
}
