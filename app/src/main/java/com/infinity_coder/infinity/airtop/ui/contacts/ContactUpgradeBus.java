package com.infinity_coder.infinity.airtop.ui.contacts;

import android.app.Activity;

import com.infinity_coder.infinity.airtop.data.db.model.Contact;

public class ContactUpgradeBus {
    private OnContactListListener contactListListener = null;
    private Activity activity;

    public void subscribe(Activity activity, OnContactListListener contactListListener){
        this.activity = activity;
        this.contactListListener = contactListListener;
    }

    public void unsubscribe(){
        this.contactListListener = null;
    }

    public void addAddressee(Contact contact){
        activity.runOnUiThread(()->{
            contactListListener.addContact(contact);
        });
    }

    public void removeAddressee(Contact contact){
        activity.runOnUiThread(()->contactListListener.removeContact(contact));
    }
}
