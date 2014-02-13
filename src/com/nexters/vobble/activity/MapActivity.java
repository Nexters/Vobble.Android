package com.nexters.vobble.activity;

import java.io.*;

import org.json.*;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.net.*;
import android.os.*;
import android.provider.*;
import android.view.*;
import android.widget.*;

import com.loopj.android.http.*;
import com.nexters.vobble.*;
import com.nexters.vobble.core.*;
import com.nexters.vobble.network.*;
import com.nexters.vobble.util.*;

public class MapActivity extends BaseActivity implements View.OnClickListener {
    private ImageView ivPhoto;
	private Button btnSave;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_map);
		
		initResources();
		initEvents();
        initImage();
	}
	
	private void initResources() {
		ivPhoto = (ImageView) findViewById(R.id.iv_photo_view);
		btnSave = (Button) findViewById(R.id.btn_save);
	}
	
	private void initEvents() {
		btnSave.setOnClickListener(this);
	}

    private void initImage() {
        Bitmap imageBitmap = BitmapFactory.decodeFile(FileIOUtils.getImageFile().getAbsolutePath());
        ivPhoto.setImageBitmap(CommonUtils.getCroppedBitmap(imageBitmap, 450));
    }

    @Override
	public void onClick(View view) {
		switch (view.getId()) {
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

        File voiceFile = FileIOUtils.getVoiceFile();
        File imageFile = FileIOUtils.getImageFile();

        RequestParams params = new RequestParams();
        params.put(Vobble.TOKEN, Vobble.getToken(this));
        params.put(Vobble.VOICE, voiceFile);
        params.put(Vobble.IMAGE, imageFile);
        params.put(Vobble.LATITUDE, "127");
        params.put(Vobble.LONGITUDE, "37");
        
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
