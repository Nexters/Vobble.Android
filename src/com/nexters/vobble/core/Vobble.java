package com.nexters.vobble.core;

import android.util.Log;

public class Vobble {
	public static final int SERVER_TEST = 0;
	public static final int SERVER_PRODUCTION = 1;
	public static final int SERVER_TARGET = SERVER_TEST;
	
	public static final String EMAIL = "email";
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	public static final String TOKEN = "token";
	
	public static final String TAG = "VOBBLE";
	
	public static void log(String msg) {
		log(TAG, msg);
	}
	
	public static void log(String tag, String msg) {
		Log.d(tag, msg);
	}
}
