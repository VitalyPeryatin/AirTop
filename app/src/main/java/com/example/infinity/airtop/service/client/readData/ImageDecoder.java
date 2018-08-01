package com.example.infinity.airtop.service.client.readData;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.example.infinity.airtop.data.network.Message;

public class ImageDecoder implements IDecoder{
    @Override
    public void decode(Message msg) {
        byte[] decodedString = Base64.decode(msg.getEncodedImage(), Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        msg.setImage(decodedImage);
    }
}
