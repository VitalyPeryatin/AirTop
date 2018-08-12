package com.example.infinity.airtop.di.modules;

import com.example.infinity.airtop.App;
import com.example.infinity.airtop.data.db.interactors.ChatInteractor;
import com.example.infinity.airtop.data.prefs.app.AppPreference;
import com.example.infinity.airtop.data.prefs.auth.AuthPreference;
import com.example.infinity.airtop.ui.auth.phone.PhoneAuthBus;
import com.example.infinity.airtop.ui.auth.phone.PhoneAuthPresenter;
import com.example.infinity.airtop.utils.ServerPostman;

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
