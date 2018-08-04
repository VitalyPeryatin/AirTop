package com.example.infinity.airtop.presentation.presenters.listeners;

import com.example.infinity.airtop.data.db.model.Message;
import com.example.infinity.airtop.data.network.MessageRequest;

public interface OnMessageListener {
    void onMessage(Message message);
}
