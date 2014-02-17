package com.nexters.vobble.activity;

import android.animation.*;
import android.content.*;
import android.graphics.Rect;
import android.os.*;
import android.view.*;
import android.widget.*;

import com.loopj.android.http.RequestParams;
import com.nexters.vobble.*;
import com.nexters.vobble.core.*;
import com.nexters.vobble.entity.User;
import com.nexters.vobble.entity.Vobble;
import com.nexters.vobble.network.APIResponseHandler;
import com.nexters.vobble.network.HttpUtil;
import com.nexters.vobble.network.URL;
import com.nexters.vobble.nmap.*;
import com.nexters.vobble.record.RecordManager;
import com.nexters.vobble.util.*;
import com.nexters.vobble.view.*;
import com.nhn.android.maps.*;
import com.nhn.android.maps.overlay.*;
import com.nhn.android.mapviewer.overlay.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class ListenVobbleActivity extends BaseNMapActivity implements View.OnClickListener {
	private boolean loadImage = false;

    private NMapView mMapView;
	private Vobble vobble;
	private ImageView vobbleImg;
    private TextView tvUsername;
    private TextView tvCreatedAt;
	
	private HoloCircularProgressBar mProgressBar;
	private ObjectAnimator mProgressBarAnimator;
    private RecordManager mRecordManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_listen_vobble);

        initResources();
		initEvents();
        initView();
		initMapView();
        initUserInfo();
		startPlaying();
	}

    private void initResources() {
		Intent intent = getIntent();
		vobble = (Vobble) intent.getExtras().getSerializable("vobble");

        mRecordManager = new RecordManager();
        mProgressBar = (HoloCircularProgressBar) findViewById(R.id.hcpb_vobble_progress);
		vobbleImg = (ImageView) findViewById(R.id.voice_photo);
        tvUsername = (TextView) findViewById(R.id.tv_username);
        tvCreatedAt = (TextView) findViewById(R.id.tv_created_at);
	}

    private void initEvents() {
        vobbleImg.setOnClickListener(this);
    }

	private void initView() {
        tvCreatedAt.setText(vobble.getCreatedAt());
	}

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        // [주의] onCreate에서 보블 이미지를 이미지뷰에 붙이면 ImageView의 getWidth()가 0을 반환해서 제대로 붙지 않음
        // onWindowFocusChanged부터 ImageView가 Window에 잘 붙어서 올바른 getWidth()를 반환하므로
        // 여기에서 초기화시켜야 함 - by 수완
        if (!loadImage) {
            loadImage = true;
            ImageManagingHelper.loadAndAttachCroppedImage(vobbleImg, vobble.getImageUrl());
        }
    }

	private void initMapView() {
        mMapView = (NMapView) findViewById(R.id.vobble_map_view);
        mMapView.setApiKey(App.NMAP_API_KEY);
        mMapView.setClickable(true);

        NMapViewerResourceProvider mMapViewerResourceProvider = new NMapViewerResourceProvider(this);
        NMapOverlayManager mOverlayManager = new NMapOverlayManager(this, mMapView, mMapViewerResourceProvider);
        mOverlayManager.setOnCalloutOverlayListener(onCalloutOverlayListener);

        int markerId = NMapPOIflagType.PIN;
        NMapPOIdata poiData = new NMapPOIdata(1, mMapViewerResourceProvider);
        poiData.beginPOIdata(1);
        poiData.addPOIitem(vobble.getLongitude(), vobble.getLatitude(), "This vobble was created in here.", markerId, 0);
        poiData.endPOIdata();

        NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);
        poiDataOverlay.showAllPOIdata(0);
        poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);
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

    private void initUserInfo() {
        String url = String.format(URL.USER_INFO, vobble.getUserId());

        HttpUtil.get(url, null, null, new APIResponseHandler(ListenVobbleActivity.this) {

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
                JSONObject dataObj = null;
                try {
                    dataObj = response.getJSONObject("user");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (dataObj != null) {
                    User user = new User().build(dataObj);
                    tvUsername.setText(user.getUsername());
                }
            }
        });
    }

    private void startPlaying() {
		mRecordManager.startPlaying(vobble.getVoiceUrl());
        startCircularProgress(mRecordManager.getDurationOfCurrentMedia());
	}

    private void stopPlaying() {
        mRecordManager.stopPlaying();
        initCircularProgress();
        stopCircularProgress();
    }

    private void startCircularProgress(int duration) {
        if (mProgressBarAnimator != null)
            mProgressBarAnimator.cancel();
        animate(mProgressBar, 1f, duration);
    }

    private void stopCircularProgress() {
        if (mProgressBarAnimator != null) {
            mProgressBarAnimator.removeAllListeners();
            mProgressBarAnimator.cancel();
        }
    }

    private void initCircularProgress() {
        mProgressBar.setProgress(0);
    }

    private void animate(final HoloCircularProgressBar progressBar, final float progress, final int duration) {
        mProgressBarAnimator = ObjectAnimator.ofFloat(progressBar, "progress", progress);
        mProgressBarAnimator.setDuration(duration);
        mProgressBarAnimator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationCancel(final Animator animation) {
            }

            @Override
            public void onAnimationEnd(final Animator animation) {
                stopPlaying();
            }

            @Override
            public void onAnimationRepeat(final Animator animation) {
            }

            @Override
            public void onAnimationStart(final Animator animation) {

            }
        });

        mProgressBarAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(final ValueAnimator animation) {
                progressBar.setProgress((Float) animation.getAnimatedValue());
            }
        });
        mProgressBarAnimator.start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        case R.id.voice_photo:
            if (mRecordManager.isPlaying()) {
                stopPlaying();
            } else if (mRecordManager.isStopRecording()) {
                startPlaying();
            }
            break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mRecordManager.isPlaying())
            stopPlaying();
    }
}
