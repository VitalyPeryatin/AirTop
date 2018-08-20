package com.infinity_coder.infinity.airtop.ui.contacts;

import com.infinity_coder.infinity.airtop.data.db.model.Contact;

public interface OnContactListListener {
    void onUpdateContact(String uuid, Contact contact);
    void onUpdateContact(String uuid, String lastMessage);
}
