package com.example.infinity.airtop.services.client.writeData;

import android.graphics.Bitmap;
import android.util.Base64;

import com.example.infinity.airtop.models.Message;

import java.io.ByteArrayOutputStream;

public class ImageEncoder implements IEncoder {
    @Override
    public void encode(Message msg) {
        Bitmap bm = msg.getImage();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] byteArrayImage = baos.toByteArray();
        String encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
        msg.setImage(null);
        msg.setEncodedImage(encodedImage);
    }
}
