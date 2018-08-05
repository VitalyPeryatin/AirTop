package com.example.infinity.airtop.ui.chat;

import com.example.infinity.airtop.data.db.model.Message;

import java.util.ArrayList;

/**
 * Transfer data(MessageRequest) between different objects and threads
 * @author infinity_coder
 * @version 1.0.2
 */
public class MessageBus {
    private ArrayList<OnMessageListener> messageListeners = new ArrayList<>();

    public void subscribe(OnMessageListener messageListener){
        messageListeners.add(messageListener);
    }

    public void unsubscribe(OnMessageListener messageListener){
        messageListeners.remove(messageListener);
    }

    public void onMessage(Message message){
        for (OnMessageListener messageListener : messageListeners) {
            messageListener.onMessage(message);
        }
    }
}