package com.example.infinity.airtop.ui.chat;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.infinity.airtop.data.db.interactors.ChatInteractor;
import com.example.infinity.airtop.data.db.model.Message;
import com.example.infinity.airtop.data.prefs.app.AppPreferencesHelper;
import com.example.infinity.airtop.di.components.ChatComponent;
import com.example.infinity.airtop.di.components.DaggerChatComponent;
import com.example.infinity.airtop.utils.JsonConverter;
import com.example.infinity.airtop.utils.MessageEditor;
import com.example.infinity.airtop.utils.serverWorker.LauncherServerSending;

import javax.inject.Inject;

/**
 * Presenter of "ChatActivity"
 * @author infinity_coder
 * @version 1.0.3
 */
@InjectViewState
public class ChatPresenter extends MvpPresenter<ChatView> implements OnMessageListener {

    @Inject
    public ChatInteractor chatInteractor;
    @Inject
    public LauncherServerSending serverSending;
    @Inject
    public MessageBus messageBus;
    @Inject
    public AppPreferencesHelper preferencesHelper;

    private String addressId;
    private MessageEditor messageEditor;

    public ChatPresenter(){
        ChatComponent chatComponent = DaggerChatComponent.create();
        chatComponent.inject(this);
        messageEditor = MessageEditor.edit();
    }

    public void onCreate(String addressId, String senderId){
        this.addressId = addressId;
        messageBus.subscribe(this);

        // Add to message the base data: "sender" and "addressee"
        messageEditor.setAddressId(addressId);
        messageEditor.setSenderId(senderId);
    }

    public int getAdapterPosition(String addresseePhone, int defaultPosition){
        int position = preferencesHelper.getAdapterPosition(addresseePhone);
        if(position == 0) position = defaultPosition;
        return position;
    }

    public void saveAdapterPosition(String addresseePhone, int position){
        if(position >= 0)
            preferencesHelper.saveAdapterPosition(addresseePhone, position);
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

    public MessageEditor getMessageEditor() {
        return messageEditor;
    }

    public String getNickname() {
        return chatInteractor.getNicknameById(addressId);
    }

    // Listen events from server with messages throw MessageBus
    @Override
    public void onMessage(Message message) {
        if(message.addressId.equals(addressId))
            getViewState().displayMessage(message);
    }

    public void onDestroy() {
        super.onDestroy();
        messageBus.unsubscribe(this);
    }
}
