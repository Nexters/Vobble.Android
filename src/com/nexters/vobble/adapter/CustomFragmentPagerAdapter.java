package com.nexters.vobble.adapter;

import android.support.v4.app.*;

import com.nexters.vobble.fragment.*;

public class CustomFragmentPagerAdapter extends FragmentPagerAdapter {
	public CustomFragmentPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	public Fragment getItem(int position) {
		if (position == 0) {
			AllVobblesFragment voiceFragment = new AllVobblesFragment();
			return voiceFragment;
		} else if (position == 1) {
			MyVobblesFragment myVobblesFragment = new MyVobblesFragment();
			return myVobblesFragment;
		} 
		return null;
	}

	public int getCount() {
		return 2;
	}
}
