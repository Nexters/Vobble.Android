package com.nexters.vobble.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.nexters.vobble.R;
import com.nexters.vobble.adapter.CustomFragmentPagerAdapter;
import com.nexters.vobble.core.AccountManager;
import com.nexters.vobble.fragment.ShowVobblesFragment;

public class MainActivity extends BaseFragmentActivity implements
		OnClickListener {

    private final int TAB_COUNT = 2;
    private final int INDEX_ALL_VOBBLES = 0;
    private final int INDEX_MY_VOBBLES = 1;

    private Boolean[] isLoaded = new Boolean[]{ false, false };

    private ShowVobblesFragment[] fragments = new ShowVobblesFragment[TAB_COUNT];
	private FrameLayout[] tabs = new FrameLayout[TAB_COUNT];

	private ImageView mIvCreateVobble;
    private ImageView mIvReloadBtn;
    private ViewPager mViewPager;

    private CustomFragmentPagerAdapter mCustomFragmentPagerAdapter;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
        initResources();
		initEvents();
        initFragments();
        initViewPager();

        showTab(INDEX_ALL_VOBBLES);
	}

    private void initResources() {
        tabs[INDEX_ALL_VOBBLES] = (FrameLayout) findViewById(R.id.fl_all_voice_tab_button);
		tabs[INDEX_MY_VOBBLES] = (FrameLayout) findViewById(R.id.fl_my_voice_tab_button);
		mIvCreateVobble = (ImageView) findViewById(R.id.iv_voice_record_btn);
        mIvReloadBtn = (ImageView) findViewById(R.id.iv_reload_btn);
	}

    private void initEvents() {
		tabs[INDEX_ALL_VOBBLES].setOnClickListener(this);
        tabs[INDEX_MY_VOBBLES].setOnClickListener(this);
		mIvCreateVobble.setOnClickListener(this);
        mIvReloadBtn.setOnClickListener(this);
	}

    private void initFragments() {
        fragments[INDEX_ALL_VOBBLES] = new ShowVobblesFragment("");
        fragments[INDEX_MY_VOBBLES] = new ShowVobblesFragment(AccountManager.getInstance().getUserId(this));
    }

	private void initViewPager() {
        FragmentManager fm = getSupportFragmentManager();
        mCustomFragmentPagerAdapter = new CustomFragmentPagerAdapter(fm, TAB_COUNT, fragments);

        mViewPager = (ViewPager) findViewById(R.id.viewPager);
		mViewPager.setAdapter(mCustomFragmentPagerAdapter);
		mViewPager.setCurrentItem(INDEX_ALL_VOBBLES);
		mViewPager.setOnPageChangeListener(onPageChangeListener);
	}

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            if (position == INDEX_ALL_VOBBLES) {
                onClick(tabs[INDEX_ALL_VOBBLES]);
            } else if (position == INDEX_MY_VOBBLES) {
                onClick(tabs[INDEX_MY_VOBBLES]);
            }
        }

        @Override
        public void onPageScrolled(int i, float v, int i2) {

        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

    public void onClick(View view) {
		switch (view.getId()) {
		case R.id.fl_all_voice_tab_button:
			showTab(INDEX_ALL_VOBBLES);
			break;
		case R.id.fl_my_voice_tab_button:
            showTab(INDEX_MY_VOBBLES);
            if (!isLoaded[INDEX_MY_VOBBLES])
                loadTab(INDEX_MY_VOBBLES);
			break;
		case R.id.iv_voice_record_btn:
			Intent intent = new Intent(MainActivity.this, CreateVobbleActivity.class);
			startActivity(intent);
            break;
		case R.id.iv_reload_btn:
            loadTab(mViewPager.getCurrentItem());
			break;
		}
	}

    private void showTab(int index) {
        mViewPager.setCurrentItem(index, true);
        for (int i = 0; i < TAB_COUNT; i++) {
            if (i == index) {
                tabs[i].setBackgroundColor(Color.argb(0, 1, 1, 1));
            } else {
                tabs[i].setBackgroundResource(R.drawable.tab_mask);
            }
        }
    }

    private void loadTab(int index) {
        isLoaded[index] = true;
        fragments[index].load();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.log_out:
                AccountManager.getInstance().signOut(this);
                Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                startActivity(intent);
                finish();
                break;
        }

        return false;
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
}
