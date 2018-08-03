package com.example.infinity.airtop.utils;

import android.graphics.Bitmap;

import com.example.infinity.airtop.data.network.MessageRequest;

public class MessageEditor{
    private MessageRequest messageRequest;
    // Base info for message which saves after clearing message by method "clear()"
    private String addressPhone, sendPhone;

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

    public void addAddressPhone(String phone){
        addressPhone = phone;
        messageRequest.setAddressee(phone);
    }

    public void addSendPhone(String phone){
        sendPhone = phone;
        messageRequest.setSender(phone);
    }

    public boolean isNotEmptyMessage(){
        return messageRequest.getText() != null || messageRequest.getImage() != null;
    }

    public void clear(){
        messageRequest = new MessageRequest();
        addAddressPhone(addressPhone);
        addSendPhone(sendPhone);
    }

    public void addMessage(MessageRequest messageRequest){
        this.messageRequest.setSender(messageRequest.sender);
        this.messageRequest.setAddressee(messageRequest.addressee);
        this.messageRequest.setText(messageRequest.text);
    }

    public MessageRequest getMessage() {
        return messageRequest;
    }
}