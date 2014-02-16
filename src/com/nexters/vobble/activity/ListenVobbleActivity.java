package com.nexters.vobble.activity;

import android.animation.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;

import com.nexters.vobble.*;
import com.nexters.vobble.core.*;
import com.nexters.vobble.entity.Vobble;
import com.nexters.vobble.nmap.*;
import com.nexters.vobble.record.RecordManager;
import com.nexters.vobble.util.*;
import com.nexters.vobble.view.*;
import com.nhn.android.maps.*;
import com.nhn.android.maps.overlay.*;
import com.nhn.android.mapviewer.overlay.*;

public class ListenVobbleActivity extends BaseNMapActivity implements View.OnClickListener {
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
        tvUsername.setText(vobble.getUsername());
        tvCreatedAt.setText(vobble.getCreatedAt());
        ImageManagingHelper.loadAndAttachCroppedImage(vobbleImg, vobble.getImageUrl());
	}

	private void initMapView() {
        mMapView = (NMapView) findViewById(R.id.vobble_map_view);
        mMapView.setApiKey(App.NMAP_API_KEY);
        mMapView.setClickable(false);
        NMapViewerResourceProvider mMapViewerResourceProvider = new NMapViewerResourceProvider(this);
        NMapOverlayManager mOverlayManager = new NMapOverlayManager(this, mMapView, mMapViewerResourceProvider);

        int markerId = NMapPOIflagType.PIN;
        NMapPOIdata poiData = new NMapPOIdata(1, mMapViewerResourceProvider);
        poiData.beginPOIdata(1);
        poiData.addPOIitem(vobble.getLongitude(), vobble.getLatitude(), "현재 위치", markerId, 0);
        poiData.endPOIdata();

        NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);
        poiDataOverlay.showAllPOIdata(0);
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
