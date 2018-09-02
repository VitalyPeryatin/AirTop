package com.infinity_coder.infinity.airtop.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.DisplayMetrics;

import com.infinity_coder.infinity.airtop.App;
import com.squareup.picasso.Transformation;

public class RoundedTransformation implements Transformation {
    private final int radius;

    public RoundedTransformation(final int radius) {
        this.radius = radius;
    }

    @Override
    public Bitmap transform(final Bitmap source) {
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));

        Bitmap output = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        canvas.drawRoundRect(new RectF(0, 0, source.getWidth(), source.getHeight()), radius, radius, paint);

        if (source != output) {
            source.recycle();
        }

        return output;
    }

    private int convertDpToPx(int dp){
        DisplayMetrics metrics = App.getInstance().getBaseContext().getResources().getDisplayMetrics();
        return dp * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    @Override
    public String key() {
        return "Radius = " + radius;
    }
}
