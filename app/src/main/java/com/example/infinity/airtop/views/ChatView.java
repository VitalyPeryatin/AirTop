package com.example.infinity.airtop.views;

import com.arellomobile.mvp.MvpView;
import com.example.infinity.airtop.models.Message;

public interface ChatView extends MvpView {
    void displayMessage(Message message);
}
