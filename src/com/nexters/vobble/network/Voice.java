package com.nexters.vobble.network;

import java.io.Serializable;

import org.json.JSONObject;

import android.text.TextUtils;

import com.nexters.vobble.core.Vobble;

public class Voice implements Serializable{
	
	private static final long serialVersionUID = 1L;
	public double distance = 0.0f;
	public String voiceUri = "";
	public String imgUri = "";
	public String userId = "";
	public double latitude = 0.0f;
	public double longitude = 0.0f;
	public static Voice parse(JSONObject json) {
		if(json == null) {
			return null;
		}
		
		Voice voice = new Voice();
		voice.distance = json.optDouble("id");
		voice.voiceUri = json.optString("voice_uri");
		voice.imgUri = json.optString("image_uri");
		voice.userId = json.optString("user_id");
		voice.latitude = json.optDouble("latitude");
		voice.longitude = json.optDouble("longitude");
		return voice;
	}
	public String getStreamingVoiceUrl(){
		if(Vobble.SERVER_TARGET == Vobble.SERVER_TEST) {
			return URL.BASE_URL_DEVELOPMENT+"/files/"+voiceUri;
		} else {
			return URL.BASE_URL_PRODUCTION+"/files/"+voiceUri;
		}
	}
	public boolean isImageExist(){ 
		if(TextUtils.isEmpty(imgUri)){
			return false;
		}
		return true;
	}
	public String getImageUrl(){
		if(Vobble.SERVER_TARGET == Vobble.SERVER_TEST) {
			return URL.BASE_URL_DEVELOPMENT+"/files/"+imgUri;
		} else {
			return URL.BASE_URL_PRODUCTION+"/files/"+imgUri;
		}
	}
}
