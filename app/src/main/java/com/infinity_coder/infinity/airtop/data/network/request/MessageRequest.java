package com.infinity_coder.infinity.airtop.data.network.request;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Base64;

import com.infinity_coder.infinity.airtop.App;
import com.infinity_coder.infinity.airtop.data.db.model.Message;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;


public class MessageRequest extends RequestModel {
    private String text, fromId, toId, encodedImage, imageName, imagePath;
    private Bitmap image;
    private static final String IMAGE_FOLDER = "MyApplication/Images";
    private static final String SEPARATOR = "/";

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
        imagePath = Environment.getExternalStorageState() + SEPARATOR + IMAGE_FOLDER + SEPARATOR + imageName;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        imagePath = saveImageToFolder();
        this.encodedImage = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
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
