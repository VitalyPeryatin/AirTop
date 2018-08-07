package com.example.infinity.airtop.ui.usernameUpdater;

import com.arellomobile.mvp.MvpView;

public interface UsernameUpdaterView extends MvpView {
    void onEmptyUsernameField();
    void onSmallUsername();
    void onUsernameIsTaken();
    void onUsernameFree();

    void onSendUsername(String username, String availableToUpdate);
    void onUpdateUsername();
}
