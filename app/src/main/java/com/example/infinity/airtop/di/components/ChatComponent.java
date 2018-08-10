package com.example.infinity.airtop.di.components;

import com.example.infinity.airtop.di.modules.ChatModule;
import com.example.infinity.airtop.ui.chat.ChatActivity;
import com.example.infinity.airtop.ui.chat.ChatPresenter;

import dagger.Component;

@Component(modules = ChatModule.class)
public interface ChatComponent {
    void inject(ChatActivity chatActivity);
    ChatPresenter getPresenter();
}