package com.nexters.vobble.network;

import org.json.*;

import android.app.*;
import android.content.*;
import android.text.*;

import com.loopj.android.http.*;
import com.nexters.vobble.*;
import com.nexters.vobble.core.*;


public class VobbleResponseHandler extends JsonHttpResponseHandler {
	
	public static final String CODE_SUCCESS = "1";
	
	private Context context;
	public VobbleResponseHandler(Context context) {
		this.context = context;
	}
	@Override
	public void onStart() {}

	@Override
	public void onFinish() {}

	@Override
	public void onSuccess(JSONObject response) {}

	@Override
	public final void onSuccess(int statusCode, JSONObject response) {
		Vobble.log("HTTP  : " + response.toString());
		String code = response.optString("result");
		String message = response.optString("msg");
		
		if(!TextUtils.isEmpty(code) && !CODE_SUCCESS.equals(code)) {
			onFailure(null, message + "(" + code + ")");
		} else {
			onSuccess(response);
			onCompletelyFinish();
		}
	}

	@Override
	public void onFailure(Throwable error, String response) {
		// 정말 404 500 에러가 날경우 처리
		StringBuilder errorMessageBuilder = new StringBuilder();
		if(!TextUtils.isEmpty(response)) {
			errorMessageBuilder.append(response).append("\n");
		}
		if(error != null) {
			errorMessageBuilder.append(error.getMessage());
		}
		
		Dialog d = new AlertDialog.Builder(context)
		.setMessage(errorMessageBuilder.toString().trim())
		.setPositiveButton(R.string.ok, null)
		.create();
		d.show();
		
		onCompletelyFinish();
		Vobble.log(errorMessageBuilder.toString().trim());
	}
	@Override
	public void onFailure ( Throwable e, JSONObject errorResponse ) {
		// 그냥 에러가 들어오는 곳
		Dialog d = new AlertDialog.Builder(context)
		.setMessage(errorResponse.optString("msg"))
		.setPositiveButton(R.string.ok, null)
		.create();
		d.show();
		
		onCompletelyFinish();
		Vobble.log(errorResponse.optString("msg"));
	}

	public void onCompletelyFinish() {}
}
