package com.example.infinity.airtop.ui.contacts;

import com.example.infinity.airtop.data.db.model.Contact;

public interface OnContactListListener {
    void onUpdateContact(String uuid, Contact contact);
    void onUpdateContact(String uuid, String lastMessage);
}
