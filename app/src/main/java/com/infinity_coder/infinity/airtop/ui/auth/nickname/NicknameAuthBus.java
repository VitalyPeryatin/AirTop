package com.infinity_coder.infinity.airtop.ui.auth.nickname;

import com.infinity_coder.infinity.airtop.data.network.response.NicknameAuthResponse;

import java.util.ArrayList;

public class NicknameAuthBus {
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