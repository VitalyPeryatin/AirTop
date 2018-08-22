package com.infinity_coder.infinity.airtop.di.modules;

import com.infinity_coder.infinity.airtop.App;
import com.infinity_coder.infinity.airtop.data.db.interactors.ChatInteractor;
import com.infinity_coder.infinity.airtop.data.prefs.app.AppPreference;
import com.infinity_coder.infinity.airtop.data.prefs.auth.AuthPreference;
import com.infinity_coder.infinity.airtop.ui.auth.phone_verify.PhoneVerifyBus;

import dagger.Module;
import dagger.Provides;

@Module
public class PhoneAuthModule {
    @Provides
    PhoneVerifyBus getPhoneAuthBus(){
        return App.getInstance().getResponseListeners().getPhoneAuthBus();
    }

    @Provides
    AuthPreference getAuthPreferences(){
        return App.getInstance().getAuthPreference();
    }

    @Provides
    AppPreference getAppPreferences(){
        return App.getInstance().getAppPreference();
    }

    @Provides
    ChatInteractor getChatInteractor(){
        return new ChatInteractor();
    }
}
