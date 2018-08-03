package com.example.infinity.airtop.ui.chat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.infinity.airtop.data.db.interactors.ChatInteractor;
import com.example.infinity.airtop.data.db.model.Addressee;
import com.example.infinity.airtop.data.network.MessageRequest;
import com.example.infinity.airtop.presentation.presenters.listeners.OnMessageListener;
import com.example.infinity.airtop.service.SocketService;
import com.example.infinity.airtop.utils.JsonConverter;
import com.example.infinity.airtop.App;

@InjectViewState
public class ChatPresenter extends MvpPresenter<ChatView> implements OnMessageListener {

    private MessageRequest messageRequest;
    private Addressee addressee;
    private ChatInteractor chatInteractor;
    private Context context;

    public ChatPresenter(){
        messageRequest = new MessageRequest();
        chatInteractor = new ChatInteractor();
    }

    public void onCreate(Intent intent){
        this.context = App.getInstance().getBaseContext();
        App.getInstance().getResponseListeners().getMessageListener().subscribe(this);
        addressee = chatInteractor.getAddresseeByPhone(intent.getStringExtra("addresseePhone"));
        messageRequest = new MessageRequest();
    }

    public void sendMessage() {
        if(messageRequest.getText() != null || messageRequest.getImage() != null) {
            messageRequest.setAddressee(addressee.phone);
            messageRequest.setSender(App.getInstance().getCurrentUser().phone);
            JsonConverter jsonConverter = new JsonConverter();
            String json = jsonConverter.toJson(messageRequest);
            Intent intent = new Intent(context, SocketService.class);
            intent.putExtra("request", json);
            context.startService(intent);
            messageRequest = new MessageRequest();
        }
    }

    public void addTextToMsg(String text){
        messageRequest.setText(text);
    }

    public void addImageToMsg(Bitmap bitmap) {
        messageRequest.setImage(bitmap);
    }

    public void displayMessage(MessageRequest messageRequest){
        chatInteractor.insertMessage(messageRequest);
        if(messageRequest.sender.equals(addressee.phone)) {
            getViewState().displayMessage(messageRequest);
        }
    }

    public String getAddresseeUserPhone() {
        return addressee.phone;
    }


    @Override
    public void onMessage(MessageRequest messageRequest) {
        displayMessage(messageRequest);
    }

    public void onDestroy() {
        super.onDestroy();
        App.getInstance().getResponseListeners().getMessageListener().unsubscribe(this);

    }
}
