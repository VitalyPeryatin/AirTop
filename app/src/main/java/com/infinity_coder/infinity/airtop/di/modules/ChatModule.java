package com.infinity_coder.infinity.airtop.di.modules;


import com.infinity_coder.infinity.airtop.App;
import com.infinity_coder.infinity.airtop.data.db.interactors.ChatInteractor;
import com.infinity_coder.infinity.airtop.data.prefs.app.AppPreference;
import com.infinity_coder.infinity.airtop.ui.chat.ChatPresenter;
import com.infinity_coder.infinity.airtop.ui.chat.MessageBus;
import com.infinity_coder.infinity.airtop.utils.MessageEditor;

import dagger.Module;
import dagger.Provides;

@Module
public class ChatModule {

    @Provides
    MessageBus getMessageBus(){
        return App.getInstance().getResponseListeners().getMessageBus();
    }

    @Provides
    AppPreference getPreferences(){
        return App.getInstance().getAppPreference();
    }

    @Provides
    MessageEditor getMessageEditor(){
        return MessageEditor.edit();
    }

    @Provides
    ChatInteractor getChatInteractor(){
        return new ChatInteractor();
    }

    @Provides
    ChatPresenter getPresenter(ChatInteractor chatInteractor, MessageBus messageBus,
                               MessageEditor messageEditor, AppPreference preferencesHelper){
        return new ChatPresenter(chatInteractor, messageBus,
                messageEditor, preferencesHelper);
    }
}
