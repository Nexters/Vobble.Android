package com.nexters.vobble.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.nexters.vobble.fragment.AllVobblesFragment;
import com.nexters.vobble.fragment.MyVobblesFragment;

public class CustomFragmentPagerAdapter extends FragmentPagerAdapter {
	private int fragmentCount;
    private Fragment[] fragments;

    public CustomFragmentPagerAdapter(FragmentManager fm, int fragmentCount, Fragment[] fragments) {
		super(fm);
        this.fragmentCount = fragmentCount;
        this.fragments = fragments;
	}

	public Fragment getItem(int position) {
		return fragments[position];
	}

	public int getCount() {
		return fragmentCount;
	}
}
