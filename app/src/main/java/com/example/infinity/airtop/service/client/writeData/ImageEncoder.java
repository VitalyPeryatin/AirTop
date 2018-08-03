package com.example.infinity.airtop.service.client.writeData;

import android.graphics.Bitmap;
import android.util.Base64;

import com.example.infinity.airtop.data.network.MessageRequest;

import java.io.ByteArrayOutputStream;

/**
 * Class for encode Image to Base 64
 * @author infinity_coder
 * @version 1.0.2
 */
public class ImageEncoder implements IEncoder {
    @Override
    public void encode(MessageRequest msg) {
        Bitmap bm = msg.getImage();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] byteArrayImage = baos.toByteArray();
        String encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
        msg.setImage(null);
        msg.setEncodedImage(encodedImage);
    }
}
