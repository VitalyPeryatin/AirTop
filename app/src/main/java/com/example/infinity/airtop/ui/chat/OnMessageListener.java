package com.example.infinity.airtop.ui.chat;

import com.example.infinity.airtop.data.db.model.Message;

/**
 * Listener incoming messages from the "DataReader"
 * @author infinity_coder
 * @version 1.0.3
 */
public interface OnMessageListener {
    void onMessage(Message message);
}
