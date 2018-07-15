package com.example.infinity.airtop.model;

import android.graphics.Bitmap;

/**
 * Class for constructing messages of various type
 * @autor infinity_coder
 * @version 1.0.0
 */
public class Message{
    private String text, sender, addressee, encodedImage;
    private final String TYPE = "message";
    private Bitmap image;

    public String getType() {
        return TYPE;
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
}
