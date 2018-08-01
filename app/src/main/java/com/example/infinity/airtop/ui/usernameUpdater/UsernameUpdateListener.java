package com.example.infinity.airtop.ui.usernameUpdater;

import com.example.infinity.airtop.data.network.CheckingUsername;
import com.example.infinity.airtop.data.network.UserRequest;
import com.example.infinity.airtop.ui.listeners.OnUsernameUpdateListener;

import java.util.ArrayList;

public class UsernameUpdateListener {
    private ArrayList<OnUsernameUpdateListener> usernameUpdateListeners = new ArrayList<>();

    public void subscribe(OnUsernameUpdateListener usernameUpdateListener){
        usernameUpdateListeners.add(usernameUpdateListener);
    }

    public void unsubscribe(OnUsernameUpdateListener usernameUpdateListener){
        usernameUpdateListeners.remove(usernameUpdateListener);
    }

    public void onUpdateUsername(UserRequest userRequest){
        for (OnUsernameUpdateListener usernameUpdateListener : usernameUpdateListeners) {
            usernameUpdateListener.onUpdateUsername(userRequest);
        }
    }

    public void onResultUsernameCheck(CheckingUsername checkingUsername){
        for (OnUsernameUpdateListener usernameUpdateListener : usernameUpdateListeners) {
            usernameUpdateListener.onResultUsernameCheck(checkingUsername);
        }
    }
}
