package com.infinity_coder.infinity.airtop.ui.contacts;

import com.infinity_coder.infinity.airtop.data.db.model.Contact;

public interface OnContactListListener {
    void addContact(Contact contact);
    void removeContact(Contact contact);
}
