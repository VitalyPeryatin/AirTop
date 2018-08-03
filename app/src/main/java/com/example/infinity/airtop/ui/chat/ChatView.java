package com.example.infinity.airtop.ui.chat;


import com.arellomobile.mvp.MvpView;
import com.example.infinity.airtop.data.db.model.Message;

public interface ChatView extends MvpView {
    void displayMessage(Message message);
}
