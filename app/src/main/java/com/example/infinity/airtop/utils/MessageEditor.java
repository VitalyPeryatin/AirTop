package com.example.infinity.airtop.utils;

import android.graphics.Bitmap;

import com.example.infinity.airtop.data.network.MessageRequest;

public class MessageEditor{
    private MessageRequest messageRequest;
    // Base info for message which saves after clearing message by method "clear()"
    private String addressId, senderId;

    private MessageEditor(){
        messageRequest = new MessageRequest();
    }

    public static MessageEditor edit(){
        return new MessageEditor();
    }

    public void addText(String text){
        messageRequest.setText(text);
    }

    public void addImage(Bitmap bitmap) {
        messageRequest.setImage(bitmap);
    }

    public void setAddressId(String id){
        addressId = id;
        messageRequest.setAddressee(id);
    }

    public void setSenderId(String id){
        senderId = id;
        messageRequest.setSender(id);
    }

    public boolean isNotEmptyMessage(){
        return messageRequest.getText() != null || messageRequest.getImage() != null;
    }

    public void clear(){
        messageRequest = new MessageRequest();
        setAddressId(addressId);
        setSenderId(senderId);
    }

    public MessageRequest getMessage() {
        return messageRequest;
    }
}