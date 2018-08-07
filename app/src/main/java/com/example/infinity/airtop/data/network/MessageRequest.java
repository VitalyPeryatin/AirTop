package com.example.infinity.airtop.data.network;

import android.graphics.Bitmap;

import com.example.infinity.airtop.data.db.model.Message;

/**
 * Class for constructing messages of various type
 * @autor infinity_coder
 * @version 1.0.0
 */
public class MessageRequest implements RequestModel{
    public String text, fromId, toId, encodedImage;
    public String phone;
    private final String TYPE = TYPE_MESSAGE;
    private Bitmap image;
    public MessageRequest(){}
    public MessageRequest(Message message){
        text = message.text;
        toId = message.addressId;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getEncodedImage() {
        return encodedImage;
    }

    public void setEncodedImage(String encodedImage) {
        this.encodedImage = encodedImage;
    }

    public String getAddressee() {
        return toId;
    }

    public void setAddressee(String id) {
        this.toId = id;
    }

    public String getSender() {
        return fromId;
    }

    public void setSender(String id) {
        this.fromId = id;
    }


}
