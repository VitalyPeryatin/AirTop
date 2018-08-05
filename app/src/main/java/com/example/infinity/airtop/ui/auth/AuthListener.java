package com.example.infinity.airtop.ui.auth;

import com.example.infinity.airtop.data.network.UserRequest;

import java.util.ArrayList;

public class AuthListener{
    private ArrayList<OnAuthListener> authListeners = new ArrayList<>();

    public void subscribe(OnAuthListener authListener){
        authListeners.add(authListener);
    }

    public void unsubscribe(OnAuthListener authListener){
        authListeners.remove(authListener);
    }

    public void onAuth(UserRequest user){
        for (OnAuthListener authListener : authListeners) {
            authListener.onPhoneAuth(user);
        }
    }
}