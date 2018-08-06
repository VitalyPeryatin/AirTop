package com.example.infinity.airtop.ui.auth.nickname;

import com.example.infinity.airtop.data.network.response.NicknameAuthResponse;

import java.util.ArrayList;

public class NicknameAuthListener {
    private ArrayList<OnNicknameAuthListener> authListeners = new ArrayList<>();

    public void subscribe(OnNicknameAuthListener authListener){
        authListeners.add(authListener);
    }

    public void unsubscribe(OnNicknameAuthListener authListener){
        authListeners.remove(authListener);
    }

    public void onNicknameAuth(NicknameAuthResponse response){
        for (OnNicknameAuthListener authListener : authListeners) {
            authListener.onNicknameAuth(response);
        }
    }
}