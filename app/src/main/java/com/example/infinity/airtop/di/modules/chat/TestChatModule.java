package com.example.infinity.airtop.di.modules.chat;

import com.example.infinity.airtop.data.prefs.app.AppPreferencesHelper;
import com.example.infinity.airtop.data.prefs.app.TestAppPreference;
import com.example.infinity.airtop.ui.chat.MessageBus;
import com.example.infinity.airtop.utils.serverWorker.IServerPostman;
import com.example.infinity.airtop.utils.serverWorker.TestIServerPostman;

import dagger.Module;
import dagger.Provides;

@Module(includes = MainChatModule.class)
public class TestChatModule {


    @Provides
    MessageBus getMessageBus(){
        return new MessageBus();
    }

    @Provides
    IServerPostman getServerWorker(){
        return new TestIServerPostman();
    }

    @Provides
    AppPreferencesHelper getPreferences(){
        return new TestAppPreference();
    }
}
