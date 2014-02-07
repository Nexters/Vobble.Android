package com.nexters.vobble.core;

import com.nexters.vobble.R;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class BaseActivity extends Activity {

	private View loadingView;

	private int loadingStackCount = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		loadingView = LayoutInflater.from(this).inflate(R.layout.view_loading, null);
		loadingView.setVisibility(View.INVISIBLE);
	}

	@Override
	protected void onStart() {
		super.onStart();
		
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
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
