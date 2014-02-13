package com.nexters.vobble.activity;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.view.View.OnTouchListener;
import android.view.animation.*;
import android.widget.*;

import com.nexters.vobble.*;

public class TutorialActivity extends Activity implements OnTouchListener {
	private ViewFlipper viewflipper;
	private GestureDetector gestureDetector;

	Animation animFlipInForward;
	Animation animFlipOutForward;
	Animation animFlipInBackward;
	Animation animFlipOutBackward;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_tutorial);
		
		initResources();
		initAnimations();
		initEvents();
	}
	
	private void initResources() {
		viewflipper = (ViewFlipper) findViewById(R.id.viewflipper);
	}
	
	private void initAnimations() {
		animFlipInForward = AnimationUtils.loadAnimation(this, R.anim.flipin);
		animFlipOutForward = AnimationUtils .loadAnimation(this, R.anim.flipout);
		animFlipInBackward = AnimationUtils.loadAnimation(this, R.anim.flipin_reverse);
		animFlipOutBackward = AnimationUtils.loadAnimation(this, R.anim.flipout_reverse);
	}

	private void initEvents() {
		viewflipper.setOnTouchListener(this);
		gestureDetector = new GestureDetector(getApplicationContext(),simpleOnGestureListener);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return gestureDetector.onTouchEvent(event);
	}

	GestureDetector.SimpleOnGestureListener simpleOnGestureListener = new GestureDetector.SimpleOnGestureListener() {

        @Override
		public boolean onDown(MotionEvent event) {
			return true;
		}

        @Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,float velocityY) {
			float sensitivity = 50;
			if ((e1.getX() - e2.getX()) > sensitivity) {
				if (viewflipper.getDisplayedChild() == viewflipper.getChildCount() - 1) {
                    Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                    startActivity(intent);
					finish();
				} else {
					viewflipper.setInAnimation(animFlipInForward);
					viewflipper.setOutAnimation(animFlipOutForward);
					viewflipper.showNext();
				}
			} else if ((e2.getX() - e1.getX()) > sensitivity) {
				if (viewflipper.getDisplayedChild()>0){
					viewflipper.setInAnimation(animFlipInBackward);
					viewflipper.setOutAnimation(animFlipOutBackward);
					viewflipper.showPrevious();
				}
			}
			return true;
		}
	};
}
