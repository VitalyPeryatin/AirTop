package com.example.infinity.airtop.ui.usernameUpdater;

import com.arellomobile.mvp.MvpView;
import com.example.infinity.airtop.data.network.CheckingUsername;

public interface UsernameUpdaterView extends MvpView {
    void onEmptyUsernameField();
    void onSmallUsername();
    void onUsernameIsTaken();
    void onUsernameFree();

    void onSendUsername(String username, String availableToUpdate);
    void onUpdateUsername();
}
