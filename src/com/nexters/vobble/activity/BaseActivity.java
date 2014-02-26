package com.nexters.vobble.activity;

import android.app.Activity;
import android.app.AlertDialog;
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

import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.nexters.vobble.R;
import com.nexters.vobble.core.App;

public class BaseActivity extends Activity {
    private int mLoadingStackCount = 0;
    private View mLoadingView;
    private AlertDialog mAlertDialog;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mLoadingView = LayoutInflater.from(this).inflate(R.layout.view_loading, null);
		mLoadingView.setVisibility(View.INVISIBLE);
	}

    @Override
	protected void onStart() {
		super.onStart();
		App.getGaTracker().set(Fields.SCREEN_NAME, (String) getTitle());
		App.getGaTracker().send(MapBuilder.createAppView().build());
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		((ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content)).addView(mLoadingView);
	}

	public synchronized void showLoading() {
		mLoadingStackCount++;
		mLoadingView.setVisibility(View.VISIBLE);
	}

	public synchronized void hideLoading() {
		mLoadingStackCount--;
		if(mLoadingStackCount <= 0) {
			mLoadingStackCount = 0;
			mLoadingView.setVisibility(View.INVISIBLE);
		}
	}

    public void showShortToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

	public void showAlert(int resId) {
		showAlert(getString(resId));
	}

	public void showAlert(String message) {
    	if (mAlertDialog != null && mAlertDialog.isShowing())
            return;
    	
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.ok, null);
        
        mAlertDialog = builder.create();
        mAlertDialog.show();
        
        TextView messageText = (TextView) mAlertDialog.findViewById(android.R.id.message);
        messageText.setGravity(Gravity.CENTER);
    }
}
