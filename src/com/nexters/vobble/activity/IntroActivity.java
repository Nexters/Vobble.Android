package com.nexters.vobble.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import com.nexters.vobble.R;
import com.nexters.vobble.core.Vobble;

public class IntroActivity extends Activity {
	private static int INTRO_LOADING_TIME = 1000;
    private Handler mHandler = null;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_intro);

		mHandler = new Handler();
		mHandler.postDelayed(new Runnable() {
            public void run() {
                endIntro();
            }
        }, INTRO_LOADING_TIME);
	}

	private void endIntro() {
        if (Vobble.isLoggedIn(this)) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(this, TutorialActivity.class);
            startActivity(intent);
            finish();
        }
	}
}
