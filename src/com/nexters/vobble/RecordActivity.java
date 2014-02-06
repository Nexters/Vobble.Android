package com.nexters.vobble;

import android.os.Bundle;
import android.app.Activity;
import android.content.*;
import android.view.*;
import android.widget.*;
import android.support.v4.app.NavUtils;

public class RecordActivity extends Activity implements View.OnClickListener{
	private ImageView confirmImageView;
	private Intent intent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_record);
		
		
		
	
		confirmImageView.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				intent = new Intent(RecordActivity.this, MapActivity.class);
				startActivity(intent);
			}
		});
	}
	
	private void initResource(){
		confirmImageView = (ImageView) findViewById(R.id.record_confirm_btn);
	
	}
	
	private void initEvent(){
		
	}
	
	@Override
	public void onClick(View view){
		switch(view.getId()){
		case R.id.iv_record_camera_icon : 
			intent = new Intent(RecordActivity.this, MapActivity.class);
			startActivity(intent);
			break;
		case R.id.iv_record_pause_btn :
			break;
		case R.id.iv_record_re_btn : 
			break;
		case R.id.btn_record_confirm :
			break;
		}
		
		
	}
}
