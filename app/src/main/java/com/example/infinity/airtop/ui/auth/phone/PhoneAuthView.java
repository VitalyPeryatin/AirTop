package com.example.infinity.airtop.ui.auth.phone;

import com.arellomobile.mvp.MvpView;

interface PhoneAuthView extends MvpView {
    void notValidPhone();
    void successfulPhoneAuth();
}
