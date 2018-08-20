package com.infinity_coder.infinity.airtop.ui.chat;

import android.util.Log;

import com.infinity_coder.infinity.airtop.data.db.interactors.ChatInteractor;
import com.infinity_coder.infinity.airtop.data.db.model.Addressee;
import com.infinity_coder.infinity.airtop.data.db.model.Message;
import com.infinity_coder.infinity.airtop.data.network.response.AddressResponse;
import com.infinity_coder.infinity.airtop.service.notifications.MessageNotification;

/**
 * Transfer data(MessageRequest) between different objects and threads
 * @author infinity_coder
 * @version 1.0.2
 */
public class MessageBus {
    private OnMessageListener messageListener = null;
    private MessageNotification notification = new MessageNotification();

    public void subscribe(OnMessageListener messageListener){
        this.messageListener = messageListener;
    }

    public void unsubscribe(OnMessageListener messageListener){
        this.messageListener = null;
    }

    public void onMessage(Message message){
        if(messageListener != null)
            messageListener.onMessage(message);
        else
            notification.onMessage(message);
    }

    public void onAddresse(AddressResponse addressResponse){
        ChatInteractor chatInteractor = new ChatInteractor();
        Addressee addressee = addressResponse.getAddressee();
        Log.d("mLog2", "adressee: " + addressee);
        chatInteractor.insertAddressee(addressee);
    }
}