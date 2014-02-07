package com.nexters.vobble.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.*;
import android.media.MediaPlayer;
import android.os.*;
import android.view.*;
import android.widget.*;

import com.nexters.vobble.R;
import com.nexters.vobble.record.*;
import com.nexters.vobble.util.FileIOUtils;
import com.nexters.vobble.view.HoloCircularProgressBar;

public class RecordActivity extends BaseActivity implements View.OnClickListener {
	public static final int READY_MODE = 0;
    public static final int RECORD_MODE = 1;
	public static final int STOP_MODE = 2;
	public static final int PLAY_MODE = 3;

    public static final int RECORD_TIME_LIMIT = 7000; // ms 단위

	private ImageView ivRecordBtn;
	private ImageView ivResetBtn;
	private Button btnConfirm;

    private HoloCircularProgressBar mProgressBar;
    private ObjectAnimator mProgressBarAnimator;

    private RecordManager recordManager;
    private int currentMode = READY_MODE;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_record);

        recordManager = new RecordManager();

        initResources();
		initEvents();
		FileIOUtils.deleteSoundFile();
	}
	
	private void initResources() {
        ivRecordBtn = (ImageView)findViewById(R.id.iv_record_record_btn);
		ivResetBtn = (ImageView)findViewById(R.id.iv_record_re_btn);
		btnConfirm = (Button) findViewById(R.id.btn_record_confirm);
        mProgressBar = (HoloCircularProgressBar) findViewById(R.id.hcpb_record_progress);
	}
	
	private void initEvents() {
		ivRecordBtn.setOnClickListener(this);
		ivResetBtn.setOnClickListener(this);
		btnConfirm.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.iv_record_record_btn:
			recordStatusPattern();
			break;
		case R.id.iv_record_re_btn:
			resetRecord();
            break;
		case R.id.btn_record_confirm:
			confirm();
			break;
		}
	}

    private void confirm() {
        if (FileIOUtils.isExistSoundFile()) {
            Intent intent = new Intent(RecordActivity.this, MapActivity.class);
            startActivity(intent);
        } else {
            alert(R.string.error_not_record);
        }
    }

	private void recordStatusPattern() {
		switch (currentMode) {
		case READY_MODE:
            startRecord();
			break;
		case RECORD_MODE:
            stopRecord();
			break;
		case STOP_MODE:
            playRecord();
			break;
		default :
			break;
		}
	}

    private void playRecord() {
        currentMode = PLAY_MODE;
        ivRecordBtn.setImageResource(R.drawable.play2_btn);
        recordManager.startPlay(FileIOUtils.getVoiceFilePath(), new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                stopRecord();
            }
        });
    }

    private void stopRecord() {
        currentMode = STOP_MODE;
        ivRecordBtn.setImageResource(R.drawable.play_btn);
        recordManager.stopRecord();

        if (mProgressBarAnimator != null) {
            mProgressBarAnimator.removeAllListeners();
            mProgressBarAnimator.cancel();
        }
    }

    private void startRecord() {
        currentMode = RECORD_MODE;
        ivRecordBtn.setImageResource(R.drawable.record_stop_btn);
        recordManager.startRecord(FileIOUtils.getVoiceFilePath());

        if (mProgressBarAnimator != null) {
            mProgressBarAnimator.cancel();
        }
        animate(mProgressBar, 1f, RECORD_TIME_LIMIT);
    }

    private void resetRecord() {
        currentMode = READY_MODE;
        ivRecordBtn.setImageResource(R.drawable.record_record_btn);
        recordManager.stopRecord();

        if (mProgressBarAnimator != null) {
            mProgressBarAnimator.removeAllListeners();
            mProgressBarAnimator.cancel();
            mProgressBar.setProgress(0);
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
                progressBar.setProgress(progress);
                stopRecord();
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
}