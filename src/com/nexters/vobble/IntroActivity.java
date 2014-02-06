package com.nexters.vobble;

import android.app.*;
import android.content.*;
import android.os.*;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.*;

import com.nexters.vobble.core.*;

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
		final String token = PreferenceManager.getDefaultSharedPreferences(this).getString(Vobble.TOKEN, "");
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
