package com.example.infinity.airtop.di.modules;

import com.example.infinity.airtop.App;
import com.example.infinity.airtop.ui.chat.MessageBus;
import com.example.infinity.airtop.utils.serverWorker.LauncherServerSending;
import com.example.infinity.airtop.utils.serverWorker.RealLauncherServerSending;

import dagger.Module;
import dagger.Provides;

@Module
public class RealChatModule {

    @Provides
    MessageBus getMessageBus(){
        return App.getInstance().getResponseListeners().getMessageBus();
    }

    @Provides
    LauncherServerSending getServerWorker(){
        return new RealLauncherServerSending();
    }

}
