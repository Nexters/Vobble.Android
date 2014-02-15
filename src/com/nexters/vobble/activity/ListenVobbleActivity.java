package com.nexters.vobble.activity;

import android.animation.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.text.*;
import android.view.*;
import android.widget.*;

import com.nexters.vobble.*;
import com.nexters.vobble.core.*;
import com.nexters.vobble.network.*;
import com.nexters.vobble.nmap.*;
import com.nexters.vobble.record.RecordManager;
import com.nexters.vobble.util.*;
import com.nexters.vobble.view.*;
import com.nhn.android.maps.*;
import com.nhn.android.maps.overlay.*;
import com.nhn.android.mapviewer.overlay.*;
import com.nostra13.universalimageloader.core.*;
import com.nostra13.universalimageloader.core.assist.*;

public class ListenVobbleActivity extends BaseNMapActivity implements View.OnClickListener {
	private final int STOP_MODE = 0;
    private final int PLAY_MODE = 1;
    private int currentMode = STOP_MODE;

    private NMapView mMapView;
	private Voice voice;
	private ImageView vobbleImg;
	
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
		playVobble();
	}

    private void initResources() {
		Intent intent = getIntent();
		voice = (Voice) intent.getExtras().getSerializable("vobble");

        mRecordManager = new RecordManager();
        mProgressBar = (HoloCircularProgressBar) findViewById(R.id.hcpb_vobble_progress);
		vobbleImg = (ImageView) findViewById(R.id.voice_photo);
	}

    private void initEvents() {
        vobbleImg.setOnClickListener(this);
    }

	private void initView() {
		if (!TextUtils.isEmpty(voice.imgUri)) {
			ImageSize targetSize = new ImageSize(450, 450);
			DisplayImageOptions options = new DisplayImageOptions.Builder()
			// TODO- 이미지 로딩 중 실패 이미지 넣기
	        /*
			.showImageOnLoading(R.drawable.ic_stub)
	        .showImageForEmptyUri(R.drawable.ic_empty)
	        .showImageOnFail(R.drawable.ic_error)
	        */
	        .build();
			
			ImageLoader.getInstance().loadImage(voice.getImageUrl(), targetSize, options, new SimpleImageLoadingListener() {
			    @Override
			    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			    	vobbleImg.setImageBitmap(CommonUtils.getCroppedBitmap(loadedImage, 450));
			    }
			});			
		}
	}
	private void initMapView() {
        mMapView = (NMapView) findViewById(R.id.vobble_map_view);
        mMapView.setApiKey(Vobble.NMAP_API_KEY);
        mMapView.setClickable(false);
        NMapViewerResourceProvider mMapViewerResourceProvider = new NMapViewerResourceProvider(this);
        NMapOverlayManager mOverlayManager = new NMapOverlayManager(this, mMapView, mMapViewerResourceProvider);

        int markerId = NMapPOIflagType.PIN;
        NMapPOIdata poiData = new NMapPOIdata(1, mMapViewerResourceProvider);
        poiData.beginPOIdata(1);
        poiData.addPOIitem(voice.longitude, voice.latitude, "현재 위치", markerId, 0);
        poiData.endPOIdata();

        NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);
        poiDataOverlay.showAllPOIdata(0);
    }

    private void playVobble() {
		currentMode = PLAY_MODE;
        mRecordManager.startPlay(voice.getStreamingVoiceUrl());
        animate(mProgressBar, 1f, mRecordManager.getDurationOfCurrentMedia());
	}

    private void stopVobble() {
        currentMode = STOP_MODE;
        mRecordManager.stopPlay();
        mProgressBar.setProgress(0);

        if (mProgressBarAnimator != null) {
            mProgressBarAnimator.removeAllListeners();
            mProgressBarAnimator.cancel();
        }
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
                stopVobble();
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
            if (currentMode == PLAY_MODE) {
                stopVobble();
            } else if (currentMode == STOP_MODE) {
                playVobble();
            }
            break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (currentMode == PLAY_MODE) {
            stopVobble();
        }
    }
}
