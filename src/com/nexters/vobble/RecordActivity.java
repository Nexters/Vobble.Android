package com.nexters.vobble;

import android.app.*;
import android.content.*;
import android.net.*;
import android.os.*;
import android.view.*;
import android.widget.*;

import com.nexters.vobble.record.*;

public class RecordActivity extends Activity implements View.OnClickListener{
	public static final int RECORD_MODE = 1;
	public static final int STOP_MODE = 2;
	public static final int PLAY_MODE = 3;	
	
	private ImageView cameraImageView;
	private ImageView recordImageView;
	private ImageView resetImageView;
	private Button confirmBtn;
	private RecordManager recordManager;
	private Uri imageUri;
	private int recordMode;
	private boolean isRecordCheck;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_record);
		
		initResource();
		initEvent();
	}
	
	private void initResource(){
		cameraImageView	= (ImageView)findViewById(R.id.iv_record_camera);		
		recordImageView = (ImageView)findViewById(R.id.iv_record_record_btn);
		resetImageView = (ImageView)findViewById(R.id.iv_record_re_btn);
		confirmBtn = (Button) findViewById(R.id.btn_record_confirm);
	}
	
	private void initEvent(){
		cameraImageView.setOnClickListener(this);
		recordImageView.setOnClickListener(this);
		resetImageView.setOnClickListener(this);
		confirmBtn.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View view){
		Intent intent = null;
		switch(view.getId()){
		case R.id.iv_record_camera :
			break;
		case R.id.iv_record_record_btn :
			recordStatusPattern();
			break;
		case R.id.iv_record_re_btn : 
			break;
		case R.id.btn_record_confirm :
			confirm();
			break;
		}
	}

	private void recordStatusPattern(){
		Intent intent = null;
		
		switch(recordMode){
		case RECORD_MODE :
			recordImageView.setImageResource(R.drawable.record_stop_btn);
			recordManager.startRecord("/storage/sdcard0/Sounds/vobble.m4a");
			recordMode = STOP_MODE;
			break;
		case STOP_MODE :
			recordImageView.setImageResource(R.drawable.play_btn); // 회색
			recordManager.stopRecord();
			recordMode = PLAY_MODE;
			break;
		case PLAY_MODE :
			cameraImageView.setImageResource(R.drawable.record_camera_icon);
			cameraImageView.setOnClickListener(RecordActivity.this);
			recordImageView.setImageResource(R.drawable.play2_btn);
			recordManager.playRecord("/storage/sdcard0/Sounds/vobble.m4a");
			break;
		default :
			break;
		}
	}
	private void confirm(){
			Intent nextIntent = new Intent(RecordActivity.this, MapActivity.class);
			nextIntent.putExtra("recordUri", "/storage/sdcard0/Sounds/vobble.m4a");
			startActivity(nextIntent);
	}	
}