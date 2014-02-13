package com.nexters.vobble.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import com.nexters.vobble.R;

public class IntroActivity extends Activity {
	private Handler handler = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_intro);
		
		// FOR TEST
		/*
		PreferenceManager.getDefaultSharedPreferences(IntroActivity.this)
		.edit().putString(Vobble.USER_ID, "21").commit();
		PreferenceManager.getDefaultSharedPreferences(SignInActivity.this)
        .edit().putString(Vobble.TOKEN, "ac59b5a203b2e6f54c887a67b55b0083").commit();
		*/
		handler = new Handler();
		handler.postDelayed(new Runnable() {
			public void run() {
				endIntro();
			}
		}, 1000);		
	}

	private void endIntro() {
		Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
	}
}
