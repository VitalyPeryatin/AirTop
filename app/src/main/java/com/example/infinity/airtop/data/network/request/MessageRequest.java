package com.example.infinity.airtop.data.network.request;

import android.graphics.Bitmap;
import android.util.Base64;

import com.example.infinity.airtop.data.db.model.Message;

import java.io.ByteArrayOutputStream;


public class MessageRequest extends RequestModel {
    private String text, fromId, toId, encodedImage;
    private Bitmap image;

    public MessageRequest(){}

    public void setText(String text) {
        this.text = text.trim();
    }

    public boolean isEmptyMessage(){
        return text == null && image == null;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public void setAddressee(String id) {
        exchangeUUID = id;
        this.toId = id;
    }

    public void setSender(String id) {
        this.fromId = id;
    }

    private void encode() {
        if(image != null)
            encodeImage();
    }

    private void encodeImage(){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] byteArrayImage = baos.toByteArray();
        String encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
        image = null;
        this.encodedImage = encodedImage;
    }

    public Message toMessageModel(){
        Message message = new Message();
        message.text = text;
        message.addressId = toId;
        message.senderId = fromId;
        message.route = Message.ROUTE_OUT;
        return message;
    }

    @Override
    public String toJson(){
        encode();
        return super.toJson();
    }
}
