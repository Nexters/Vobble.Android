package com.nexters.vobble.activity;

import android.app.*;
import android.content.*;
import android.os.*;
import android.text.TextUtils;
import android.view.*;

import com.nexters.vobble.R;
import com.nexters.vobble.core.*;

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
		final String token = Vobble.getToken(this);
		if(!TextUtils.isEmpty(token)){
			Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
		}else{
			Intent intent = new Intent(this, StartActivity.class);
			startActivity(intent);
			finish();
		}
	}
}
