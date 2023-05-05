package com.bhsoft.ar3d.ui.activity.edit_other;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.view.View;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Util {
    public static String ALBUM = "GalaxyOverlay";
    public static String CROPPED_IMAGE_NAME = "CropImage.jpg";
    public static String DEV_ID = "6926065835086467761";
    public static String FONT_MAIN = "fonts/Comfortaa-Bold.ttf";
    public static boolean IS_DEBUG = false;
    public static boolean IS_DISPLAY_ADS = true;
    public static int KIND_REQUEST = 1;

    public static String md5_Hash(String str) {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            messageDigest = null;
        }
        messageDigest.update(str.getBytes(), 0, str.length());
        return new BigInteger(1, messageDigest.digest()).toString(16);
    }

    public static String getDeviceID(Context context) {
        return md5_Hash(Settings.Secure.getString(context.getContentResolver(), "android_id")).toUpperCase();
    }

    public static int getInputTypeForNoSuggestsInput() {
        return Build.MANUFACTURER.equalsIgnoreCase("Samsung") ? 524432 : 524288;
    }

    public static int getRelativeLeft(View view) {
        if (view.getParent() == view.getRootView()) {
            return view.getLeft();
        }
        return getRelativeLeft((View) view.getParent()) + view.getLeft();
    }

    public static int getRelativeTop(View view) {
        if (view.getParent() == view.getRootView()) {
            return view.getTop();
        }
        return getRelativeTop((View) view.getParent()) + view.getTop();
    }
}
