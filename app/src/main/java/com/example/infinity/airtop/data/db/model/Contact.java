package com.example.infinity.airtop.data.db.model;

import com.example.infinity.airtop.ui.contacts.ContactsRecyclerAdapter;

/**
 * POJO-class for {@linkplain ContactsRecyclerAdapter}
 * @author infinity_coder
 * @version 1.0.3
 */
public class Contact {
    public Addressee addressee;
    public String lastMessage;

    public Contact(Addressee addressee, String lastMessage) {
        this.addressee = addressee;
        this.lastMessage = lastMessage;
    }

    public Contact(){

    }
}
