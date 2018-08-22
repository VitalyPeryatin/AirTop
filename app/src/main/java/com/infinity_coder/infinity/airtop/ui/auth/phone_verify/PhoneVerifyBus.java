package com.infinity_coder.infinity.airtop.ui.auth.phone_verify;

import com.infinity_coder.infinity.airtop.data.network.response.PhoneAuthResponse;

import java.util.ArrayList;

public class PhoneVerifyBus {
    private ArrayList<OnPhoneVerifyListener> authListeners = new ArrayList<>();

    public void subscribe(OnPhoneVerifyListener authListener){
        authListeners.add(authListener);
    }

    public void unsubscribe(OnPhoneVerifyListener authListener){
        authListeners.remove(authListener);
    }

    public void onPhoneVerify(PhoneAuthResponse response){
        for (OnPhoneVerifyListener authListener : authListeners) {
            authListener.onPhoneVerify(response);
        }
    }
}
