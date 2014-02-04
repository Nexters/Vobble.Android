package com.nexters.vobble;

import android.app.*;
import android.app.FragmentManager;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.support.v4.app.*;
import android.support.v4.view.*;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

public class MainActivity extends FragmentActivity implements OnClickListener{
	private ViewPager viewPager = null;
	//private MyFragmentPagerAdapter adapter = null;

	private FrameLayout allVoiceButtonLayout = null;
	private FrameLayout myVoiceButtonLayout = null;
	
	private ImageView nextVobbleImageView = null;
	private ImageView prevVobbleImageView = null;
	
	private ImageView recordImageView = null;
	private Intent intent = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		recordImageView = (ImageView) findViewById(R.id.voice_record_btn);
		
		recordImageView.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				intent = new Intent(MainActivity.this, RecordActivity.class);
				startActivity(intent);
			}
		});
		
		//VobblePrefsManager manager = new VobblePrefsManager(this);
		//int userId = manager.getUserId();
		//if (userId == -1) {
		//	Intent intent = new Intent(MainActivity.this, StartActivity.class);
		//	startActivity(intent);
		//	finish();
		//}

		loadResource();

		viewPager.setCurrentItem(0);
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			public void onPageSelected(int position) {
				setSelectedTab(position);
			}

			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			public void onPageScrollStateChanged(int arg0) {

			}
		});
		
		
	}
	
	
	
	public void loadResource() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		//viewPager = (ViewPager) findViewById(R.id.viewPager);
		//nextVobbleImageView = (ImageView)findViewById(R.id.nextVobbleImageView);
		//prevVobbleImageView = (ImageView)findViewById(R.id.prevVobbleImageView);
		
		allVoiceButtonLayout = (FrameLayout) findViewById(R.id.allVoiceTabButtonLayout);
		myVoiceButtonLayout = (FrameLayout) findViewById(R.id.myVoiceTabButtonLayout);
		//recordImageView = (ImageView) findViewById(R.id.recordImageView);
		//FragmentManager fm = getSupportFragmentManager();

		//adapter = new MyFragmentPagerAdapter(fm);
		//viewPager.setAdapter(adapter);

		allVoiceButtonLayout.setOnClickListener(this);
		myVoiceButtonLayout.setOnClickListener(this);
		recordImageView.setOnClickListener(this);

		allVoiceButtonLayout.setBackgroundColor(Color.argb(0, 1, 1, 1));
	}
	
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.allVoiceTabButtonLayout:
			viewPager.setCurrentItem(0, true);
			allVoiceButtonLayout.setBackgroundColor(Color.argb(0, 1, 1, 1));
			myVoiceButtonLayout.setBackgroundResource(R.drawable.tab_mask);
			break;
		case R.id.myVoiceTabButtonLayout:
			viewPager.setCurrentItem(1, true);
			myVoiceButtonLayout.setBackgroundColor(Color.argb(0, 1, 1, 1));
			allVoiceButtonLayout.setBackgroundResource(R.drawable.tab_mask);
			break;
		//case R.id.recordImageView:
		//	Intent intent = new Intent(MainActivity.this, RecordActivity.class);
		//	startActivity(intent);
		//	break;

		}
	}
	
	public void setSelectedTab(int position) {
		if (position == 0)
			onClick(allVoiceButtonLayout);
		else if (position == 1)
			onClick(myVoiceButtonLayout);
	}

	public ImageView getPrevVobbleImageView() {
		return prevVobbleImageView;
	}
	public ImageView getNextVobbleImageView() {
		return nextVobbleImageView;
	}
	

	public void onBackPressed() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("종료");
		builder.setMessage("Vobble을 종료하시겠습니까?");
		builder.setNegativeButton("아니요", null);
		builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				finish();
			}
		});
		builder.show();
	}
	
	
}

