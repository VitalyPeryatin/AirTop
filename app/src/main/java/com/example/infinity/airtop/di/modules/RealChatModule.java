package com.example.infinity.airtop.di.modules;

import com.example.infinity.airtop.App;
import com.example.infinity.airtop.data.prefs.app.AppPreference;
import com.example.infinity.airtop.data.prefs.app.AppPreferencesHelper;
import com.example.infinity.airtop.ui.chat.MessageBus;
import com.example.infinity.airtop.utils.serverWorker.IServerPostman;
import com.example.infinity.airtop.utils.serverWorker.ServerPostman;

import dagger.Module;
import dagger.Provides;

@Module(includes = MainChatModule.class)
public class RealChatModule {

    @Provides
    MessageBus getMessageBus(){
        return App.getInstance().getResponseListeners().getMessageBus();
    }

    @Provides
    IServerPostman getServerWorker(){
        return new ServerPostman();
    }

    @Provides
    AppPreferencesHelper getPreferences(){
        return new AppPreference();
    }

}
