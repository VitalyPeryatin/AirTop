package com.example.infinity.airtop.ui.chat;

import android.content.Context;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.infinity.airtop.data.db.interactors.ChatInteractor;
import com.example.infinity.airtop.data.db.model.Message;
import com.example.infinity.airtop.di.components.ChatComponent;
import com.example.infinity.airtop.di.components.DaggerChatComponent;
import com.example.infinity.airtop.presentation.presenters.listeners.OnMessageListener;
import com.example.infinity.airtop.utils.JsonConverter;
import com.example.infinity.airtop.utils.MessageEditor;
import com.example.infinity.airtop.utils.serverWorker.LauncherServerSending;
import com.example.infinity.airtop.utils.serverWorker.TestLauncherServerSending;

import javax.inject.Inject;

/**
 * Presenter of "ChatActivity"
 * @author infinity_coder
 * @version 1.0.2
 */
@InjectViewState
public class ChatPresenter extends MvpPresenter<ChatView> implements OnMessageListener {

    @Inject
    public ChatInteractor chatInteractor;
    @Inject
    public LauncherServerSending serverSending;
    @Inject
    public MessageBus messageBus;

    private String addresseePhone;
    private MessageEditor messageEditor;


    public ChatPresenter(){
        ChatComponent chatComponent = DaggerChatComponent.create();
        chatComponent.inject(this);
        messageEditor = MessageEditor.edit();
    }

    public void onCreate(String addresseePhone, String senderPhone){
        this.addresseePhone = addresseePhone;
        messageBus.subscribe(this);

        // Add to message the base data: "sender" and "addressee"
        messageEditor.addAddressPhone(addresseePhone);
        messageEditor.addSendPhone(senderPhone);
    }

    public void sendMessage() {
        if(messageEditor.isNotEmptyMessage()) {
            // Save message in DB and display the message
            Message message = new Message(messageEditor.getMessage(), Message.ROUTE_OUT);
            chatInteractor.insertMessage(message);
            getViewState().displayMessage(message);

            // Send message to server
            JsonConverter jsonConverter = new JsonConverter();
            String json = jsonConverter.toJson(messageEditor.getMessage());
            serverSending.sendMessage(json);

            // Clear existing message after sending
            messageEditor.clear();
        }
    }

    // Need ONLY for Unit tests, Sorry! It is a pain. (Don't Remove!)
    public TestLauncherServerSending getServerSending(){
        return (TestLauncherServerSending) serverSending;
    }

    public MessageEditor getMessageEditor() {
        return messageEditor;
    }

    public String getAddresseeUserPhone() {
        return addresseePhone;
    }

    // Listen events from server with messages throw MessageBus
    @Override
    public void onMessage(Message message) {
        if(message.addresseePhone.equals(addresseePhone))
            getViewState().displayMessage(message);
    }

    public void onDestroy() {
        super.onDestroy();
        messageBus.unsubscribe(this);
    }
}
