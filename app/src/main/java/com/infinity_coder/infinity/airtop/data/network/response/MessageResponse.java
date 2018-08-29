package com.infinity_coder.infinity.airtop.data.network.response;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.util.LruCache;
import android.util.Base64;
import android.util.Log;

import com.infinity_coder.infinity.airtop.App;
import com.infinity_coder.infinity.airtop.data.db.model.Message;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MessageResponse implements ResponseModel {
    private String text, fromId, toId, encodedImage, phone, imageName, imagePath, fromNickname;
    private Bitmap image;
    private static final String IMAGE_FOLDER = "Images";
    private static final String SEPARATOR = "/";

    public void decode(){
        if(encodedImage != null) {
            byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
            image = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imagePath = saveImageToFolder();
        }
    }

    public String getFromId() {
        return fromId;
    }

    private String saveImageToFolder() {
        try {
            File dir = new File(App.getInstance().getFilesDir(), IMAGE_FOLDER);
            File file = new File(App.getInstance().getFilesDir() + SEPARATOR + IMAGE_FOLDER, imageName);
            if (!dir.exists())
                dir.mkdirs();
            if (!file.exists())
                file.createNewFile();
            FileOutputStream stream = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            stream.close();
            return file.getAbsolutePath();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public String getFromNickname() {
        return fromNickname;
    }

    /**
     * MessageResponse transfer to Message for saving in DataBase.
     * If the message is sent by the user, then the sender = the addressee.
     * If the message is received by the user, then the sender and the addressee change places.
     * (The message is received by the addressee)
     */
    public Message toMessageModel(){
        decode();

        Message message = new Message();
        message.route = Message.ROUTE_IN;
        String aSender = fromId;
        message.senderId = toId;
        message.addressId = aSender;
        message.text = text;
        message.imageName = imageName;
        message.imagePath = imagePath;
        return message;
    }


}
