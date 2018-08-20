package com.infinity_coder.infinity.airtop.ui.chat;


import com.arellomobile.mvp.MvpView;
import com.infinity_coder.infinity.airtop.data.db.model.Message;

/**
 * Open access for presenter to "ChatActivity"
 * @author infinity_coder
 * @version 1.0.3
 */
public interface ChatView extends MvpView {
    void displayMessage(Message message);
}
