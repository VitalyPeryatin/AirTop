package com.infinity_coder.infinity.airtop.di.components;

import com.infinity_coder.infinity.airtop.di.modules.PhoneAuthModule;
import com.infinity_coder.infinity.airtop.ui.auth.phone.PhoneAuthFragment;
import com.infinity_coder.infinity.airtop.ui.auth.phone.PhoneAuthPresenter;

import dagger.Component;

@Component(modules = PhoneAuthModule.class)
public interface PhoneAuthComponent {
    void inject(PhoneAuthFragment phoneAuthFragment);
    PhoneAuthPresenter providePhoneAuthPresenter();
}
