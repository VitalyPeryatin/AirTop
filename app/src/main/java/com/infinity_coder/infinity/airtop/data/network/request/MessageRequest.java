package com.infinity_coder.infinity.airtop.data.network.request;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;

import com.infinity_coder.infinity.airtop.data.db.model.Message;
import com.infinity_coder.infinity.airtop.data.network.Image;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class MessageRequest extends RequestModel {
    private String text, fromId, toId, fromNickname;
    @Nullable
    private Image image;

    public MessageRequest(){}

    public void setText(String text) {
        this.text = text.trim();
    }

    public boolean isEmptyMessage(){
        return (text == null || text.length() == 0) && image == null;
    }

    public void setImage(Bitmap bitmap, String name) {
        image = new Image();
        this.image.setImage(bitmap, name);
    }

    public void setAddressee(String id) {
        exchangeUUID = id;
        this.toId = id;
    }

    public void setSender(String id) {
        this.fromId = id;
    }

    public void setFromNickname(String fromNickname) {
        this.fromNickname = fromNickname;
    }

    private void encode() {
        if(image != null) {
            image.encodeImage();
            image.saveImageToFolder();
        }
    }

    public Message toMessageModel(){
        Message message = new Message();
        message.text = text;
        message.addressId = toId;
        message.senderId = fromId;
        message.route = Message.ROUTE_OUT;
        if(image != null) {
            message.setImage(image);
        }
        return message;
    }

    @Override
    public String toJson(){
        ExecutorService executor = Executors.newCachedThreadPool();
        Future<String> future = executor.submit(() -> {
            encode();
            return MessageRequest.super.toJson();
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) { e.printStackTrace(); }
        return null;
    }
}
