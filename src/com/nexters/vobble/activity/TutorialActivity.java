package com.nexters.vobble.activity;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.view.View.OnTouchListener;
import android.view.animation.*;
import android.widget.*;

import com.nexters.vobble.*;

public class TutorialActivity extends Activity implements OnTouchListener{
	private ViewFlipper viewflipper;
	private WindowManager wm = null;
	private GestureDetector gestureDetector;
	private Boolean isFirst=false;

	Animation animFlipInForeward;
	Animation animFlipOutForeward;
	Animation animFlipInBackward;
	Animation animFlipOutBackward;

	// 터치 이벤트 발생 지점의 x좌표 저장
	private float xAtDown;
	private float xAtUp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_tutorial);
		
		initResource();
		initAnimation();
		
		initEvent();		
		
	}
	
	private void initResource() {
		viewflipper = (ViewFlipper) findViewById(R.id.viewflipper);
	}
	
	private void initAnimation() {
		animFlipInForeward = AnimationUtils.loadAnimation(this, R.anim.flipin);
		animFlipOutForeward = AnimationUtils .loadAnimation(this, R.anim.flipout);
		animFlipInBackward = AnimationUtils.loadAnimation(this, R.anim.flipin_reverse);
		animFlipOutBackward = AnimationUtils.loadAnimation(this, R.anim.flipout_reverse);
	}

	private void initEvent() {
		viewflipper.setOnTouchListener(this);
		wm = (WindowManager) getSystemService(Service.WINDOW_SERVICE);
		gestureDetector = new GestureDetector(getApplicationContext(),simpleOnGestureListener);
	}
	
	public void finishTutorial() {
		if (wm != null) {
			wm.removeView(viewflipper);
			viewflipper.setDisplayedChild(0);
			wm = null;
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
		return gestureDetector.onTouchEvent(event);
	}

	GestureDetector.SimpleOnGestureListener simpleOnGestureListener = new GestureDetector.SimpleOnGestureListener() {

		public boolean onDown(MotionEvent event) {
			return true;
		}

		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,float velocityY) {
			String swipe = "abc ";
			float sensitvity = 50;
			if ((e1.getX() - e2.getX()) > sensitvity) {
				swipe += "Swipe Left\n";

				if (viewflipper.getDisplayedChild() == viewflipper.getChildCount() - 1) {
					if (isFirst != null) {
						if (isFirst) {
							Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
							startActivity(intent);
						}
					}
					finish();
				}else{
					viewflipper.setInAnimation(animFlipInForeward);
					viewflipper.setOutAnimation(animFlipOutForeward);

					viewflipper.showNext();
				}
			} else if ((e2.getX() - e1.getX()) > sensitvity) {
				swipe += "Swipe Right\n";

				if (viewflipper.getDisplayedChild()>0){
					viewflipper.setInAnimation(animFlipInBackward);
					viewflipper.setOutAnimation(animFlipOutBackward);
					viewflipper.showPrevious();
				}
			} else {
				swipe += "\n";
			}

			if ((e1.getY() - e2.getY()) > sensitvity) {
				swipe += "Swipe Up\n";
			} else if ((e2.getY() - e1.getY()) > sensitvity) {
				swipe += "Swipe Down\n";
			} else {
				swipe += "\n";
			}
			return true;
		}

	};	
	
}
