package com.nexters.vobble.network;

import com.nexters.vobble.core.App;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.text.TextUtils;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nexters.vobble.R;


public class APIResponseHandler extends JsonHttpResponseHandler {
	private AlertDialog alertDialog;
	public static final String CODE_SUCCESS = "1";
	
	private Context context;
	public APIResponseHandler(Context context) {
		this.context = context;
	}
	@Override
	public void onStart() {
		
	}

	@Override
	public void onFinish() {}

	@Override
	public void onSuccess(JSONObject response) {}

	@Override
	public final void onSuccess(int statusCode, JSONObject response) {
		App.log("HTTP  : " + response.toString());
		String code = response.optString("result");
		String message = response.optString("msg");
		
		if(!TextUtils.isEmpty(code) && !CODE_SUCCESS.equals(code)) {	    	
	    	AlertDialog.Builder builder = new AlertDialog.Builder(context);
	        builder.setMessage(message + "(" + code + ")");
	        builder.setPositiveButton(R.string.ok, null);
	        
	        alertDialog = builder.create();
	        alertDialog.show();
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
		
		if( alertDialog != null && alertDialog.isShowing() ) return;
    	
    	AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.error_network);
        builder.setPositiveButton(R.string.ok, null);
        
        alertDialog = builder.create();
        alertDialog.show();
		
		onCompletelyFinish();
		App.log(errorMessageBuilder.toString().trim());
	}
	@Override
	public void onFailure ( Throwable e, JSONObject errorResponse ) {
		// 그냥 에러가 들어오는 곳
		if( alertDialog != null && alertDialog.isShowing() ) return;
    	
    	AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.error_network);
        builder.setPositiveButton(R.string.ok, null);
        
        alertDialog = builder.create();
        alertDialog.show();
		onCompletelyFinish();
		App.log(errorResponse.optString("msg"));
	}
	
	public void onCompletelyFinish() {}
}
