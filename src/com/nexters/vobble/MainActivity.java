package com.nexters.vobble;

import android.app.*;
import android.content.*;
import android.os.*;
import android.support.v4.view.*;
import android.view.*;
import android.widget.*;

public class MainActivity extends Activity{
	private ImageView recordImageView = null;
	private Intent intent = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		recordImageView = (ImageView) findViewById(R.id.voice_record_btn);
		
		recordImageView.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				intent = new Intent(MainActivity.this, RecordActivity.class);
				startActivity(intent);
			}
		});
	}
}
