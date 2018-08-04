package com.example.infinity.airtop.data.db.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.example.infinity.airtop.data.network.MessageRequest;


/**
 * Class for constructing messages of various type
 * @autor infinity_coder
 * @version 1.0.2
 */
@Entity(foreignKeys = @ForeignKey(entity = Addressee.class, parentColumns = "phone", childColumns = "addressee_phone"))
public class Message {
    public static final String ROUTE_IN = "in", ROUTE_OUT = "out";
    @PrimaryKey(autoGenerate = true)
    public long id;
    @ColumnInfo(name = "addressee_phone")
    public String addresseePhone;
    @ColumnInfo(name = "sender_phone")
    public String senderPhone;
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
    public Message(MessageRequest messageRequest, String route){
        this.route = route;

        if(route.equals(ROUTE_OUT)) {
            senderPhone = messageRequest.addressee;
            addresseePhone = senderPhone;
        }
        else {
            String aSender = messageRequest.sender;
            senderPhone = messageRequest.addressee;
            addresseePhone = aSender;
        }
        text = messageRequest.text;
    }
}
