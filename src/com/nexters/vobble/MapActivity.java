package com.nexters.vobble;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;

public class MapActivity extends Activity implements View.OnClickListener{
	private ImageView photoImageView;
	private Button saveBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_map);
		
		initResource();
		initEvent();
	}
	
	private void initResource(){
		photoImageView = (ImageView)findViewById(R.id.iv_photo_view);
		saveBtn = (Button) findViewById(R.id.btn_save);
	}
	
	private void initEvent(){
		photoImageView.setOnClickListener(this);
		saveBtn.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View view){
		switch(view.getId()){
		case R.id.iv_photo_view :
			break;
		case R.id.btn_save :
			Intent intent = new Intent(MapActivity.this, MainActivity.class);
			startActivity(intent);
			break;
		}
	}
}
