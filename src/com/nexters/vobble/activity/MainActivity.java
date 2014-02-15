package com.nexters.vobble.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.nexters.vobble.R;
import com.nexters.vobble.adapter.CustomFragmentPagerAdapter;
import com.nexters.vobble.core.Vobble;
import com.nexters.vobble.fragment.ShowVobblesFragment;

public class MainActivity extends BaseFragmentActivity implements
		OnClickListener {

    private final int FRAGMENT_COUNT = 2;
    private Fragment[] fragments = new Fragment[FRAGMENT_COUNT];

	private FrameLayout mFlAllVobbleTab;
	private FrameLayout myVoiceButtonLayout;
	private ImageView mIvCreateVobble;
    private ViewPager mViewPager;

    private CustomFragmentPagerAdapter mCustomFragmentPagerAdapter;

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
        fragments[0] = new ShowVobblesFragment("");
        fragments[1] = new ShowVobblesFragment(Vobble.getUserId(this));
        mFlAllVobbleTab = (FrameLayout) findViewById(R.id.fl_all_voice_tab_button);
		myVoiceButtonLayout = (FrameLayout) findViewById(R.id.fl_my_voice_tab_button);
		mIvCreateVobble = (ImageView) findViewById(R.id.iv_voice_record_btn);
		mFlAllVobbleTab.setBackgroundColor(Color.argb(0, 1, 1, 1));
	}

	private void initEvents() {
		mFlAllVobbleTab.setOnClickListener(this);
		myVoiceButtonLayout.setOnClickListener(this);
		mIvCreateVobble.setOnClickListener(this);
	}

	private void initViewPager() {
        FragmentManager fm = getSupportFragmentManager();
        mCustomFragmentPagerAdapter = new CustomFragmentPagerAdapter(fm, FRAGMENT_COUNT, fragments);

        mViewPager = (ViewPager) findViewById(R.id.viewPager);
		mViewPager.setAdapter(mCustomFragmentPagerAdapter);
		mViewPager.setCurrentItem(0);
		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    onClick(mFlAllVobbleTab);
                } else if (position == 1) {
                    onClick(myVoiceButtonLayout);
                }
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
			showAllVobblesFragment();
			break;
		case R.id.fl_my_voice_tab_button:
			showMyVobblesFragment();
			break;
		case R.id.iv_voice_record_btn:
			Intent intent = new Intent(MainActivity.this, CreateVobbleActivity.class);
			startActivity(intent);
            break;
		}
	}

	private void showAllVobblesFragment() {
		mViewPager.setCurrentItem(0, true);
		mFlAllVobbleTab.setBackgroundColor(Color.argb(0, 1, 1, 1));
		myVoiceButtonLayout.setBackgroundResource(R.drawable.tab_mask);
	}

	private void showMyVobblesFragment() {
		mViewPager.setCurrentItem(1, true);
		myVoiceButtonLayout.setBackgroundColor(Color.argb(0, 1, 1, 1));
		mFlAllVobbleTab.setBackgroundResource(R.drawable.tab_mask);
	}

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.end);
        builder.setMessage(R.string.confirm_finish_app);
        builder.setNegativeButton(R.string.no, null);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                finish();
            }
        });
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.log_out:
            Vobble.setToken(this, "");
        	Intent intent = new Intent(getApplicationContext(), StartActivity.class);
            startActivity(intent);
			finish();
            break;
        }
 
        return false;
    }
}
