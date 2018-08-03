package com.example.infinity.airtop.service.client.readData;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.example.infinity.airtop.data.network.MessageRequest;

/**
 * Class for decode Base64 to Image
 * @author infinity_coder
 * @version 1.0.2
 */
public class ImageDecoder implements IDecoder{
    @Override
    public void decode(MessageRequest msg) {
        byte[] decodedString = Base64.decode(msg.getEncodedImage(), Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        msg.setImage(decodedImage);
    }
}
