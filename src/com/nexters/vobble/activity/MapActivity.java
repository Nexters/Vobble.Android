package com.nexters.vobble.activity;

import java.io.File;
import java.io.FileNotFoundException;

import com.nexters.vobble.R;
import org.json.JSONObject;

import com.loopj.android.http.RequestParams;

import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;

import com.nexters.vobble.core.*;
import com.nexters.vobble.network.*;

public class MapActivity extends BaseActivity implements View.OnClickListener{
	private ImageView ivPhoto;
	private Button btnSave;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_map);
		
		initResources();
		initEvents();
	}
	
	private void initResources() {
		ivPhoto = (ImageView) findViewById(R.id.iv_photo_view);
		btnSave = (Button) findViewById(R.id.btn_save);
	}
	
	private void initEvents() {
		ivPhoto.setOnClickListener(this);
		btnSave.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.iv_photo_view :
			break;
		case R.id.btn_save :
			try {
				executeCreateVobble();
			} catch (FileNotFoundException e) {
				Vobble.log("FileNotFoundException");
				alert(R.string.error_vobble_create);
			}
			break;
		}
	}
	
	private void executeCreateVobble() throws FileNotFoundException {
        String url = String.format(URL.VOBBLES_CREATE,Vobble.getUserId(this));
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +"/.Sounds/" + Vobble.SOUND_FILE_NAME);
        
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
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    			startActivity(intent);
            }
        });
    }
}
