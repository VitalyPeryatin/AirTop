package com.example.infinity.airtop.data.network.response;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class MessageResponse {
    private String text, fromId, toId, encodedImage, phone;
    private Bitmap image;

    public String getText() {
        return text;
    }

    public Bitmap getImage() {
        return image;
    }

    public String getEncodedImage() {
        return encodedImage;
    }

    public String getAddressee() {
        return toId;
    }

    public String getFromId() {
        return fromId;
    }

    public String getToId() {
        return toId;
    }

    public void decode(){
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        image = decodedImage;
    }


}
