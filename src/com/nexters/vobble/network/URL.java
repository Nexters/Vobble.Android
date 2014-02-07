package com.nexters.vobble.network;

import com.nexters.vobble.core.*;

public class URL {
	public static final String BASE_URL_DEVELOPMENT = "http://127.0.0.1";
	public static final String BASE_URL_PRODUCTION = "http://vobble.herokuapp.com";
	public static final String getBaseUrl() {
		if(Vobble.SERVER_TARGET == Vobble.SERVER_TEST) {
			return BASE_URL_PRODUCTION;
		} else {
			return BASE_URL_PRODUCTION;
		}
	}
	
	
	public static final String SIGN_UP = getBaseUrl() + "/users";
	public static final String SIGN_IN = getBaseUrl() + "/tokens";
	public static final String VOBBLES = getBaseUrl() + "/vobbles";
	public static final String VOBBLES_COUNT = getBaseUrl() + "/vobbles/count";
	public static final String VOBBLES_CREATE = getBaseUrl() + "/users/%s/vobbles";
	public static final String USER_VOBBLES = getBaseUrl() + "/users/%s/vobbles";
	public static final String USER_VOBBLES_COUNT = getBaseUrl() + "/users/%s/vobbles/count";
	public static final String USER_VOBBLES_DELETE = getBaseUrl() + "/users/%s/vobbles/%s";
}