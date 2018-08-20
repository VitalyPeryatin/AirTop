package com.infinity_coder.infinity.airtop.di.components;

import com.infinity_coder.infinity.airtop.di.modules.ChatModule;
import com.infinity_coder.infinity.airtop.ui.chat.ChatActivity;
import com.infinity_coder.infinity.airtop.ui.chat.ChatPresenter;

import dagger.Component;

@Component(modules = ChatModule.class)
public interface ChatComponent {
    void inject(ChatActivity chatActivity);
    ChatPresenter getPresenter();
}