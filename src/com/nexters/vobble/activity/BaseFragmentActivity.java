package com.nexters.vobble.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.nexters.vobble.R;
import com.nexters.vobble.core.App;

public class BaseFragmentActivity extends FragmentActivity {
    private int loadingStackCount = 0;
    private View loadingView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		loadingView = LayoutInflater.from(this).inflate(R.layout.view_loading, null);
		loadingView.setVisibility(View.INVISIBLE);
		
	}
	@Override
	protected void onStart() {
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this);
		App.getGaTracker().set(Fields.SCREEN_NAME, (String)getTitle());
		App.getGaTracker().send(MapBuilder.createAppView().build());
	}
	@Override
	protected void onStop() {
		super.onStop();
		EasyTracker.getInstance(this).activityStop(this);
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

	public void alert(int resId) {
		alert(getString(resId));
	}

	public void alert(String message) {
		Dialog d = new AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton(R.string.ok, null)
            .create();
		d.show();
		TextView messageText = (TextView) d.findViewById(android.R.id.message);
		messageText.setGravity(Gravity.CENTER);
	}
}
