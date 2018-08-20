package com.infinity_coder.infinity.airtop.di.modules;

import com.infinity_coder.infinity.airtop.App;
import com.infinity_coder.infinity.airtop.data.db.interactors.ChatInteractor;
import com.infinity_coder.infinity.airtop.data.prefs.auth.AuthPreference;
import com.infinity_coder.infinity.airtop.ui.auth.phone.PhoneAuthBus;
import com.infinity_coder.infinity.airtop.ui.auth.phone.PhoneAuthPresenter;
import com.infinity_coder.infinity.airtop.utils.ServerPostman;

import dagger.Module;
import dagger.Provides;

@Module
public class PhoneAuthModule {
    @Provides
    PhoneAuthBus getPhoneAuthBus(){
        return App.getInstance().getResponseListeners().getPhoneAuthBus();
    }

    @Provides
    ServerPostman getServerWorker(){
        return new ServerPostman();
    }

    @Provides
    AuthPreference getPreferences(){
        return App.getInstance().getAuthPreference();
    }

    @Provides
    ChatInteractor getChatInteractor(){
        return new ChatInteractor();
    }

    @Provides
    PhoneAuthPresenter getPresenter(ChatInteractor interactor, ServerPostman serverPostman,
                                    PhoneAuthBus phoneAuthBus, AuthPreference preferencesHelper){
        return new PhoneAuthPresenter(interactor, serverPostman, phoneAuthBus, preferencesHelper);
    }
}
