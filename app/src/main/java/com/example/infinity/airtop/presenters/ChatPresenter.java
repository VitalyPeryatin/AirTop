package com.example.infinity.airtop.presenters;

import android.graphics.Bitmap;
import android.util.Log;

import com.example.infinity.airtop.models.Message;
import com.example.infinity.airtop.models.User;
import com.example.infinity.airtop.views.App;
import com.example.infinity.airtop.views.ChatActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class ChatPresenter implements Presenter<ChatActivity> {

    private static ChatPresenter chatPresenter;
    private ChatActivity activity;
    private int userLocalId = 0;

    private Message message;

    private User addresseeUser;

    private ChatPresenter(){
        message = new Message();
    }

    public static ChatPresenter getInstance() {
        if(chatPresenter == null) chatPresenter = new ChatPresenter();
        Log.d("mLog", "Presenter: " + chatPresenter + ", client: " + App.getBackendClient());
        return chatPresenter;
    }

    public void sendMessage() {
        if(message.getText() != null || message.getImage() != null) {
            message.setAddressee(addresseeUser.phone);
            message.setSender(App.getInstance().getCurrentUser().phone);
            App.getBackendClient().sendRequest(message);
            message = new Message();
        }
    }


    public void closeConnection() {
        /*if(backendClient != null) {
            backendClient.closeConnection();
            backendClient = null;
        }*/
    }

    public void addTextToMsg(String text){
        message.setText(text);
    }

    public void addImageToMsg(Bitmap bitmap) {
        message.setImage(bitmap);
    }

    public void displayMessage(Message message){
        activity.displayMessage(message);
    }

    public User getAddresseeUser() {
        return addresseeUser;
    }

    public void setAddresseeUser(User addresseeUser) {
        this.addresseeUser = addresseeUser;
    }

    @Override
    public void attachActivity(ChatActivity activity) {
        this.activity = activity;
    }

    @Override
    public void detachActivity() {
        activity = null;
    }

}
