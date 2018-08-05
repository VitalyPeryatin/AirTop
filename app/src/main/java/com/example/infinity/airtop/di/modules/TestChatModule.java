package com.example.infinity.airtop.di.modules;

import com.example.infinity.airtop.data.prefs.app.AppPreferencesHelper;
import com.example.infinity.airtop.data.prefs.app.TestPreference;
import com.example.infinity.airtop.ui.chat.MessageBus;
import com.example.infinity.airtop.utils.serverWorker.LauncherServerSending;
import com.example.infinity.airtop.utils.serverWorker.TestLauncherServerSending;

import dagger.Module;
import dagger.Provides;

@Module
public class TestChatModule {


    @Provides
    MessageBus getMessageBus(){
        return new MessageBus();
    }

    @Provides
    LauncherServerSending getServerWorker(){
        return new TestLauncherServerSending();
    }

    @Provides
    AppPreferencesHelper getPreferences(){
        return new TestPreference();
    }
}
