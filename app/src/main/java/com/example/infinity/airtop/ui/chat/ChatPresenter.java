package com.example.infinity.airtop.ui.chat;

import android.content.Context;
import android.content.Intent;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.infinity.airtop.data.db.interactors.ChatInteractor;
import com.example.infinity.airtop.data.db.model.Message;
import com.example.infinity.airtop.data.network.MessageRequest;
import com.example.infinity.airtop.presentation.presenters.listeners.OnMessageListener;
import com.example.infinity.airtop.service.ClientService;
import com.example.infinity.airtop.utils.JsonConverter;
import com.example.infinity.airtop.App;
import com.example.infinity.airtop.utils.MessageEditor;

/**
 * Presenter of "ChatActivity"
 * @author infinity_coder
 * @version 1.0.2
 */
@InjectViewState
public class ChatPresenter extends MvpPresenter<ChatView> implements OnMessageListener {

    private String addresseePhone;
    private ChatInteractor chatInteractor;
    private Context context;
    private MessageBus messageBus;
    private MessageEditor messageEditor;

    ChatPresenter(){
        messageEditor = MessageEditor.edit();
        chatInteractor = new ChatInteractor();
        messageBus = App.getInstance().getResponseListeners().getMessageBus();
    }

    void onCreate(Intent intent){
        this.context = App.getInstance().getBaseContext();
        messageBus.subscribe(this);
        addresseePhone = intent.getStringExtra("addresseePhone");

        // Add to message the base data: "sender" and "addressee"
        messageEditor.addAddressPhone(addresseePhone);
        messageEditor.addSendPhone(App.getInstance().getCurrentUser().phone);
    }

    void sendMessage() {
        if(messageEditor.isNotEmptyMessage()) {
            // Save message in DB and display the message
            Message message = new Message(messageEditor.getMessage(), Message.ROUTE_OUT);
            chatInteractor.insertMessage(message);
            getViewState().displayMessage(message);

            // Send message to server
            JsonConverter jsonConverter = new JsonConverter();
            String json = jsonConverter.toJson(messageEditor.getMessage());
            Intent intent = new Intent(context, ClientService.class);
            intent.putExtra("request", json);
            context.startService(intent);

            // Clear existing message after sending
            messageEditor.clear();
        }
    }

    public MessageEditor getMessageEditor() {
        return messageEditor;
    }

    public String getAddresseeUserPhone() {
        return addresseePhone;
    }

    // Listen events from server with messages throw MessageBus
    @Override
    public void onMessage(MessageRequest messageRequest) {
        Message message = new Message(messageRequest, Message.ROUTE_IN);
        chatInteractor.insertMessage(message); // TODO сообщение не сохраняется в БД!
        if(messageRequest.sender.equals(addresseePhone))
            getViewState().displayMessage(message);
    }

    public void onDestroy() {
        super.onDestroy();
        messageBus.unsubscribe(this);
    }
}
