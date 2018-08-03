package com.example.infinity.airtop.data.db.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.example.infinity.airtop.data.network.MessageRequest;


/**
 * Class for constructing messages of various type
 * @autor infinity_coder
 * @version 1.0.0
 */
@Entity(foreignKeys = @ForeignKey(entity = Addressee.class, parentColumns = "phone", childColumns = "addressee_phone"))
public class Message {
    @PrimaryKey(autoGenerate = true)
    public long id;
    @ColumnInfo(name = "addressee_phone")
    public String addresseePhone;
    @ColumnInfo(name = "sender_phone")
    public String senderPhone;

    public String text;
    // public Bitmap image;

    public Message(){}

    public Message(MessageRequest messageRequest){
        addresseePhone = messageRequest.addressee;
        senderPhone = messageRequest.sender;
        text = messageRequest.text;
    }
}
