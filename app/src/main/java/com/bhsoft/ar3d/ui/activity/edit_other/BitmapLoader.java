package com.bhsoft.ar3d.ui.activity.edit_other;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import java.io.IOException;
import java.io.InputStream;


public class BitmapLoader {
    public static Bitmap load(Context context, int[] iArr, String str) {
        return null;
    }

    public static Bitmap loadFromAsset(Context context, int[] iArr, String str) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        int i = 1;
        if (Build.VERSION.SDK_INT < 11) {
            try {
                options.getClass().getField("inNativeAlloc").setBoolean(options, true);
            } catch (Exception unused) {
            }
        }
        options.inJustDecodeBounds = true;
        options.inScaled = false;
        AssetManager assets = context.getAssets();
        Bitmap bitmap = null;
        try {
            InputStream open = assets.open(str);
            BitmapFactory.decodeStream(open, null, options);
            open.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError unused2) {
        }
        int i2 = options.outWidth;
        int i3 = options.outHeight;
        int i4 = iArr[0];
        int i5 = iArr[1];
        if (i3 > i5 || i2 > i4) {
            int i6 = i2 / 2;
            int i7 = i3 / 2;
            while (i7 / i > i5 && i6 / i > i4) {
                i *= 2;
            }
        }
        options.inSampleSize = i;
        options.inJustDecodeBounds = false;
        try {
            InputStream open2 = assets.open(str);
            bitmap = BitmapFactory.decodeStream(open2, null, options);
            open2.close();
            return bitmap;
        } catch (IOException unused3) {
            return bitmap;
        } catch (OutOfMemoryError unused4) {
            return bitmap;
        }
    }

    public static Bitmap loadFromResource(Context context, int[] iArr, int i) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        int i2 = 1;
        if (Build.VERSION.SDK_INT < 11) {
            try {
                options.getClass().getField("inNativeAlloc").setBoolean(options, true);
            } catch (Exception unused) {
            }
        }
        options.inJustDecodeBounds = true;
        options.inScaled = false;
        try {
            BitmapFactory.decodeResource(context.getResources(), i, options);
        } catch (OutOfMemoryError unused2) {
        }
        int i3 = options.outWidth;
        int i4 = options.outHeight;
        int i5 = iArr[0];
        int i6 = iArr[1];
        if (i4 > i6 || i3 > i5) {
            int i7 = i3 / 2;
            int i8 = i4 / 2;
            while (i8 / i2 > i6 && i7 / i2 > i5) {
                i2 *= 2;
            }
        }
        options.inSampleSize = i2;
        options.inJustDecodeBounds = false;
        try {
            return BitmapFactory.decodeResource(context.getResources(), i, options);
        } catch (OutOfMemoryError unused3) {
            return null;
        }
    }

    public static Bitmap load(Context context, String str) throws Exception {
        BitmapFactory.Options options = new BitmapFactory.Options();
        if (Build.VERSION.SDK_INT < 11) {
            try {
                options.getClass().getField("inNativeAlloc").setBoolean(options, true);
            } catch (Exception unused) {
            }
        }
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(str, options);
        options.inSampleSize = (int) Math.pow(2.0d, Math.floor((double) (((((((float) options.outWidth) * ((float) options.outHeight)) * 4.0f) / 1024.0f) / 1024.0f) / MemoryManagement.free(context))));
        options.inJustDecodeBounds = false;
        return BitmapProcessing.modifyOrientation(BitmapFactory.decodeFile(str, options), str);
    }
}
