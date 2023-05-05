package com.bhsoft.ar3d.ui.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import androidx.core.content.FileProvider;

import com.bhsoft.ar3d.R;

import java.io.File;
import java.io.IOException;


public class Utility {

    public static Bitmap easer_bitmap = null;
    public static Bitmap gallery_bitmap = null;
    public static File shareFile = null;
    public static Bitmap share_ = null;


    public static boolean isNetworkAvailable(Context context) {
        NetworkInfo[] allNetworkInfo;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (!(connectivityManager == null || (allNetworkInfo = connectivityManager.getAllNetworkInfo()) == null)) {
            for (int i = 0; i < allNetworkInfo.length; i++) {
                Log.w("INTERNET:", String.valueOf(i));
                if (allNetworkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                    Log.w("INTERNET:", "connected!");
                    return true;
                }
            }
        }
        return false;
    }

    public static Bitmap getBitmapFromAsset(Context context, String str) {
        try {
            return BitmapFactory.decodeStream(context.getAssets().open(str));
        } catch (IOException unused) {
            return null;
        }
    }


    public static void shareApppp(Activity activity) {
        try {
            Intent intent = new Intent("android.intent.action.SEND");
            intent.putExtra("android.intent.extra.TEXT", "" + getShareText(activity, ""));
            intent.setType("text/plain");
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            activity.startActivityForResult(Intent.createChooser(intent, "Share image by..."), 108);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void shareImageDialog(Activity activity, String str, String str2, String str3) {
        try {
            File file = new File(str);
            Uri uriForFile = FileProvider.getUriForFile(activity, activity.getApplicationContext().getPackageName() + ".provider", file);
            Intent intent = new Intent("android.intent.action.SEND");
            intent.putExtra("android.intent.extra.STREAM", uriForFile);
            intent.putExtra("android.intent.extra.TEXT", "" + getShareText(activity, str2));
            intent.putExtra("android.intent.extra.SUBJECT", "" + activity.getString(R.string.app_name));
            intent.setType("image/*");
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            filterByPackageName(activity, intent, str3);
            activity.startActivityForResult(Intent.createChooser(intent, "Share image by..."), 108);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void filterByPackageName(Context context, Intent intent, String str) {
        for (ResolveInfo resolveInfo : context.getPackageManager().queryIntentActivities(intent, 0)) {
            Log.e("packageName:", "" + resolveInfo.activityInfo.packageName.toLowerCase());
            if (resolveInfo.activityInfo.packageName.toLowerCase().startsWith(str)) {
                intent.setPackage(resolveInfo.activityInfo.packageName);
                return;
            }
        }
    }

    public static int random(int i, int i2) {
        double d = (double) i;
        double random = Math.random();
        double d2 = (double) ((i2 - i) + 1);
        Double.isNaN(d2);
        Double.isNaN(d);
        int round = Math.round((float) (d + (random * d2)));
        return round >= i2 ? i2 : round;
    }

    public static String getShareText(Context context, String str) {
        return getRandomStartText() + " Application \n https://play.google.com/store/apps/details?id=" + context.getPackageName();
    }

    public static String getRandomStartText() {
        try {
            String[] strArr = {"A beautiful", "Cool", "An Awesome", "The best"};
            return strArr[random(0, strArr.length - 1)];
        } catch (Exception e) {
            e.printStackTrace();
            return "A beautiful";
        }
    }
}
