package com.example.infinity.airtop.di.modules;

import com.example.infinity.airtop.App;
import com.example.infinity.airtop.data.db.interactors.ChatInteractor;
import com.example.infinity.airtop.data.prefs.app.AppPreference;
import com.example.infinity.airtop.data.prefs.app.AppPreferencesHelper;
import com.example.infinity.airtop.data.prefs.auth.AuthPreference;
import com.example.infinity.airtop.data.prefs.auth.AuthPreferencesHelper;
import com.example.infinity.airtop.ui.auth.phone.PhoneAuthBus;
import com.example.infinity.airtop.ui.auth.phone.PhoneAuthPresenter;
import com.example.infinity.airtop.ui.chat.ChatPresenter;
import com.example.infinity.airtop.ui.chat.MessageBus;
import com.example.infinity.airtop.utils.MessageEditor;
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
    AuthPreferencesHelper getPreferences(){
        return new AuthPreference();
    }

    @Provides
    ChatInteractor getChatInteractor(){
        return new ChatInteractor();
    }

    @Provides
    PhoneAuthPresenter getPresenter(ChatInteractor interactor, ServerPostman serverPostman,
                                    PhoneAuthBus phoneAuthBus, AuthPreferencesHelper preferencesHelper){
        return new PhoneAuthPresenter(interactor, serverPostman, phoneAuthBus, preferencesHelper);
    }
}
