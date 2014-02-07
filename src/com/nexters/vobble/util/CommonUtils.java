package com.nexters.vobble.util;

import android.graphics.*;

public class CommonUtils {

    public static Bitmap getCroppedBitmap(Bitmap bitmap, int radius) {
        Bitmap scaledBitmap;

        if (bitmap.getWidth() != radius || bitmap.getHeight() != radius) {
            scaledBitmap = Bitmap.createScaledBitmap(bitmap, radius, radius, false);
        } else {
            scaledBitmap = bitmap;
        }

        Bitmap output = Bitmap.createBitmap(scaledBitmap.getWidth(), scaledBitmap.getHeight(), Bitmap.Config.ARGB_8888);

        final Paint paint = new Paint();

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        paint.setColor(Color.parseColor("#BAB399"));
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        final Rect rect = new Rect(0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight());

        Canvas canvas = new Canvas(output);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(scaledBitmap.getWidth() / 2 + 0.7f, scaledBitmap.getHeight() / 2 + 0.7f, scaledBitmap.getWidth() / 2 + 0.1f, paint);
        canvas.drawBitmap(scaledBitmap, rect, rect, paint);

        return output;
    }
}
