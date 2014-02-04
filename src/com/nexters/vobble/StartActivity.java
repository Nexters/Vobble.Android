package com.nexters.vobble;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;

public class StartActivity extends Activity {
	private ImageView loginImageView = null;
	private ImageView signUpImageView = null;
	private Intent intent = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.activity_start);
		
		loginImageView = (ImageView) findViewById(R.id.loginBtn);
		signUpImageView = (ImageView) findViewById(R.id.signUpBtn);
		
		loginImageView.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				intent = new Intent(StartActivity.this, LoginActivity.class);
				startActivity(intent);
			}
		});
		
		signUpImageView.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				intent  = new Intent(StartActivity.this, SignUpActivity.class);
				startActivity(intent);
			}
		});
	}
	
	
}
