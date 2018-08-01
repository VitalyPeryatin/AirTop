package com.example.infinity.airtop.ui.chat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.infinity.airtop.data.db.model.Addressee;
import com.example.infinity.airtop.data.network.Message;
import com.example.infinity.airtop.data.db.repositoryDao.AddresseeDao;
import com.example.infinity.airtop.presentation.presenters.listeners.OnMessageListener;
import com.example.infinity.airtop.service.ClientService;
import com.example.infinity.airtop.service.client.JsonConverter;
import com.example.infinity.airtop.App;

@InjectViewState
public class ChatPresenter extends MvpPresenter<ChatView> implements OnMessageListener {

    private Message message;
    private Addressee addressee;
    private Context context;

    public ChatPresenter(){
        message = new Message();
    }

    public void onCreate(Context context, Intent intent){
        this.context = context;
        App.getInstance().getListeners().getMessageListener().subscribe(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                AddresseeDao addresseeDao = App.getInstance().getDatabase().addresseeDao();
                addressee = addresseeDao.getByPhone(intent.getStringExtra("addresseePhone"));

            }
        }).start();
        message = new Message();

    }

    public void sendMessage() {
        if(message.getText() != null || message.getImage() != null) {
            message.setAddressee(addressee.phone);
            message.setSender(App.getInstance().getCurrentUser().phone);
            JsonConverter jsonConverter = new JsonConverter();
            String json = jsonConverter.toJson(message);
            Intent intent = new Intent(context, ClientService.class);
            intent.putExtra("request", json);
            context.startService(intent);
            //App.getInstance().getBackendClient().sendRequest(message);
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
        getViewState().displayMessage(message);
    }

    public String getAddresseeUserPhone() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return addressee.phone;
    }


    @Override
    public void onMessage(Message message) {
        displayMessage(message);
    }

    public void onDestroy() {
        super.onDestroy();
        App.getInstance().getListeners().getMessageListener().unsubscribe(this);

    }
}
