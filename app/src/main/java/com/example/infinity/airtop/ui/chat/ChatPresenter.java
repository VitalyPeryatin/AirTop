package com.example.infinity.airtop.ui.chat;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.infinity.airtop.data.db.interactors.ChatInteractor;
import com.example.infinity.airtop.data.db.model.Message;
import com.example.infinity.airtop.data.network.request.MessageRequest;
import com.example.infinity.airtop.data.prefs.app.AppPreference;
import com.example.infinity.airtop.utils.MessageEditor;
import com.example.infinity.airtop.utils.ServerPostman;

import javax.inject.Inject;

/**
 * Presenter of "ChatActivity"
 * @author infinity_coder
 * @version 1.0.3
 */
@InjectViewState
public class ChatPresenter extends MvpPresenter<ChatView> implements OnMessageListener {

    private ChatInteractor chatInteractor;
    private ServerPostman serverPostman;
    private MessageBus messageBus;
    private AppPreference preferencesHelper;
    private MessageEditor messageEditor;
    private String addressId;

    @Inject
    public ChatPresenter(ChatInteractor chatInteractor,
                         ServerPostman serverPostman,
                         MessageBus messageBus,
                         MessageEditor messageEditor,
                         AppPreference preferencesHelper){
        this.chatInteractor = chatInteractor;
        this.messageBus = messageBus;
        this.messageEditor = messageEditor;
        this.preferencesHelper = preferencesHelper;
        this.serverPostman = serverPostman;
    }

    public void onCreate(@NonNull String addressId, @NonNull String senderId){
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

    public void addTextToMessage(@NonNull String text){
        if(text.length() > 0)
            messageEditor.addText(text);
    }

    public void saveAdapterPosition(String addresseePhone, int position){
        if(position >= 0)
            preferencesHelper.saveAdapterPosition(addresseePhone, position);
    }

    public void sendMessage() {
        if(messageEditor.isNotEmptyMessage()) {
            // Save message in DB and display the message

            MessageRequest messageRequest = messageEditor.getMessage();
            Message message = messageRequest.toMessageModel();
            chatInteractor.insertMessage(message);
            getViewState().displayMessage(message);
            messageEditor.clear();

            serverPostman.postRequest(messageRequest);
        }
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
