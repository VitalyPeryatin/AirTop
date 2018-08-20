package com.infinity_coder.infinity.airtop.data.network.request;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import com.infinity_coder.infinity.airtop.App;
import com.infinity_coder.infinity.airtop.data.db.model.Message;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class MessageRequest extends RequestModel {
    private String text, fromId, toId, encodedImage, imageName, imagePath;
    private Bitmap image;

    public MessageRequest(){}

    public void setText(String text) {
        this.text = text.trim();
    }

    public boolean isEmptyMessage(){
        return text == null && image == null;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public void setAddressee(String id) {
        exchangeUUID = id;
        this.toId = id;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public void setSender(String id) {
        this.fromId = id;
    }

    private void encode() {
        if(image != null)
            encodeImage();
    }

    private void encodeImage(){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        try {
            imagePath = saveImageToFolder();
        } catch (IOException e) { e.printStackTrace(); }
        this.encodedImage = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
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

    public Message toMessageModel(){
        encode();

        Message message = new Message();
        message.text = text;
        message.addressId = toId;
        message.senderId = fromId;
        message.route = Message.ROUTE_OUT;
        message.imageName = imageName;
        message.imagePath = imagePath;
        return message;
    }

    @Override
    public String toJson(){
        encode();
        return super.toJson();
    }
}
