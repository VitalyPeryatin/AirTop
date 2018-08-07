package com.example.infinity.airtop.data.db.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.example.infinity.airtop.data.network.request.MessageRequest;
import com.example.infinity.airtop.data.network.response.MessageResponse;


@Entity(foreignKeys = @ForeignKey(entity = Addressee.class, parentColumns = "uuid", childColumns = "addressId"),
        indices = @Index("addressId"))
public class Message {
    public static final String ROUTE_IN = "in", ROUTE_OUT = "out";
    @PrimaryKey(autoGenerate = true)
    public long id;
    public String senderId;
    public String addressId;
    public String route;

    public String text;
    // public Bitmap image;

    public Message(){}

    /**
     * MessageRequest transfer to Message for saving in DataBase.
     * If the message is sent by the user, then the sender = the addressee.
     * If the message is received by the user, then the sender and the addressee change places.
     * (The message is received by the addressee)
     * @param route indicates a message: sent or received
     */
    public Message(MessageResponse messageResponse, String route){
        this.route = route;

        if(route.equals(ROUTE_OUT)) {
            senderId = messageResponse.getToId();
            addressId = senderId;
        }
        else {
            String aSender = messageResponse.getFromId();
            senderId = messageResponse.getToId();
            addressId = aSender;
        }
        text = messageResponse.getText();
    }
}
