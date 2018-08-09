package com.example.infinity.airtop.di.modules.chat;

import com.example.infinity.airtop.data.db.interactors.ChatInteractor;

import dagger.Module;
import dagger.Provides;

@Module
public class MainChatModule {

    @Provides
    ChatInteractor getChatInteractor(){
        return new ChatInteractor();
    }
}
