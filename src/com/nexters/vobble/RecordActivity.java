package com.nexters.vobble;

import android.os.Bundle;
import android.app.Activity;
import android.content.*;
import android.view.*;
import android.widget.*;
import android.support.v4.app.NavUtils;

public class RecordActivity extends Activity implements View.OnClickListener{
	private Button confirmBtn;
	private ImageView pauseImageView;
	private ImageView resetImageView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_record);
		
		initResource();
		initEvent();
	}
	
	private void initResource(){
		confirmBtn = (Button) findViewById(R.id.btn_record_confirm);
		pauseImageView = (ImageView)findViewById(R.id.iv_record_pause_btn);
		resetImageView = (ImageView)findViewById(R.id.iv_record_re_btn);
	}
	
	private void initEvent(){
		confirmBtn.setOnClickListener(this);
		pauseImageView.setOnClickListener(this);
		resetImageView.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View view){
		Intent intent = null;
		switch(view.getId()){
		case R.id.iv_record_camera_icon :
			break;
		case R.id.iv_record_pause_btn :
			break;
		case R.id.iv_record_re_btn : 
			break;
		case R.id.btn_record_confirm :
			intent = new Intent(RecordActivity.this, MapActivity.class);
			startActivity(intent);
			break;
		}
	}
}
