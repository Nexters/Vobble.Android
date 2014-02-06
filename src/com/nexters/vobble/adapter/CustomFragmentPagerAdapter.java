package com.nexters.vobble.adapter;

import android.support.v4.app.*;

import com.nexters.vobble.fragment.*;

public class CustomFragmentPagerAdapter extends FragmentPagerAdapter {
	public CustomFragmentPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	public Fragment getItem(int position) {
		if (position == 0) {
			AllVoiceFragment voiceFragment = new AllVoiceFragment();
			return voiceFragment;
		} else if (position == 1) {
			MyVoiceFragment myVoiceFragment = new MyVoiceFragment();
			return myVoiceFragment;
		} 
		return null;
	}

	public int getCount() {
		return 2;
	}
}
