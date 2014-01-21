package com.nexters.vobble;

import android.os.Bundle;
import android.app.Activity;
import android.content.*;
import android.view.*;
import android.widget.*;
import android.support.v4.app.NavUtils;

public class RecordActivity extends Activity {
	private ImageView confirmImageView = null;
	private Intent intent = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record);
		
		confirmImageView = (ImageView) findViewById(R.id.record_confirm_btn);
		
		confirmImageView.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				intent = new Intent(RecordActivity.this, MapActivity.class);
				startActivity(intent);
			}
		});
		
	}
}
