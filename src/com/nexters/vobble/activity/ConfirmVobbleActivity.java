package com.nexters.vobble.activity;

import java.io.*;

import com.nexters.vobble.entity.User;
import com.nexters.vobble.entity.Vobble;
import com.nexters.vobble.listener.CustomOnCalloutOverlayListener;
import com.nexters.vobble.listener.CustomOnStateChangeListener;
import com.nexters.vobble.listener.ImageViewTouchListener;
import com.nhn.android.maps.maplib.NGeoPoint;
import org.json.*;

import android.content.*;
import android.graphics.*;
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

public class ConfirmVobbleActivity extends BaseNMapActivity {
    private int mIvPhotoWidth;
    private boolean isVobbleImageLoaded = false;

    private NMapView mMapView;
    private NMapController mMapController;
    private NMapOverlayManager mOverlayManager;
    private NMapViewerResourceProvider mMapViewerResourceProvider;
    private NMapPOIdataOverlay poiDataOverlay;
    private NMapLocationManager mLocationManager;
    private CustomOnCalloutOverlayListener mCalloutOverlayListener;
    private CustomOnStateChangeListener mPOIdataStateChangeListener;

    private ImageView mIvPhoto;
    private ImageView mIvReloadLocation;
	private Button mBtnSave;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_confirm_vobble);

        initResources();
		initEvents();
        initMapView();
        initLocation();
	}

    private void initResources() {
        mIvReloadLocation = (ImageView) findViewById(R.id.iv_reload_location);
        mIvPhoto = (ImageView) findViewById(R.id.iv_photo_view);
		mBtnSave = (Button) findViewById(R.id.btn_save);
	}
	
	private void initEvents() {
        ImageViewTouchListener ivTouchListener = new ImageViewTouchListener();
        mIvReloadLocation.setOnTouchListener(ivTouchListener);
        mIvReloadLocation.setOnClickListener(btnClickListener);
        mBtnSave.setOnClickListener(btnClickListener);
	}

    private void initMapView() {
        mMapView = (NMapView) findViewById(R.id.map_view);
        mMapView.setApiKey(App.NMAP_API_KEY);
        mMapView.setClickable(true);
        mMapController = mMapView.getMapController();

        mMapViewerResourceProvider = new NMapViewerResourceProvider(this);
        mOverlayManager = new NMapOverlayManager(this, mMapView, mMapViewerResourceProvider);
        mCalloutOverlayListener = new CustomOnCalloutOverlayListener();
        mPOIdataStateChangeListener = new CustomOnStateChangeListener();

        mOverlayManager.setOnCalloutOverlayListener(mCalloutOverlayListener);
    }

    private void initLocation() {
        mLocationManager = new NMapLocationManager(this);
        mLocationManager.setOnLocationChangeListener(mLocationListener);
        if (mLocationManager.enableMyLocation(false)) {
            showShortToast("위치 정보를 가져옵니다.");
        } else {
            showDialogForLocationAccessSetting();
        }
    }

    private NMapLocationManager.OnLocationChangeListener mLocationListener = new NMapLocationManager.OnLocationChangeListener() {
        @Override
        public boolean onLocationChanged(NMapLocationManager nMapLocationManager, NGeoPoint location) {
            initOverlayInMapView(location);
            return false;
        }

        @Override
        public void onLocationUpdateTimeout(NMapLocationManager nMapLocationManager) {

        }

        @Override
        public void onLocationUnavailableArea(NMapLocationManager nMapLocationManager, NGeoPoint nGeoPoint) {

        }
    };

    private void initOverlayInMapView(NGeoPoint location) {
        NMapPOIdata poiData = new NMapPOIdata(1, mMapViewerResourceProvider);
        poiData.beginPOIdata(1);
        poiData.addPOIitem(location.getLongitude(), location.getLatitude(), "You're in here.", NMapPOIflagType.PIN, 0);
        poiData.endPOIdata();

        if (poiDataOverlay != null)
            poiDataOverlay.removeAllPOIdata();

        poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);
        poiDataOverlay.showAllPOIdata(0);
        poiDataOverlay.setOnStateChangeListener(mPOIdataStateChangeListener);

        mMapController.animateTo(location);
        mMapController.setZoomLevel(10);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        initVobbleImage();
    }

    private void initVobbleImage() {
        // [주의] onCreate에서 보블 이미지를 이미지뷰에 붙이면 ImageView의 getWidth()가 0을 반환해서 제대로 붙지 않음
        // onWindowFocusChanged부터 ImageView가 Window에 잘 붙어서 올바른 getWidth()를 반환하므로
        // 여기에서 초기화시켜야 함 - by 수완
        if (!isVobbleImageLoaded) {
            mIvPhotoWidth = mIvPhoto.getWidth();
            loadVobbleImage();
        }
    }

    private void loadVobbleImage() {
        isVobbleImageLoaded = true;
        Bitmap imageBitmap = BitmapFactory.decodeFile(TempFileManager.getImageFile().getAbsolutePath());
        mIvPhoto.setImageBitmap(ImageManagingHelper.getCroppedBitmap(imageBitmap, mIvPhotoWidth));
    }

    private View.OnClickListener btnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_save:
                    executeCreatingVobbleIfLocationEnabled();
                    break;
                case R.id.iv_reload_location:
                    initLocation();
                    break;
            }
        }
    };

    private void executeCreatingVobbleIfLocationEnabled() {
        if (!mLocationManager.isMyLocationEnabled()) {
            showShortToast("위치 정보를 가져오지 못했습니다.");
        } else {
            executeCreatingVobble(mLocationManager.getMyLocation());
        }
    }

    private void executeCreatingVobble(NGeoPoint location) {
        String url = String.format(URL.VOBBLES_CREATE, AccountManager.getInstance().getUserId(this));

        File voiceFile = TempFileManager.getVoiceFile();
        File imageFile = TempFileManager.getImageFile();

        RequestParams params = new RequestParams();
        params.put(User.TOKEN, AccountManager.getInstance().getToken(this));
        params.put(Vobble.LATITUDE, String.valueOf(location.getLatitude()));
        params.put(Vobble.LONGITUDE, String.valueOf(location.getLongitude()));

        try {
            params.put(App.VOICE, voiceFile);
            params.put(App.IMAGE, imageFile);
        } catch (FileNotFoundException e) {
            showShortToast("음성, 사진 파일이 유실되었습니다. 다시 시도해 주세요.");
            App.log("FileNotFoundException");
            e.printStackTrace();
            return;
        }

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationManager.isMyLocationEnabled())
            mLocationManager.disableMyLocation();
    }
}
