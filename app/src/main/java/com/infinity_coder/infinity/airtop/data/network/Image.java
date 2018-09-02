package com.infinity_coder.infinity.airtop.data.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.DisplayMetrics;

import com.infinity_coder.infinity.airtop.App;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class Image {
    private String encodedImage, imageName;
    private int width, height;
    private Bitmap bitmap;

    public void saveImageToFolder() {
        try {
            File dir = new File(App.imagePath);
            File file = new File(App.imagePath, imageName);
            if (!dir.exists())
                dir.mkdirs();
            if (!file.exists())
                file.createNewFile();
            FileOutputStream stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            stream.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private int convertPxToDp(int px){
        DisplayMetrics metrics = App.getInstance().getBaseContext().getResources().getDisplayMetrics();
        return px / (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public void encodeImage(){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        this.bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        this.encodedImage = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
    }

    public void decodeImage() {
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        this.bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }


    public void setImage(Bitmap bitmap, String name) {
        this.imageName = name;
        this.bitmap = bitmap;
        width = convertPxToDp(bitmap.getWidth());
        height = convertPxToDp(bitmap.getHeight());
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getName() {
        return imageName;
    }

    public void setName(String imageName) {
        this.imageName = imageName;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
