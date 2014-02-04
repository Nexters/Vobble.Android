package com.nexters.vobble;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;

public class SignInActivity extends Activity {
	private TextView loginTextView = null;
	private Intent intent = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_sign_in);
		
		loginTextView = (TextView) findViewById(R.id.login_check);
		
		loginTextView.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				intent = new Intent(SignInActivity.this, MainActivity.class);
				startActivity(intent);
			}
		});
	}
}
