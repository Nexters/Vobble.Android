package com.nexters.vobble.util;


import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import android.graphics.Bitmap;
import android.os.Environment;

public class TempFileManager {

    private static final String IMAGE_FILE_NAME = "vobble.jpg";
    private static final String VOICE_FILE_NAME = "vobble.m4a";

    private static final String basePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/.Vobble/";
    private static final String imageFilePath = basePath + IMAGE_FILE_NAME;
    private static final String voiceFilePath = basePath + VOICE_FILE_NAME;

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
        File file = new File(basePath);
        if(!file.exists())
            file.mkdirs();
        return imageFilePath;
    }

    public static String getVoiceFilePath() {
        File file = new File(basePath);
        if(!file.exists())
            file.mkdirs();
        return voiceFilePath;
    }

    public static File getImageFile() {
        return new File(imageFilePath);
    }

    public static File getVoiceFile() {
        return new File(voiceFilePath);
    }

    public static void deleteImageFile() {
        File file = getImageFile();
        if (file.exists())
            file.delete();
    }

    public static void deleteVoiceFile() {
        File file = getVoiceFile();
        if (file.exists())
            file.delete();
    }

    public static boolean isExistSoundFile() {
        File file = getVoiceFile();
        return file.exists();
    }
}
