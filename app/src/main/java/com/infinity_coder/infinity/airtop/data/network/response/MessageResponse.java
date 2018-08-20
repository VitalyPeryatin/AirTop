package com.infinity_coder.infinity.airtop.data.network.response;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;

import com.infinity_coder.infinity.airtop.App;
import com.infinity_coder.infinity.airtop.data.db.model.Message;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MessageResponse implements ResponseModel {
    private String text, fromId, toId, encodedImage, phone, imageName, imagePath;
    private Bitmap image;

    public void decode(){
        if(encodedImage != null) {
            byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
            image = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            try {
                imagePath = saveImageToFolder();
            } catch (IOException e) { e.printStackTrace(); }
        }
    }

    private String saveImageToFolder() throws IOException {
        if(App.getInstance().hasReadAndWriteFilePermission()) {
            String path = "MyApplication/Images";
            File dir = new File(Environment.getExternalStorageDirectory(), "MyApplication/Images");
            File file = new File(Environment.getExternalStorageDirectory() + "/" + path, imageName);

            Log.d("mLog", "Path: " + file.getAbsolutePath());
            if (!dir.exists())
                dir.mkdirs();
            if (!file.exists())
                file.createNewFile();
            FileOutputStream stream = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            stream.close();
            return file.getAbsolutePath();
        }
        return null;
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
