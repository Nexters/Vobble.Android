package com.nexters.vobble.core;

import java.io.File;

import android.app.Application;
import android.content.Context;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.nostra13.universalimageloader.cache.disc.impl.TotalSizeLimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;

public class App extends Application {
	public static final int SERVER_TEST = 0;
	public static final int SERVER_PRODUCTION = 1;
	public static final int SERVER_TARGET = SERVER_PRODUCTION;

    public static final String VOICE = "voice";
	public static final String IMAGE = "image";

    public static final String TAG = "VOBBLE";

    public static final String NMAP_API_KEY = "9d613b3fed909e86f46be79aae114235";

	public static void log(String msg) {
		log(TAG, msg);
	}
	
	public static void log(String tag, String msg) {
		Log.d(tag, msg);
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
