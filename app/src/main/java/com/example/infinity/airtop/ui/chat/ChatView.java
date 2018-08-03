package com.example.infinity.airtop.ui.chat;


import com.arellomobile.mvp.MvpView;
import com.example.infinity.airtop.data.network.MessageRequest;

public interface ChatView extends MvpView {
    void displayMessage(MessageRequest messageRequest);
}
