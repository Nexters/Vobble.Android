package com.nexters.vobble.entity;

import java.io.Serializable;

import com.nexters.vobble.core.App;
import com.nexters.vobble.network.URL;
import org.json.JSONObject;

import android.text.TextUtils;

public class Vobble implements Serializable {
	
	private static final long serialVersionUID = 1L;

    public static final String VOBBLE_ID = "vobble_id";
    public static final String VOICE_URI = "voice_uri";
    public static final String IMAGE_URI = "image_uri";
    public static final String CREATED_AT = "created_at";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";

    private int vobbleId;
    private int userId;
    private String voiceUri;
	private String imageUri;
    private String username;
    private String createdAt;
    private double latitude;
    private double longitude;

	public static Vobble build(JSONObject json) {
		if (json == null) {
			return null;
		}
		
		Vobble vobble = new Vobble();
		vobble.vobbleId = json.optInt(VOBBLE_ID);
        vobble.voiceUri = json.optString(VOICE_URI);
		vobble.imageUri = json.optString(IMAGE_URI);
		vobble.userId = json.optInt(User.USER_ID);
        vobble.username = json.optString(User.USERNAME);
        vobble.createdAt = json.optString(CREATED_AT).substring(0, 10);
		vobble.latitude = json.optDouble(LATITUDE);
		vobble.longitude = json.optDouble(LONGITUDE);
		return vobble;
	}

	public String getVoiceUrl() {
		if (App.SERVER_TARGET == App.SERVER_TEST) {
			return URL.BASE_URL_DEVELOPMENT + "/files/" + voiceUri;
		} else {
			return URL.BASE_URL_PRODUCTION + "/files/" + voiceUri;
		}
	}

	public String getImageUrl() {
		if (App.SERVER_TARGET == App.SERVER_TEST) {
			return URL.BASE_URL_DEVELOPMENT + "/files/" + imageUri;
		} else {
			return URL.BASE_URL_PRODUCTION + "/files/" + imageUri;
		}
	}

    public String getUsername() {
        return username;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }
}
