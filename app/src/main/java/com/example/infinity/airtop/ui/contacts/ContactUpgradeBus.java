package com.example.infinity.airtop.ui.contacts;

public class ContactUpgradeBus {
    private OnContactListListener contactListListener = null;

    public void subscribe(OnContactListListener contactListListener){
        this.contactListListener = contactListListener;
    }

    public void unsubscribe(){
        this.contactListListener = null;
    }

    public void onLoadContacts(){
        if(contactListListener != null)
            contactListListener.onLoadContacts();
    }

    public void onUpdateLastMessage(String uuid, String lastMessage){
        if(contactListListener != null)
            contactListListener.onUpdateLastMessage(uuid, lastMessage);
    }
}
