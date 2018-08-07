package com.example.infinity.airtop.data.network.request;

import android.graphics.Bitmap;
import android.util.Base64;

import com.example.infinity.airtop.data.db.model.Message;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;

/**
 * Class for constructing messages of various type
 * @autor infinity_coder
 * @version 1.0.0
 */
public class MessageRequest implements RequestModel {
    public String text, fromId, toId, encodedImage;
    public String phone;
    private final String TYPE = TYPE_MESSAGE;
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
        String jsonMessage = new Gson().toJson(this);
        jsonMessage = jsonMessage.length() + "@" + jsonMessage;
        return jsonMessage;
    }
}
