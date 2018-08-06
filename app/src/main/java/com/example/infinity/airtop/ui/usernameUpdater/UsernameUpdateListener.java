package com.example.infinity.airtop.ui.usernameUpdater;

import com.example.infinity.airtop.data.network.response.UpdateUsernameResponse;

import java.util.ArrayList;

public class UsernameUpdateListener {
    private ArrayList<OnUsernameUpdateListener> usernameUpdateListeners = new ArrayList<>();

    public void subscribe(OnUsernameUpdateListener usernameUpdateListener){
        usernameUpdateListeners.add(usernameUpdateListener);
    }

    public void unsubscribe(OnUsernameUpdateListener usernameUpdateListener){
        usernameUpdateListeners.remove(usernameUpdateListener);
    }

    public void onUpdateUsername(UpdateUsernameResponse response){
        for (OnUsernameUpdateListener usernameUpdateListener : usernameUpdateListeners) {
            usernameUpdateListener.onUpdateUsername(response);
        }
    }
}
