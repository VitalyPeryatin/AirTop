package com.infinity_coder.infinity.airtop.di.components;

import com.infinity_coder.infinity.airtop.di.modules.PhoneAuthModule;
import com.infinity_coder.infinity.airtop.ui.auth.phone_verify.PhoneVerifyFragment;

import dagger.Component;

@Component(modules = PhoneAuthModule.class)
public interface PhoneAuthComponent {
    void inject(PhoneVerifyFragment phoneVerifyFragment);
}
