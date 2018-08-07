package com.example.infinity.airtop.data.network.response;

import android.graphics.Bitmap;

public class MessageResponse {
    private String text, fromId, toId, encodedImage;
    private String phone;
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

    public String getSender() {
        return fromId;
    }

}
