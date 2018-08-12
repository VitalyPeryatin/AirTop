package com.example.infinity.airtop.ui.contacts;

public interface OnContactListListener {
    void onLoadContacts();
    void onUpdateLastMessage(String uuid, String lastMessage);
}
