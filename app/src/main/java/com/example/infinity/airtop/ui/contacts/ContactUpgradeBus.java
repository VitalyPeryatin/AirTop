package com.example.infinity.airtop.ui.contacts;

import android.app.Activity;

import com.example.infinity.airtop.data.db.model.Contact;

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

    public void onUpdateLastMessage(String uuid, Contact contact){
        if(contactListListener != null)
            activity.runOnUiThread(()-> contactListListener.onUpdateContact(uuid, contact));
    }

    public void onUpdateLastMessage(String uuid, String lastMessage){
        if(contactListListener != null)
            activity.runOnUiThread(()-> contactListListener.onUpdateContact(uuid, lastMessage));
    }
}
