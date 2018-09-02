package com.infinity_coder.infinity.airtop.data.network.response;

import android.support.annotation.Nullable;

import com.infinity_coder.infinity.airtop.data.db.model.Message;
import com.infinity_coder.infinity.airtop.data.network.Image;

public class MessageResponse implements ResponseModel {
    private String text, fromId, toId, fromNickname;
    @Nullable
    private Image image;

    private void decode(){
        if(image != null) {
            image.decodeImage();
            image.saveImageToFolder();
        }
    }

    public String getFromId() {
        return fromId;
    }

    public String getFromNickname() {
        return fromNickname;
    }

    /**
     * MessageResponse transfer to Message for saving in DataBase.
     * If the message is sent by the user, then the sender = the addressee.
     * If the message is received by the user, then the sender and the addressee change places.
     * (The message is received by the addressee)
     */
    public Message toMessageModel(){
        decode();

        Message message = new Message();
        message.route = Message.ROUTE_IN;
        String aSender = fromId;
        message.senderId = toId;
        message.addressId = aSender;
        message.text = text;
        if(image != null) {
            message.setImage(image);
        }
        return message;
    }


}
