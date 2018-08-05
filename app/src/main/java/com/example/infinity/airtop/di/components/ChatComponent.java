package com.example.infinity.airtop.di.components;

import com.example.infinity.airtop.data.db.interactors.ChatInteractor;
import com.example.infinity.airtop.data.prefs.app.AppPreferencesHelper;
import com.example.infinity.airtop.di.modules.MainChatModule;
import com.example.infinity.airtop.di.modules.RealChatModule;
import com.example.infinity.airtop.ui.chat.ChatPresenter;
import com.example.infinity.airtop.ui.chat.MessageBus;
import com.example.infinity.airtop.utils.serverWorker.LauncherServerSending;

import dagger.Component;

@Component(modules = {MainChatModule.class, RealChatModule.class})
public interface ChatComponent {
    void inject(ChatPresenter chatPresenter);
    MessageBus getMessageBus();
    LauncherServerSending getServerSending();
    ChatInteractor getChatInteractor();
    AppPreferencesHelper getPreferences();
}