package com.example.infinity.airtop.ui.usernameUpdater;

import com.arellomobile.mvp.MvpView;
import com.example.infinity.airtop.data.network.CheckingUsername;

public interface UsernameUpdaterView extends MvpView {
    void onUpdateUsername();
    void onResultUsernameCheck(CheckingUsername checkingUsername);
}
