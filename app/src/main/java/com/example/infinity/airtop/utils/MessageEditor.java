package com.example.infinity.airtop.utils;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.example.infinity.airtop.data.network.request.MessageRequest;

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

    public void addText(@NonNull String text){
        messageRequest.setText(text);
    }

    public void addImage(@NonNull Bitmap bitmap) {
        messageRequest.setImage(bitmap);
    }

    public void setAddressId(@NonNull String id){
        addressId = id;
        messageRequest.setAddressee(id);
    }

    public void setSenderId(@NonNull String id){
        senderId = id;
        messageRequest.setSender(id);
    }

    public boolean isNotEmptyMessage(){
        return !messageRequest.isEmptyMessage();
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