package com.nexters.vobble.activity;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
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

import android.widget.TextView;
import com.google.analytics.tracking.android.Fields;
import com.nexters.vobble.R;
import com.nexters.vobble.adapter.CustomFragmentPagerAdapter;
import com.nexters.vobble.core.AccountManager;
import com.nexters.vobble.core.App;
import com.nexters.vobble.fragment.BaseMainFragment;
import com.nexters.vobble.fragment.FriendsFragment;
import com.nexters.vobble.fragment.ShowVobblesFragment;
import com.nexters.vobble.fragment.ShowVobblesFragment.VOBBLE_FRAMGMENT_TYPE;
import com.nexters.vobble.listener.ImageViewTouchListener;
import com.nexters.vobble.network.APIResponseHandler;
import com.nexters.vobble.network.HttpUtil;
import com.nexters.vobble.network.URL;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends BaseFragmentActivity implements
		OnClickListener, BaseMainFragment.OnFragmentListener {

    private final int TAB_COUNT = 3;
    private final int INDEX_ALL_VOBBLES = 0;
    private final int INDEX_MY_VOBBLES = 1;
    private final int INDEX_FRIENDS_VOBBLES = 2;

    private Boolean[] hasNeedToLoad = new Boolean[] { false, true, false };
    private BaseMainFragment[] fragments = new BaseMainFragment[TAB_COUNT];
	private FrameLayout[] tabs = new FrameLayout[TAB_COUNT];
    private String[] vobblesCnt = new String[TAB_COUNT];

	private TextView mTvVobbleCnt;
    private TextView mTvVobbleCntType;
    private ImageView mIvReloadBtn;
    private ImageView mIvEventBtn;
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

        loadVobblesCount(INDEX_ALL_VOBBLES);

        showTab(INDEX_ALL_VOBBLES);
        App.getGaTracker().set(Fields.SCREEN_NAME, this.getClass().getSimpleName());
	}

    private void initResources() {
        tabs[INDEX_ALL_VOBBLES] = (FrameLayout) findViewById(R.id.fl_all_voice_tab_button);
		tabs[INDEX_MY_VOBBLES] = (FrameLayout) findViewById(R.id.fl_my_voice_tab_button);
        tabs[INDEX_FRIENDS_VOBBLES] = (FrameLayout) findViewById(R.id.fl_friends_voice_tab_button);
		mTvVobbleCnt = (TextView) findViewById(R.id.tv_vobble_cnt);
        mTvVobbleCntType = (TextView) findViewById(R.id.tv_vobble_cnt_type);
        mIvReloadBtn = (ImageView) findViewById(R.id.iv_reload_btn);
        mIvEventBtn = (ImageView) findViewById(R.id.iv_event_btn);
	}

    private void initEvents() {
        ImageViewTouchListener ivTouchListener = new ImageViewTouchListener();
        tabs[INDEX_ALL_VOBBLES].setOnClickListener(this);
        tabs[INDEX_MY_VOBBLES].setOnClickListener(this);
        tabs[INDEX_FRIENDS_VOBBLES].setOnClickListener(this);
        mIvReloadBtn.setOnTouchListener(ivTouchListener);
        mIvReloadBtn.setOnClickListener(this);
        mIvEventBtn.setOnTouchListener(ivTouchListener);
        mIvEventBtn.setOnClickListener(this);
	}

    private void initFragments() {
        fragments[INDEX_ALL_VOBBLES] = new ShowVobblesFragment("", VOBBLE_FRAMGMENT_TYPE.ALL);
        fragments[INDEX_MY_VOBBLES] = new ShowVobblesFragment(AccountManager.getInstance().getUserId(this), VOBBLE_FRAMGMENT_TYPE.MY);
        fragments[INDEX_FRIENDS_VOBBLES] = new FriendsFragment();
    }

	private void initViewPager() {
        FragmentManager fm = getSupportFragmentManager();
        mCustomFragmentPagerAdapter = new CustomFragmentPagerAdapter(fm, TAB_COUNT, fragments);

        mViewPager = (ViewPager) findViewById(R.id.viewPager);
		mViewPager.setAdapter(mCustomFragmentPagerAdapter);
        mViewPager.setOffscreenPageLimit(TAB_COUNT);
        mViewPager.setCurrentItem(INDEX_ALL_VOBBLES);
		mViewPager.setOnPageChangeListener(onPageChangeListener);
	}

    private void loadVobblesCount(int index) {
        if (vobblesCnt[index] == null) {
            executeGetVobblesCount(index);
            return;
        }
        mTvVobbleCnt.setText(vobblesCnt[index]);
        if (index == INDEX_ALL_VOBBLES)
            mTvVobbleCntType.setText("All vobbles");
        else if (index == INDEX_MY_VOBBLES)
            mTvVobbleCntType.setText("My vobbles");
    }

    private void executeGetVobblesCount(final int index) {
        String url = "";
        if (index == INDEX_ALL_VOBBLES) {
            url = URL.VOBBLES_COUNT;
        } else if (index == INDEX_MY_VOBBLES) {
            url = String.format(URL.USER_VOBBLES_COUNT, AccountManager.getInstance().getUserId(this));
        }

        HttpUtil.get(url, null, null, new APIResponseHandler(this) {

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
                try {
                    vobblesCnt[index] = response.getString("count");
                    loadVobblesCount(index);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable error, String response) {
                super.onFailure(error, response);
            }
        });
    }

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            if (position == INDEX_ALL_VOBBLES) {
                onClick(tabs[INDEX_ALL_VOBBLES]);
            } else if (position == INDEX_MY_VOBBLES) {
                onClick(tabs[INDEX_MY_VOBBLES]);
            } else if (position == INDEX_FRIENDS_VOBBLES) {
                onClick(tabs[INDEX_FRIENDS_VOBBLES]);
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
                loadVobblesCount(INDEX_ALL_VOBBLES);
                if (hasNeedToLoad[INDEX_ALL_VOBBLES])
                    loadTab(INDEX_ALL_VOBBLES);
                break;
            case R.id.fl_my_voice_tab_button:
                showTab(INDEX_MY_VOBBLES);
                loadVobblesCount(INDEX_MY_VOBBLES);
                if (hasNeedToLoad[INDEX_MY_VOBBLES])
                    loadTab(INDEX_MY_VOBBLES);
                break;
            case R.id.fl_friends_voice_tab_button:
                showTab(INDEX_FRIENDS_VOBBLES);
                break;
            case R.id.iv_reload_btn:
                loadTab(mViewPager.getCurrentItem());
                break;
            case R.id.iv_event_btn:
                Intent intent = new Intent(this, EventActivity.class);
                startActivity(intent);
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
        vobblesCnt[index] = null;
        loadVobblesCount(index);

        hasNeedToLoad[index] = false;
        fragments[index].load();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_feedback:
                sendToEmail();
                break;
            case R.id.action_tutorial:
                intent = new Intent(getApplicationContext(), TutorialActivity.class);
                startActivity(intent);
                break;
            case R.id.action_sign_out:
                AccountManager.getInstance().signOut(this);
                intent = new Intent(getApplicationContext(), StartActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return false;
    }

    private void sendToEmail() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        String[] tos = { "nexters.vobble@gmail.com" };
        intent.putExtra(Intent.EXTRA_EMAIL, tos);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            showShortToast("설치된 메일 클라이언트가 없습니다.");
        }
    }

    @Override
    public void onRemovedVobble() {
        hasNeedToLoad[INDEX_MY_VOBBLES] = true;
        hasNeedToLoad[INDEX_ALL_VOBBLES] = true;
        loadTab(INDEX_MY_VOBBLES);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.finish);
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
    public boolean onPrepareOptionsMenu (Menu menu){
    	menu.clear();
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onPrepareOptionsMenu(menu);
    }
}
