package com.bhsoft.ar3d.ui.activity.gallery_edit;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class ImageManager {
    public static HashMap<String, ArrayList<String>> getAllImageFromDevices(Context context) {
        HashMap<String, ArrayList<String>> folderItem = new HashMap<>();
        folderItem.put("All", null);
        Uri collection;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        } else {
            collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }
        String[] projection = new String[]{MediaStore.Images.Media.DATA};

        String sortOrder = MediaStore.Images.Media.DATE_MODIFIED + " DESC";
        Cursor cursor = context.getContentResolver().query(collection, projection, null, null, sortOrder);
        int dem = 0;
        if (cursor != null) {
            while (cursor.moveToNext()) {
                File file;
                String path = cursor.getString(0);
                try {
                    file = new File(path);
                    if (file.exists()) {
                        if (folderItem.containsKey(Objects.requireNonNull(file.getParent()).substring(file.getParent().lastIndexOf("/") + 1))) {
                            Objects.requireNonNull(folderItem.get(file.getParent().substring(file.getParent().lastIndexOf("/") + 1))).add(path);
                        } else {
                            ArrayList<String> list = new ArrayList<>();
                            list.add(path);
                            folderItem.put(file.getParent().substring(file.getParent().lastIndexOf("/") + 1), list);
                        }
                    }
                    dem++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            cursor.close();
        }
        return folderItem;
    }

    public static void deleteMediaStore(Context context, String path) {
        Uri collection;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            collection = MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        } else {
            collection = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        }
        context.getContentResolver().delete(collection, MediaStore.Audio.Media.DATA + " =?", new String[]{path});
    }


    public static ArrayList<String> getAllImageFromDevicesAll(Context context) {
        ArrayList<String> folderItem = new ArrayList<>();
        folderItem.add("camera");
        Uri collection;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        } else {
            collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }
        String[] projection = new String[]{MediaStore.Images.Media.DATA};

        String sortOrder = MediaStore.Images.Media.DATE_MODIFIED + " DESC";
        Cursor cursor = context.getContentResolver().query(collection, projection, null, null, sortOrder);
        int dem = 0;
        if (cursor != null) {
            while (cursor.moveToNext()) {
                File file;
                String path = cursor.getString(0);
                try {
                    file = new File(path);
                    if (file.exists()) {
                        folderItem.add(path);
                    }
                    dem++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            cursor.close();
        }
        return folderItem;
    }
}
