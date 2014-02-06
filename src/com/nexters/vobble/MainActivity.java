package com.nexters.vobble;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.support.v4.app.*;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.*;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

import com.nexters.vobble.adapter.*;

public class MainActivity extends FragmentActivity implements OnClickListener{
	private ViewPager viewPager;
	private CustomFragmentPagerAdapter adapter;
	private FrameLayout allVoiceButtonLayout;
	private FrameLayout myVoiceButtonLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

		initResources();
        initEvents();
        initViewPager();
	}

    private void initResources() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
		allVoiceButtonLayout = (FrameLayout) findViewById(R.id.fl_all_voice_tab_button);
		myVoiceButtonLayout = (FrameLayout) findViewById(R.id.fl_my_voice_tab_button);

		allVoiceButtonLayout.setBackgroundColor(Color.argb(0, 1, 1, 1));
	}

    private void initEvents() {
        allVoiceButtonLayout.setOnClickListener(this);
        myVoiceButtonLayout.setOnClickListener(this);
    }

    private void initViewPager() {
        FragmentManager fm = getSupportFragmentManager();
        adapter = new CustomFragmentPagerAdapter(fm);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                setSelectedTab(position);
            }

            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.fl_all_voice_tab_button:
            showAllVoiceFragment();
			break;
		case R.id.fl_my_voice_tab_button:
            showMyVoiceFragment();
			break;
		}
	}

    private void showAllVoiceFragment() {
        viewPager.setCurrentItem(0, true);
        allVoiceButtonLayout.setBackgroundColor(Color.argb(0, 1, 1, 1));
        myVoiceButtonLayout.setBackgroundResource(R.drawable.tab_mask);
    }

    private void showMyVoiceFragment() {
        viewPager.setCurrentItem(1, true);
        myVoiceButtonLayout.setBackgroundColor(Color.argb(0, 1, 1, 1));
        allVoiceButtonLayout.setBackgroundResource(R.drawable.tab_mask);
    }

    public void setSelectedTab(int position) {
        if (position == 0)
			onClick(allVoiceButtonLayout);
		else if (position == 1)
			onClick(myVoiceButtonLayout);
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

