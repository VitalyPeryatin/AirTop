package com.example.infinity.airtop.di.components;

import com.example.infinity.airtop.di.modules.PhoneAuthModule;
import com.example.infinity.airtop.ui.auth.phone.PhoneAuthFragment;
import com.example.infinity.airtop.ui.auth.phone.PhoneAuthPresenter;

import dagger.Component;

@Component(modules = PhoneAuthModule.class)
public interface PhoneAuthComponent {
    void inject(PhoneAuthFragment phoneAuthFragment);
    PhoneAuthPresenter providePhoneAuthPresenter();
}
