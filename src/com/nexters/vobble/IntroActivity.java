package com.nexters.vobble;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;

public class IntroActivity extends Activity {
	private Handler handler = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_intro);
		
		handler = new Handler();
		handler.postDelayed(new Runnable() {
			public void run() {
				endIntro();
			}
		}, 1000);		
	}

	private void endIntro() {
		Intent intent = new Intent(this, StartActivity.class);
		startActivity(intent);
		finish();
	}
}
