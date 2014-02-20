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
import com.nexters.vobble.listener.ImageViewTouchListener;
import com.nexters.vobble.record.RecordManager;
import com.nexters.vobble.util.ImageManagingHelper;
import com.nexters.vobble.util.TempFileManager;
import com.nexters.vobble.view.HoloCircularProgressBar;

public class CreateVobbleActivity extends BaseActivity {
    public static final int RECORD_TIME_LIMIT = 10000; // ms 단위

    public static final int REQUEST_TAKE_PHOTO = 1;
    public static final int REQUEST_PICK_FROM_GALLERY = 2;

    private int mIvPhotoWidth;
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

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        mIvPhotoWidth = mIvPhotoBtn.getWidth();
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
        ImageViewTouchListener ivTouchListener = new ImageViewTouchListener();
        mIvPhotoBtn.setOnTouchListener(ivTouchListener);
        mIvPhotoBtn.setOnClickListener(btnClickListener);
        mIvRecordBtn.setOnTouchListener(ivTouchListener);
        mIvRecordBtn.setOnClickListener(btnClickListener);
		mIvResetBtn.setOnClickListener(btnClickListener);
		mBtnConfirm.setOnClickListener(btnClickListener);
	}

	private View.OnClickListener btnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.iv_record_photo_btn:
                    showDialogChoosingPhoto();
                    break;
                case R.id.iv_record_record_btn:
                    decideRecordingOrPlayingActionByRecordingStatus();
                    break;
                case R.id.iv_record_re_btn:
                    stopRecordingOrPlaying();
                    resetRecording();
                    break;
                case R.id.btn_record_confirm:
                    executeConfirm();
                    break;
            }
        }
    };

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
        setCommonPutExtrasToIntent(pickFromGalleryIntent);
        startActivityForResult(pickFromGalleryIntent, REQUEST_PICK_FROM_GALLERY);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        setCommonPutExtrasToIntent(takePictureIntent);
        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
    }

    private void setCommonPutExtrasToIntent(Intent intent) {
        intent.putExtra("crop", "true");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, TempFileManager.getImageFileUri());
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("outputX", 360);
        intent.putExtra("outputY", 360);
        intent.putExtra("aspectX", 360);
        intent.putExtra("aspectY", 360);
        intent.putExtra("scale", true);
    }

    private void setPicFromUri() {
        Uri uri = TempFileManager.getImageFileUri();
        InputStream imageStream = null;
        try {
            imageStream = getContentResolver().openInputStream(uri);
            
            BitmapFactory.Options o = new BitmapFactory.Options();
    		o.inJustDecodeBounds = true;
            mImageBitmap = BitmapFactory.decodeStream(imageStream, null, o);
            imageStream.close();
            
            int width = o.outWidth;
    		int height = o.outHeight;
    		
    		int scaleFactor = Math.max(width / 300, height / 300);

    		Bitmap b = null;
    		imageStream = getContentResolver().openInputStream(uri);
    		o = new BitmapFactory.Options();
    		o.inSampleSize = scaleFactor;
    		b = BitmapFactory.decodeStream(imageStream, null, o);
    		
            mIvPhotoBtn.setImageBitmap(ImageManagingHelper.getCroppedBitmap(b, mIvPhotoWidth));
            imageStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void decideRecordingOrPlayingActionByRecordingStatus() {
		if (mRecordManager.isRecording()) {
            stopRecording();
        } else if (mRecordManager.isReadyToRecording()) {
            startRecording();
        } else if (mRecordManager.isStopRecording()) {
            startPlaying();
        }
	}

    private void startRecording() {
        mRecordManager.startRecording(TempFileManager.getVoiceFilePath());
        startCircularProgress();
        mIvRecordBtn.setImageResource(R.drawable.record_stop_btn);
    }

    private void stopRecording() {
        mRecordManager.stopRecording();
        stopCircularProgress();
        mIvRecordBtn.setImageResource(R.drawable.play_btn);
    }

    private void resetRecording() {
        mRecordManager.resetRecording();
        initCircularProgress();
        mIvRecordBtn.setImageResource(R.drawable.record_record_btn);
        TempFileManager.deleteVoiceFile();
    }

    private void startPlaying() {
        mIvRecordBtn.setImageResource(R.drawable.play_btn_o);
        mRecordManager.startPlaying(TempFileManager.getVoiceFilePath(), new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                stopPlaying();
            }
        });
    }

    private void stopPlaying() {
        mRecordManager.stopPlaying();
        mIvRecordBtn.setImageResource(R.drawable.play_btn);
    }

    private void stopRecordingOrPlaying() {
        if (mRecordManager.isPlaying()) {
            stopPlaying();
        } else if (mRecordManager.isRecording()) {
            stopRecording();
        }
    }

    private void startCircularProgress() {
        if (mCircularProgressBarAnimator != null)
            mCircularProgressBarAnimator.cancel();
        animate(mCircularProgressBar, 1f, RECORD_TIME_LIMIT);
    }

    private void stopCircularProgress() {
        if (mCircularProgressBarAnimator != null) {
            mCircularProgressBarAnimator.removeAllListeners();
            mCircularProgressBarAnimator.cancel();
        }
    }

    private void initCircularProgress() {
        mCircularProgressBar.setProgress(0);
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
                stopRecording();
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

    private void executeConfirm() {
        if (mRecordManager.isRecording())
            stopRecording();

        if (!TempFileManager.isExistSoundFile()) {
            alert(R.string.error_not_exist_voice);
        } else if (mImageBitmap == null) {
            alert(R.string.error_not_exist_image);
        } else {
            Intent intent = new Intent(CreateVobbleActivity.this, ConfirmVobbleActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_TAKE_PHOTO:
                if (resultCode == Activity.RESULT_OK)
                    setPicFromUri();
                break;
            case REQUEST_PICK_FROM_GALLERY:
                if (resultCode == Activity.RESULT_OK)
                    setPicFromUri();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TempFileManager.deleteImageFile();
        TempFileManager.deleteVoiceFile();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopRecordingOrPlaying();
    }
}