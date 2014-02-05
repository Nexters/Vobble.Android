package com.nexters.vobble.view;

import android.content.*;
import android.graphics.*;
import android.util.*;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

import com.nexters.vobble.*;
import com.nexters.vobble.listener.*;

public class VobbleWidget extends FrameLayout implements OnClickListener {

	private Context context = null;
	private LayoutInflater inflater = null;
	private View view = null;

	private CircleImageView ci = null;
	private FrameLayout vobbleFrameLayout = null;
	private ImageView vobbleImageView = null;
	private ImageView removeImageView = null;
	private ImageView clickVobbleImageView = null;

	private String test = "";
	private VobbleClickListener vobbleClickListener = null;

	private CircleProgressBar cp = null;

	private String voicePath = "";
	
	public VobbleWidget(Context context) {
		super(context);
		this.context = context;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//this.view = inflater.inflate(R.layout.view_vobble, null);

		//addView(view);

		setOnClickListener(this);

		ci = new CircleImageView(context);
		cp = new CircleProgressBar(context);

		//vobbleFrameLayout = (FrameLayout) view
		//		.findViewById(R.id.vobbleFrameLayout);
		//vobbleImageView = (ImageView) view.findViewById(R.id.vobbleImageView);
		//removeImageView = (ImageView) view.findViewById(R.id.removeImageView);
		//clickVobbleImageView = (ImageView) view.findViewById(R.id.clickVobbleImageView);
		
		//vobbleFrameLayout.addView(cp, 0);
		// Handler handler = new Handler();
		// handler.post(new Runnable() {
		// public void run() {
		// for (int i = 0; i < 360; i++) {
		// cp.a = i;
		// }
		// }
		// });

	}
	public ImageView getClickVobbleImageView() {
		return clickVobbleImageView;
	}

	public String getVoicePath() {
		return voicePath;
	}

	public void setVoicePath(String voicePath) {
		this.voicePath = voicePath;
	}

	public CircleProgressBar getCp() {
		return cp;
	}

	public void setCp(CircleProgressBar cp) {
		this.cp = cp;
	}

	public void setVobbleImage(Bitmap bmp) {
		vobbleImageView.setImageBitmap(ci.getCroppedBitmap(bmp, 450));
	}

	public void onClick(View v) {
		VobbleWidget widget = (VobbleWidget) v;
		Log.d("test", "qwreewqrerwqrwe");
	}

	public void setVobbleClickListener(VobbleClickListener vobbleClickListener) {
		this.vobbleClickListener = vobbleClickListener;
	}

	public ImageView getVobbleImageView() {
		return vobbleImageView;
	}

	public void setVobbleImageView(ImageView vobbleImageView) {
		this.vobbleImageView = vobbleImageView;
	}

	public ImageView getRemoveImageView() {
		return removeImageView;
	}

	public void setRemoveImageView(ImageView removeImageView) {
		this.removeImageView = removeImageView;
	}

	// //////test
	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}
	// /////

}
