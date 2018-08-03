package com.example.infinity.airtop.ui.chat;

import com.example.infinity.airtop.data.network.MessageRequest;
import com.example.infinity.airtop.presentation.presenters.listeners.OnMessageListener;

import java.util.ArrayList;

public class MessageListener{
    private ArrayList<OnMessageListener> messageListeners = new ArrayList<>();

    public void subscribe(OnMessageListener messageListener){
        messageListeners.add(messageListener);
    }

    public void unsubscribe(OnMessageListener messageListener){
        messageListeners.remove(messageListener);
    }

    public void onMessage(MessageRequest messageRequest){
        for (OnMessageListener messageListener : messageListeners) {
            messageListener.onMessage(messageRequest);
        }
    }
}