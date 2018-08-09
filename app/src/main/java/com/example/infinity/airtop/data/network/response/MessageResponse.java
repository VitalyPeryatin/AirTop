package com.example.infinity.airtop.data.network.response;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.example.infinity.airtop.data.db.model.Message;

public class MessageResponse {
    private String text, fromId, toId, encodedImage, phone;
    private Bitmap image;

    public void decode(){
        if(encodedImage != null) {
            byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
            image = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        }
    }

    /**
     * MessageResponse transfer to Message for saving in DataBase.
     * If the message is sent by the user, then the sender = the addressee.
     * If the message is received by the user, then the sender and the addressee change places.
     * (The message is received by the addressee)
     */
    public Message toMessageModel(){
        Message message = new Message();
        message.route = Message.ROUTE_IN;
        String aSender = fromId;
        message.senderId = toId;
        message.addressId = aSender;
        message.text = text;
        return message;
    }


}
