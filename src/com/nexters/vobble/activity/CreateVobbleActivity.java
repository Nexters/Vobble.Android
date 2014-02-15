package com.nexters.vobble.activity;

import java.io.FileNotFoundException;
import java.io.InputStream;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.nexters.vobble.R;
import com.nexters.vobble.record.RecordManager;
import com.nexters.vobble.util.CommonUtils;
import com.nexters.vobble.util.FileIOUtils;
import com.nexters.vobble.view.HoloCircularProgressBar;

public class CreateVobbleActivity extends BaseActivity implements View.OnClickListener {
    public static final int RECORD_READY_MODE = 0;
    public static final int RECORD_RECORDING_MODE = 1;
	public static final int RECORD_STOP_MODE = 2;
	public static final int RECORD_PLAY_MODE = 3;

    public static final int RECORD_TIME_LIMIT = 10000; // ms 단위

    public static final int REQUEST_TAKE_PHOTO = 1;
    public static final int REQUEST_PICK_FROM_GALLERY = 2;

    private int mCurrentRecordMode = RECORD_READY_MODE;

    private ImageView mIvPhotoBtn;
    private ImageView mIvRecordBtn;
	private ImageView mIvResetBtn;
	private Button mBtnConfirm;
    private HoloCircularProgressBar mCircularProgressBar;
    private ObjectAnimator mCircularProgressBarAnimator;

    private RecordManager mRecordManager;
    private Bitmap mImageBitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_create_vobble);

        initResources();
		initEvents();
	}
	
	private void initResources() {
        mRecordManager = new RecordManager();
        mIvPhotoBtn = (ImageView) findViewById(R.id.iv_record_photo_btn);
        mIvRecordBtn = (ImageView) findViewById(R.id.iv_record_record_btn);
		mIvResetBtn = (ImageView) findViewById(R.id.iv_record_re_btn);
		mBtnConfirm = (Button) findViewById(R.id.btn_record_confirm);
        mCircularProgressBar = (HoloCircularProgressBar) findViewById(R.id.hcpb_record_progress);
	}
	
	private void initEvents() {
		mIvPhotoBtn.setOnClickListener(this);
        mIvRecordBtn.setOnClickListener(this);
		mIvResetBtn.setOnClickListener(this);
		mBtnConfirm.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
        case R.id.iv_record_photo_btn:
            showDialogChoosingPhoto();
            break;
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

    private void showDialogChoosingPhoto() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.activity_confirm_vobble_choosing_photo_dialog_title)
               .setItems(R.array.menu_choosing_photo_dialog_items, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int which) {
                       switch (which) {
                           case 0:
                               dispatchTakePictureIntent();
                               break;
                           case 1:
                               dispatchPickFromGalleryIntent();
                               break;
                       }
                   }
               });
        builder.show();
    }

    private void dispatchPickFromGalleryIntent() {
        Intent pickFromGalleryIntent = new Intent(Intent.ACTION_PICK);
        pickFromGalleryIntent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(pickFromGalleryIntent, REQUEST_PICK_FROM_GALLERY);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
    }

    private void setPicFromCamera(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            mImageBitmap = (Bitmap) extras.get("data");
            mIvPhotoBtn.setImageBitmap(CommonUtils.getCroppedBitmap(mImageBitmap, 450));
        }
    }

    private void setPicFromGallery(Intent data) {
        Uri uri = data.getData();
        InputStream imageStream = null;
        try {
            imageStream = getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mImageBitmap = BitmapFactory.decodeStream(imageStream);
        mIvPhotoBtn.setImageBitmap(CommonUtils.getCroppedBitmap(mImageBitmap, 450));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_TAKE_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    setPicFromCamera(data);
                }
                break;
            case REQUEST_PICK_FROM_GALLERY:
                if (resultCode == Activity.RESULT_OK) {
                    setPicFromGallery(data);
                }
                break;
        }
    }

    private void confirm() {
        if (mCurrentRecordMode == RECORD_RECORDING_MODE) {
            stopRecord();
        }

        if (!FileIOUtils.isExistSoundFile()) {
            alert(R.string.error_not_exist_voice);
            return;
        } else if (mImageBitmap == null) {
            alert(R.string.error_not_exist_image);
            return;
        } else {
            FileIOUtils.saveBitmapToImageFile(mImageBitmap);
            Intent intent = new Intent(CreateVobbleActivity.this, ConfirmVobbleActivity.class);
            startActivity(intent);
        }
    }

	private void recordStatusPattern() {
		switch (mCurrentRecordMode) {
		case RECORD_READY_MODE:
            startRecord();
			break;
		case RECORD_RECORDING_MODE:
            stopRecord();
			break;
		case RECORD_STOP_MODE:
            startPlay();
			break;
		default :
			break;
		}
	}

    private void startRecord() {
        mCurrentRecordMode = RECORD_RECORDING_MODE;
        mIvRecordBtn.setImageResource(R.drawable.record_stop_btn);
        mRecordManager.startRecord(FileIOUtils.getVoiceFilePath());

        if (mCircularProgressBarAnimator != null) {
            mCircularProgressBarAnimator.cancel();
        }
        animate(mCircularProgressBar, 1f, RECORD_TIME_LIMIT);
    }

    private void stopRecord() {
        mCurrentRecordMode = RECORD_STOP_MODE;
        mIvRecordBtn.setImageResource(R.drawable.play_btn);
        mRecordManager.stopRecord();

        if (mCircularProgressBarAnimator != null) {
            mCircularProgressBarAnimator.removeAllListeners();
            mCircularProgressBarAnimator.cancel();
        }
    }

    private void resetRecord() {
        if (mCurrentRecordMode == RECORD_READY_MODE) {
            return;
        }

        if (mCurrentRecordMode == RECORD_PLAY_MODE) {
            stopPlay();
        } else if (mCurrentRecordMode == RECORD_RECORDING_MODE) {
            stopRecord();
        } else if (mCurrentRecordMode == RECORD_STOP_MODE) {

        }

        mCurrentRecordMode = RECORD_READY_MODE;
        mIvRecordBtn.setImageResource(R.drawable.record_record_btn);
        mCircularProgressBar.setProgress(0);
        FileIOUtils.deleteSoundFile();
    }

    private void startPlay() {
        mCurrentRecordMode = RECORD_PLAY_MODE;
        mIvRecordBtn.setImageResource(R.drawable.play2_btn);
        mRecordManager.startPlay(FileIOUtils.getVoiceFilePath(), new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                stopPlay();
            }
        });
    }

    private void stopPlay() {
        mCurrentRecordMode = RECORD_STOP_MODE;
        mIvRecordBtn.setImageResource(R.drawable.play_btn);
        mRecordManager.stopPlay();
    }

    private void animate(final HoloCircularProgressBar progressBar, final float progress, final int duration) {
        mCircularProgressBarAnimator = ObjectAnimator.ofFloat(progressBar, "progress", progress);
        mCircularProgressBarAnimator.setDuration(duration);
        mCircularProgressBarAnimator.addListener(new Animator.AnimatorListener() {

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

        mCircularProgressBarAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(final ValueAnimator animation) {
                progressBar.setProgress((Float) animation.getAnimatedValue());
            }
        });
        mCircularProgressBarAnimator.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FileIOUtils.deleteImageFile();
        FileIOUtils.deleteSoundFile();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mCurrentRecordMode == RECORD_RECORDING_MODE) {
            stopRecord();
        } else if (mCurrentRecordMode == RECORD_PLAY_MODE) {
            stopPlay();
        }
    }
}