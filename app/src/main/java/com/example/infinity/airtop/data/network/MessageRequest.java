package com.example.infinity.airtop.data.network;

import android.graphics.Bitmap;

import com.example.infinity.airtop.data.db.model.Message;

/**
 * Class for constructing messages of various type
 * @autor infinity_coder
 * @version 1.0.0
 */
public class MessageRequest implements RequestModel{
    public String text, sender, addressee, encodedImage;
    public String phone;
    private final String TYPE = TYPE_MESSAGE;
    private Bitmap image;
    public MessageRequest(){}
    public MessageRequest(Message message){
        text = message.text;
        addressee = message.addresseePhone;
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
        return addressee;
    }

    public void setAddressee(String addressee) {
        this.addressee = addressee;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }


}
