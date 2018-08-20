package com.infinity_coder.infinity.airtop.ui.settings.updaters.username;

import com.arellomobile.mvp.MvpView;

public interface UsernameSettingsView extends MvpView {
    void onEmptyUsernameField();
    void onSmallUsername();
    void onUsernameIsTaken();
    void onUsernameFree();

    void onSendUsername(String username, String availableToUpdate);
    void onUpdateUsername();
}
