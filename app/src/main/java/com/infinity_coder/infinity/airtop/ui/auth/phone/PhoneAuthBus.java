package com.infinity_coder.infinity.airtop.ui.auth.phone;

import com.infinity_coder.infinity.airtop.data.network.response.PhoneAuthResponse;

import java.util.ArrayList;

public class PhoneAuthBus {
    private ArrayList<OnPhoneAuthListener> authListeners = new ArrayList<>();

    public void subscribe(OnPhoneAuthListener authListener){
        authListeners.add(authListener);
    }

    public void unsubscribe(OnPhoneAuthListener authListener){
        authListeners.remove(authListener);
    }

    public void onPhoneAuth(PhoneAuthResponse response){
        for (OnPhoneAuthListener authListener : authListeners) {
            authListener.onPhoneAuth(response);
        }
    }
}