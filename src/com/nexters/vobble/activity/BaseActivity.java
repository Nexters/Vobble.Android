package com.nexters.vobble.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;
import com.nexters.vobble.R;
import com.nexters.vobble.core.App;
import com.nexters.vobble.entity.Vobble;

public class BaseActivity extends Activity {
    private int loadingStackCount = 0;
    private View loadingView;
    private AlertDialog alertDialog;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (!isNetworkConnected()) {
			alert(R.string.error_network);
		}
		loadingView = LayoutInflater.from(this).inflate(R.layout.view_loading, null);
		loadingView.setVisibility(View.INVISIBLE);
	}
    @Override
	protected void onStart() {
		super.onStart();
		if(App.SERVER_TARGET == App.SERVER_PRODUCTION) {
			EasyTracker.getInstance(this).activityStart(this);
		}
		App.getGaTracker().send(MapBuilder.createAppView().build());
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onStop() {
		super.onStop();
		if(App.SERVER_TARGET == App.SERVER_PRODUCTION) {
			EasyTracker.getInstance(this).activityStop(this);
		}
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		((ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content)).addView(loadingView);
	}

	public synchronized void showLoading() {
		loadingStackCount++;
		loadingView.setVisibility(View.VISIBLE);
	}

	public synchronized void hideLoading() {
		loadingStackCount--;
		if(loadingStackCount <= 0) {
			loadingStackCount = 0;
			loadingView.setVisibility(View.INVISIBLE);
		}
	}

    public void showShortToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

	public void alert(int resId) {
		alert(getString(resId));
	}

	public void alert(String message) {
    	if (alertDialog != null && alertDialog.isShowing())
            return;
    	
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.ok, null);
        
        alertDialog = builder.create();
        alertDialog.show();
        
        TextView messageText = (TextView) alertDialog.findViewById(android.R.id.message);
        messageText.setGravity(Gravity.CENTER);
    }

	private boolean isNetworkConnected() {
	    ConnectivityManager connectivityManager 
	            = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();    
	}
}
