package com.nexters.vobble.activity;

import java.io.*;

import com.nexters.vobble.entity.User;
import com.nexters.vobble.entity.Vobble;
import com.nexters.vobble.listener.CustomOnCalloutOverlayListener;
import com.nexters.vobble.listener.CustomOnMapStateChangeListener;
import com.nexters.vobble.listener.CustomOnStateChangeListener;
import com.nhn.android.maps.maplib.NGeoPoint;
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

public class ConfirmVobbleActivity extends BaseNMapActivity {
    private int mIvPhotoWidth;
    private boolean loadImage = false;
    private LocationHelper mLocationHelper;
    private Location mLocation;

    private NMapView mMapView;
    private NMapController mMapController;
    private NMapOverlayManager mOverlayManager;
    private NMapViewerResourceProvider mMapViewerResourceProvider;
    private NMapPOIdataOverlay poiDataOverlay;

    private ImageView mIvPhoto;
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

	private void initResources() {
        mIvPhoto = (ImageView) findViewById(R.id.iv_photo_view);
		mBtnSave = (Button) findViewById(R.id.btn_save);
        mMapView = (NMapView) findViewById(R.id.map_view);
	}
	
	private void initEvents() {
		mBtnSave.setOnClickListener(btnClickListener);
	}

    private void initMapView() {
        mMapView.setApiKey(App.NMAP_API_KEY);
        mMapView.setClickable(true);
        mMapController = mMapView.getMapController();
    }

    private void initLocation() {
        mLocation = null;
        mLocationHelper = new LocationHelper(this, mLocationListener);
        showShortToast("위치 정보를 가져옵니다.");
    }

    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (mLocation == null) {
                mLocation = location;
                initOverlayInMapView();
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    private void initOverlayInMapView() {
        CustomOnCalloutOverlayListener onCalloutOverlayListener = new CustomOnCalloutOverlayListener();
        CustomOnStateChangeListener onPOIdataStateChangeListener = new CustomOnStateChangeListener();

        mMapViewerResourceProvider = new NMapViewerResourceProvider(this);
        mOverlayManager = new NMapOverlayManager(this, mMapView, mMapViewerResourceProvider);
        mOverlayManager.setOnCalloutOverlayListener(onCalloutOverlayListener);

        int markerId = NMapPOIflagType.PIN;
        NMapPOIdata poiData = new NMapPOIdata(1, mMapViewerResourceProvider);
        poiData.beginPOIdata(1);
        poiData.addPOIitem(mLocation.getLongitude(), mLocation.getLatitude(), "You're in here.", markerId, 0);
        poiData.endPOIdata();

        if (poiDataOverlay != null)
            poiDataOverlay.removeAllPOIdata();

        poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);
        poiDataOverlay.showAllPOIdata(0);
        poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);

        mMapController.setMapCenter(new NGeoPoint(mLocation.getLongitude(), mLocation.getLatitude()), 10);
    }

    private void initImage() {
        Bitmap imageBitmap = BitmapFactory.decodeFile(TempFileManager.getImageFile().getAbsolutePath());
        mIvPhoto.setImageBitmap(ImageManagingHelper.getCroppedBitmap(imageBitmap, mIvPhotoWidth));
    }

    private View.OnClickListener btnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_save:
                    executeSaving();
                    break;
            }
        }
    };

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationHelper != null)
            mLocationHelper.destroy();
    }
}
