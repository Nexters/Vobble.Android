package com.nexters.vobble.activity;

import java.io.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import com.nexters.vobble.R;
import com.nexters.vobble.util.CommonUtils;
import com.nexters.vobble.util.FileIOUtils;
import org.json.JSONObject;

import com.loopj.android.http.RequestParams;

import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;

import com.nexters.vobble.core.*;
import com.nexters.vobble.network.*;

public class MapActivity extends BaseActivity implements View.OnClickListener {
    public static final int REQUEST_TAKE_PHOTO = 1;
    public static final int REQUEST_PICK_FROM_GALLERY = 2;

    private ImageView ivPhoto;
	private Button btnSave;

    private Bitmap imageBitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_map);
		
		initResources();
		initEvents();
	}
	
	private void initResources() {
		ivPhoto = (ImageView) findViewById(R.id.iv_photo_view);
		btnSave = (Button) findViewById(R.id.btn_save);
	}
	
	private void initEvents() {
		ivPhoto.setOnClickListener(this);
		btnSave.setOnClickListener(this);
	}

    @Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.iv_photo_view :
            showDialogChoosingPhoto();
            break;
		case R.id.btn_save :
			try {
				executeCreateVobble();
			} catch (FileNotFoundException e) {
				Vobble.log("FileNotFoundException");
				alert(R.string.error_vobble_create);
			}
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
            ivPhoto.setImageBitmap(CommonUtils.getCroppedBitmap(imageBitmap, 450));
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
        ivPhoto.setImageBitmap(CommonUtils.getCroppedBitmap(imageBitmap, 450));
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FileIOUtils.deleteImageFile();
    }

    private void executeCreateVobble() throws FileNotFoundException {
        String url = String.format(URL.VOBBLES_CREATE,Vobble.getUserId(this));

        File voiceFile = FileIOUtils.getVoiceFile();

        RequestParams params = new RequestParams();
        params.put(Vobble.TOKEN, Vobble.getToken(this));
        params.put(Vobble.VOICE, voiceFile);
        params.put(Vobble.LATITUDE, "127");
        params.put(Vobble.LONGITUDE, "37");

        if (imageBitmap != null) {
            FileIOUtils.saveBitmapToImageFile(imageBitmap);
            File imageFile = FileIOUtils.getImageFile();
            params.put(Vobble.IMAGE, imageFile);
        }
        
        HttpUtil.post(url, null, params, new VobbleResponseHandler(MapActivity.this) {

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
                Intent intent = new Intent(MapActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    			startActivity(intent);
            }
        });
    }
}
