package com.nexters.vobble.util;


import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import android.graphics.Bitmap;
import android.os.Environment;

import com.nexters.vobble.core.Vobble;

public class FileIOUtils {

    public static File saveBitmapToImageFile(Bitmap bitmap) {
        File file = new File(getImageFilePath());
        OutputStream out;
        try {
            file.createNewFile();
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    public static String getImageFilePath() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/.Images/";
        File file = new File(path);
        if(!file.exists()) {
            file.mkdirs();
        }
        return path + Vobble.IMAGE_FILE_NAME;
    }

    public static String getVoiceFilePath() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/.Sounds/";
        File file = new File(path);
        if(!file.exists()) {
            file.mkdirs();
        }
        return path + Vobble.SOUND_FILE_NAME;
    }

    public static File getImageFile() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/.Images/" + Vobble.IMAGE_FILE_NAME;
        return new File(path);
    }

    public static File getVoiceFile() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/.Sounds/" + Vobble.SOUND_FILE_NAME;
        return new File(path);
    }

    static public void deleteImageFile() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/.Images/" + Vobble.IMAGE_FILE_NAME;
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }

    static public void deleteSoundFile() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/.Sounds/" + Vobble.SOUND_FILE_NAME;
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }

    static public boolean isExistSoundFile() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/.Sounds/" + Vobble.SOUND_FILE_NAME;
        File file = new File(path);
        if (file.exists()) {
            return true;
        }
        return false;
    }
}
