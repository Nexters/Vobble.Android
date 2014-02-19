package com.nexters.vobble.activity;

import java.io.*;

import com.nexters.vobble.entity.User;
import com.nexters.vobble.entity.Vobble;
import org.json.*;

import android.content.*;
import android.graphics.*;
import android.location.*;
import android.os.*;
import android.view.*;
import android.widget.*;

import com.google.analytics.tracking.android.Fields;
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
    private int mIvPhotoWidth;
    private boolean loadImage = false;
    private Location mLocation;

    private NMapView mMapView;
    private ImageView mIvPhoto;
	private Button mBtnSave;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_confirm_vobble);

        initLocation();
        initResources();
		initEvents();
        initMapView();
        
	}

    private void initLocation() {
        LocationHelper locationHelper = new LocationHelper(this);

        if (locationHelper.isGPSEnabled()) {
            mLocation = locationHelper.getCurrentLocation();
            if(mLocation == null){
            	mLocation = new Location("");
                mLocation.setLatitude(37);
                mLocation.setLongitude(127);
            }
        } else {
            alert(R.string.error_cannot_use_gps);
            mLocation = new Location("");
            mLocation.setLatitude(37);
            mLocation.setLongitude(127);
        }
    }

	private void initResources() {
        mIvPhoto = (ImageView) findViewById(R.id.iv_photo_view);
		mBtnSave = (Button) findViewById(R.id.btn_save);
        mMapView = (NMapView) findViewById(R.id.map_view);
	}
	
	private void initEvents() {
		mBtnSave.setOnClickListener(this);
	}

    private void initMapView() {
        mMapView.setApiKey(App.NMAP_API_KEY);
        mMapView.setClickable(true);

        NMapViewerResourceProvider mMapViewerResourceProvider = new NMapViewerResourceProvider(this);
        NMapOverlayManager mOverlayManager = new NMapOverlayManager(this, mMapView, mMapViewerResourceProvider);
        mOverlayManager.setOnCalloutOverlayListener(onCalloutOverlayListener);

        int markerId = NMapPOIflagType.PIN;
        NMapPOIdata poiData = new NMapPOIdata(1, mMapViewerResourceProvider);
        poiData.beginPOIdata(1);
        poiData.addPOIitem(mLocation.getLongitude(), mLocation.getLatitude(), "You're in here.", markerId, 0);
        poiData.endPOIdata();

        NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);
        poiDataOverlay.showAllPOIdata(0);
        poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        // [주의] onCreate에서 보블 이미지를 이미지뷰에 붙이면 ImageView의 getWidth()가 0을 반환해서 제대로 붙지 않음
        // onWindowFocusChanged부터 ImageView가 Window에 잘 붙어서 올바른 getWidth()를 반환하므로
        // 여기에서 초기화시켜야 함 - by 수완
        if (!loadImage) {
            loadImage = true;
            mIvPhotoWidth = mIvPhoto.getWidth();
            initImage();
        }
    }

    private void initImage() {
        Bitmap imageBitmap = BitmapFactory.decodeFile(TempFileManager.getImageFile().getAbsolutePath());
        mIvPhoto.setImageBitmap(ImageManagingHelper.getCroppedBitmap(imageBitmap, mIvPhotoWidth));
    }

    private NMapPOIdataOverlay.OnStateChangeListener onPOIdataStateChangeListener = new NMapPOIdataOverlay.OnStateChangeListener() {
        @Override
        public void onFocusChanged(NMapPOIdataOverlay poiDataOverlay, NMapPOIitem item) {}

        @Override
        public void onCalloutClick(NMapPOIdataOverlay poiDataOverlay, NMapPOIitem item) {}
    };

    private NMapOverlayManager.OnCalloutOverlayListener onCalloutOverlayListener = new NMapOverlayManager.OnCalloutOverlayListener() {
        @Override
        public NMapCalloutOverlay onCreateCalloutOverlay(NMapOverlay itemOverlay, NMapOverlayItem overlayItem, Rect itemBounds) {
            return new NMapCalloutBasicOverlay(itemOverlay, overlayItem, itemBounds);
        }
    };

    @Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_save:
            executeSaving();
            break;
		}
	}

    private void executeSaving() {
        try {
            executeCreatingVobble();
        } catch (FileNotFoundException e) {
            App.log("FileNotFoundException");
            alert(R.string.error_cannot_create_vobble);
        }
    }

    private void executeCreatingVobble() throws FileNotFoundException {
        String url = String.format(URL.VOBBLES_CREATE, AccountManager.getInstance().getUserId(this));

        File voiceFile = TempFileManager.getVoiceFile();
        File imageFile = TempFileManager.getImageFile();

        RequestParams params = new RequestParams();
        params.put(User.TOKEN, AccountManager.getInstance().getToken(this));
        params.put(Vobble.LATITUDE, String.valueOf(mLocation.getLatitude()));
        params.put(Vobble.LONGITUDE, String.valueOf(mLocation.getLongitude()));
        
        params.put(App.VOICE, voiceFile);
        params.put(App.IMAGE, imageFile);

        HttpUtil.post(url, null, params, new APIResponseHandler(ConfirmVobbleActivity.this) {

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
