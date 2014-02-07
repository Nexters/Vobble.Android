package com.nexters.vobble;

import java.io.File;
import java.io.FileNotFoundException;

import org.json.JSONObject;

import com.loopj.android.http.RequestParams;

import android.app.*;
import android.content.*;
import android.os.*;
import android.preference.PreferenceManager;
import android.view.*;
import android.widget.*;

import com.nexters.vobble.core.*;
import com.nexters.vobble.network.*;

public class MapActivity extends BaseActivity implements View.OnClickListener{
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
			try {
				executeCreateVobble();
			} catch (FileNotFoundException e) {
				Vobble.log("FileNotFoundException");
				alert("보블 생성에 실패하였습니다.");
			}
			break;
		}
	}
	
	private void executeCreateVobble() throws FileNotFoundException {
        String url = String.format(URL.VOBBLES_CREATE,Vobble.getUserId(this));
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +"/.Sounds/vobble.m4a");
        
        RequestParams params = new RequestParams();
        params.put(Vobble.TOKEN,Vobble.getToken(this));
        params.put(Vobble.VOICE,file);
        params.put(Vobble.LATITUDE,"127");
        params.put(Vobble.LONGITUDE,"37");
        
        HttpUtil.post(url, null, params, new VobbleResponseHandler(MapActivity.this) {

            @Override
            public void onStart() {
                super.onStart();
                showLoading();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                hideLoading();
            }

            @Override
            public void onSuccess(JSONObject response) {
            	Intent intent = new Intent(MapActivity.this, MainActivity.class);
    			startActivity(intent);
            }
        });
    }
}
