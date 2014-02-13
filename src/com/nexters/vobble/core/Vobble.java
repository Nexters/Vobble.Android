package com.nexters.vobble.core;

import java.io.File;

import android.app.Application;
import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;

import com.nostra13.universalimageloader.cache.disc.impl.TotalSizeLimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;

public class Vobble extends Application {
	public static final int SERVER_TEST = 0;
	public static final int SERVER_PRODUCTION = 1;
	public static final int SERVER_TARGET = SERVER_PRODUCTION;
	
	public static final String EMAIL = "email";
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	public static final String TOKEN = "token";
	public static final String USER_ID = "user_id";
	public static final String LATITUDE = "latitude";
	public static final String LONGITUDE = "longitude";
	public static final String LIMIT = "limit";
	public static final String VOICE = "voice";
	public static final String IMAGE = "image";

    public static final String IMAGE_FILE_NAME = "vobble.jpg";
	public static final String SOUND_FILE_NAME = "vobble.m4a";
	public static final String TAG = "VOBBLE";
	

	public static void log(String msg) {
		log(TAG, msg);
	}
	
	public static void log(String tag, String msg) {
		Log.d(tag, msg);
	}
	public static String getToken(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context).getString(Vobble.TOKEN, "");
	}
	public static String getName(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context).getString(Vobble.USERNAME, "");
	}
	public static String getEmail(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context).getString(Vobble.EMAIL, "");
	}
	public static String getUserId(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context).getString(Vobble.USER_ID, "");
	}
	@Override
	public void onCreate() {
		super.onCreate();
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.build();
		
		File cacheDir = StorageUtils.getCacheDirectory(this);
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
		.memoryCache(new LruMemoryCache(8 * 1024 * 1024))
		.discCache(new TotalSizeLimitedDiscCache(cacheDir, 20 * 1024 * 1024))
		.defaultDisplayImageOptions(options)
		.build();
		ImageLoader.getInstance().init(config);
	}
}
