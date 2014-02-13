package com.nexters.vobble.activity;

import android.app.*;
import android.content.*;
import android.os.*;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.*;
import android.view.View.OnTouchListener;
import android.view.animation.*;
import android.widget.*;

import com.nexters.vobble.*;
import com.nexters.vobble.core.Vobble;

public class TutorialActivity extends Activity {
	
	private static final int INTRO_PAGE_CNT = 6;
	
	private ViewPager pager;
	private Animation newStartFadeIn;
	private View[] introViews = new View[INTRO_PAGE_CNT];
	private TutorialPagerAdapter adapter;
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
		pager = (ViewPager) findViewById(R.id.pager);
		adapter = new TutorialPagerAdapter();
		pager.setAdapter(adapter);
		
		LayoutInflater layoutInflater = LayoutInflater.from(this);
		for(int i = 0; i < INTRO_PAGE_CNT; i++) {
			int resId = getResources().getIdentifier("tutorial_page"+i, "layout", getPackageName());
			introViews[i] = layoutInflater.inflate(resId, null);
		}
		pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			int currentPage = 0;
			@Override
			public void onPageSelected(int arg0) {
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				currentPage = arg0;
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				if (arg0 == ViewPager.SCROLL_STATE_IDLE) {
					
				}else if(arg0 == ViewPager.SCROLL_STATE_DRAGGING){
					if(currentPage == INTRO_PAGE_CNT-1){
						Intent intent = new Intent(getApplicationContext(), StartActivity.class);
	                    startActivity(intent);
						finish();
					}
				}
			}
		});
	}
	
	private void initAnimations() {
		newStartFadeIn = AnimationUtils.loadAnimation(this, R.anim.tutorial_fade_in);
		introViews[0].startAnimation(newStartFadeIn);
	}

	private void initEvents() {
		
	}

	private class TutorialPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return INTRO_PAGE_CNT;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View v = null;
			
			v = introViews[position];
			
			((ViewPager) container).addView(v);
			return v;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object view) {
			((ViewPager) container).removeView((View) view);
		}

		@Override
		public boolean isViewFromObject(View container, Object obj) {
			return container == obj;
		}
		
	}
}
