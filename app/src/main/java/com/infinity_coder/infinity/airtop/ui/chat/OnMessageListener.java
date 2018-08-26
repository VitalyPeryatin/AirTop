package com.infinity_coder.infinity.airtop.ui.chat;

import com.infinity_coder.infinity.airtop.data.db.model.Message;

/**
 * Listener incoming messages from the "DataReader"
 * @author infinity_coder
 * @version 1.0.3
 */
public interface OnMessageListener {
    void onMessage(String nickname, Message message);
}
