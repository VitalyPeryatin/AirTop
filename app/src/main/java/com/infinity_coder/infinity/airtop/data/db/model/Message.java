package com.infinity_coder.infinity.airtop.data.db.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = Contact.class, parentColumns = "uuid", childColumns = "addressId"),
        indices = @Index("addressId"))
public class Message {
    public static final String ROUTE_IN = "in", ROUTE_OUT = "out";
    @PrimaryKey(autoGenerate = true)
    public long id;
    public String senderId;
    public String addressId;
    public String route;

    public String text;
    public String imageName;
    public String imagePath;
}
