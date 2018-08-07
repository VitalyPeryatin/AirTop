package com.example.infinity.airtop.di.components;

import com.example.infinity.airtop.data.db.interactors.ChatInteractor;
import com.example.infinity.airtop.data.prefs.app.AppPreferencesHelper;
import com.example.infinity.airtop.di.modules.RealChatModule;
import com.example.infinity.airtop.ui.chat.ChatPresenter;
import com.example.infinity.airtop.ui.chat.MessageBus;
import com.example.infinity.airtop.utils.serverWorker.IServerPostman;

import dagger.Component;

@Component(modules = RealChatModule.class)
public interface ChatComponent {
    void inject(ChatPresenter chatPresenter);
    MessageBus getMessageBus();
    IServerPostman getServerSending();
    ChatInteractor getChatInteractor();
    AppPreferencesHelper getPreferences();
}