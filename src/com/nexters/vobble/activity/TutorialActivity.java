package com.nexters.vobble.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.analytics.tracking.android.Fields;
import com.nexters.vobble.R;
import com.nexters.vobble.adapter.CustomPagerAdapter;
import com.nexters.vobble.core.App;

public class TutorialActivity extends BaseActivity {
	
	private static final int INTRO_PAGE_CNT = 4;
	
	private ViewPager mViewPager;
    private View[] mIntroPageViews = new View[INTRO_PAGE_CNT];
    private CustomPagerAdapter mCustomPagerAdapter;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_tutorial);

        initResources();
		initViewPager();
	}

    private void initResources() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        for(int i = 0; i < INTRO_PAGE_CNT; i++) {
            int resId = getResources().getIdentifier("tutorial_page" + i, "layout", getPackageName());
            mIntroPageViews[i] = layoutInflater.inflate(resId, null);
        }
    }

    private void initViewPager() {
        mCustomPagerAdapter = new CustomPagerAdapter(INTRO_PAGE_CNT, mIntroPageViews);
        mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mCustomPagerAdapter);
		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int currentPage = 0;

            @Override
            public void onPageSelected(int index) {

            }

            @Override
            public void onPageScrolled(int index, float arg1, int arg2) {
                currentPage = index;
            }

            @Override
            public void onPageScrollStateChanged(int index) {
                if (index == ViewPager.SCROLL_STATE_DRAGGING) {
                    if (currentPage == INTRO_PAGE_CNT - 1) {
                        finish();
                    }
                }
            }
        });
	}
}
