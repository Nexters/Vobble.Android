package com.nexters.vobble.activity;

import java.io.*;

import org.json.*;

import android.content.*;
import android.graphics.*;
import android.location.*;
import android.os.*;
import android.view.*;
import android.widget.*;

import com.loopj.android.http.*;
import com.nexters.vobble.*;
import com.nexters.vobble.core.*;
import com.nexters.vobble.network.*;
import com.nexters.vobble.nmap.*;
import com.nexters.vobble.util.*;
import com.nhn.android.maps.*;
import com.nhn.android.maps.overlay.*;
import com.nhn.android.mapviewer.overlay.*;

public class ConfirmVobbleActivity extends BaseNMapActivity implements View.OnClickListener {
    private NMapView mMapView;
    private ImageView mIvPhoto;
	private Button mBtnSave;

    private Location mLocation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_confirm_vobble);
		
        initResources();
		initEvents();
        initImage();
        initMapView();
	}

	private void initResources() {
        mLocation = CommonUtils.getLocation(this);
        mIvPhoto = (ImageView) findViewById(R.id.iv_photo_view);
		mBtnSave = (Button) findViewById(R.id.btn_save);
	}
	
	private void initEvents() {
		mBtnSave.setOnClickListener(this);
	}

    private void initImage() {
        Bitmap imageBitmap = BitmapFactory.decodeFile(FileIOUtils.getImageFile().getAbsolutePath());
        mIvPhoto.setImageBitmap(CommonUtils.getCroppedBitmap(imageBitmap, 450));
    }

    private void initMapView() {
        mMapView = (NMapView) findViewById(R.id.map_view);
        mMapView.setApiKey(Vobble.NMAP_API_KEY);
        mMapView.setClickable(false);

        if (mLocation != null) {
            NMapViewerResourceProvider mMapViewerResourceProvider = new NMapViewerResourceProvider(this);
            NMapOverlayManager mOverlayManager = new NMapOverlayManager(this, mMapView, mMapViewerResourceProvider);

            int markerId = NMapPOIflagType.PIN;
            NMapPOIdata poiData = new NMapPOIdata(1, mMapViewerResourceProvider);
            poiData.beginPOIdata(1);
            poiData.addPOIitem(mLocation.getLongitude(), mLocation.getLatitude(), "현재 위치", markerId, 0);
            poiData.endPOIdata();

            NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);
            poiDataOverlay.showAllPOIdata(0);
        } else {
            alert(R.string.error_cannot_use_gps);
        }
    }

    @Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_save :
			try {
                executeCreateVobble();
            } catch (FileNotFoundException e) {
                Vobble.log("FileNotFoundException");
                alert(R.string.error_cannot_create_vobble);
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

        if (mLocation != null) {
            params.put(Vobble.LATITUDE, String.valueOf(mLocation.getLatitude()));
            params.put(Vobble.LONGITUDE, String.valueOf(mLocation.getLongitude()));
        } else {
            alert(R.string.error_cannot_use_gps);
            return;
        }

        HttpUtil.post(url, null, params, new VobbleResponseHandler(ConfirmVobbleActivity.this) {

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
                Intent intent = new Intent(ConfirmVobbleActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    			startActivity(intent);
            }
        });
    }
}
