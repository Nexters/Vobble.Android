package com.nexters.vobble.activity;

import android.animation.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.*;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.*;
import android.net.Uri;
import android.os.*;
import android.provider.MediaStore;
import android.view.*;
import android.widget.*;

import com.nexters.vobble.*;
import com.nexters.vobble.record.*;
import com.nexters.vobble.util.*;
import com.nexters.vobble.view.*;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class RecordActivity extends BaseActivity implements View.OnClickListener {
    public static final int READY_MODE = 0;
    public static final int RECORD_MODE = 1;
	public static final int STOP_MODE = 2;
	public static final int PLAY_MODE = 3;

    public static final int REQUEST_TAKE_PHOTO = 1;
    public static final int REQUEST_PICK_FROM_GALLERY = 2;

    public static final int RECORD_TIME_LIMIT = 7000; // ms 단위

    private ImageView ivPhotoBtn;
    private ImageView ivRecordBtn;
	private ImageView ivResetBtn;
	private Button btnConfirm;

    private HoloCircularProgressBar mProgressBar;
    private ObjectAnimator mProgressBarAnimator;

    private RecordManager recordManager;
    private int currentMode = READY_MODE;

    private Bitmap imageBitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_record);

        initResources();
		initEvents();
	}
	
	private void initResources() {
        recordManager = new RecordManager();
        ivPhotoBtn = (ImageView) findViewById(R.id.iv_record_photo_btn);
        ivRecordBtn = (ImageView) findViewById(R.id.iv_record_record_btn);
		ivResetBtn = (ImageView) findViewById(R.id.iv_record_re_btn);
		btnConfirm = (Button) findViewById(R.id.btn_record_confirm);
        mProgressBar = (HoloCircularProgressBar) findViewById(R.id.hcpb_record_progress);
	}
	
	private void initEvents() {
		ivPhotoBtn.setOnClickListener(this);
        ivRecordBtn.setOnClickListener(this);
		ivResetBtn.setOnClickListener(this);
		btnConfirm.setOnClickListener(this);
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
        builder.setTitle(R.string.activity_map_choosing_photo_dialog_title)
               .setItems(R.array.activity_map_choosing_photo_dialog_items, new DialogInterface.OnClickListener() {
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
            imageBitmap = (Bitmap) extras.get("data");
            ivPhotoBtn.setImageBitmap(CommonUtils.getCroppedBitmap(imageBitmap, 450));
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
        imageBitmap = BitmapFactory.decodeStream(imageStream);
        ivPhotoBtn.setImageBitmap(CommonUtils.getCroppedBitmap(imageBitmap, 450));
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
        if (!FileIOUtils.isExistSoundFile()) {
            alert(R.string.error_not_record);
        } else if (imageBitmap == null) {
            alert(R.string.error_not_image);
        } else {
            FileIOUtils.saveBitmapToImageFile(imageBitmap);
            Intent intent = new Intent(RecordActivity.this, MapActivity.class);
            startActivity(intent);
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
        if (currentMode == PLAY_MODE) {
            showShortToast("재생이 끝나고 다시 시도해 주세요.");
            return;
        }
        currentMode = READY_MODE;
        ivRecordBtn.setImageResource(R.drawable.record_record_btn);
        recordManager.stopRecord();
        FileIOUtils.deleteSoundFile();

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FileIOUtils.deleteImageFile();
        FileIOUtils.deleteSoundFile();
    }
}