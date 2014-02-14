package com.nexters.vobble.activity;

import java.io.File;
import java.io.FileNotFoundException;

import com.nexters.vobble.nmap.NMapPOIFlagType;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.loopj.android.http.RequestParams;
import com.nexters.vobble.R;
import com.nexters.vobble.core.Vobble;
import com.nexters.vobble.network.HttpUtil;
import com.nexters.vobble.network.URL;
import com.nexters.vobble.network.VobbleResponseHandler;
import com.nexters.vobble.nmap.NMapViewerResourceProvider;
import com.nexters.vobble.util.CommonUtils;
import com.nexters.vobble.util.FileIOUtils;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;

public class ConfirmVobbleActivity extends BaseNMapActivity implements View.OnClickListener {
    private NMapView mMapView;
    private ImageView mIvPhoto;
	private Button mBtnSave;

    private Location mLocation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_map);
		
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

            int markerId = NMapPOIFlagType.PIN;
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
