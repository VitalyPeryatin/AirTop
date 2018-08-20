package com.infinity_coder.infinity.airtop.utils;

import android.graphics.Bitmap;


import com.infinity_coder.infinity.airtop.data.network.request.MessageRequest;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

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

    public void addText(@NotNull String text){
        messageRequest.setText(text);
    }

    public void addImage(@NotNull Bitmap bitmap) {
        messageRequest.setImage(bitmap);
        Random random = new Random();
        messageRequest.setImageName(random.nextInt(2_147_000_000) + ".jpeg");
    }

    public void setAddressId(@NotNull String id){
        addressId = id;
        messageRequest.setAddressee(id);
    }

    public void setSenderId(@NotNull String id){
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