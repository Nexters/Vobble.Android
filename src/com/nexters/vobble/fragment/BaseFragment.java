package com.nexters.vobble.fragment;

import com.nexters.vobble.core.*;

import android.app.Activity;
import android.support.v4.app.*;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;


public class BaseFragment extends Fragment {

	protected ViewGroup rootView;

	public LayoutInflater inflater;
	public BaseFragmentActivity activity;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
    }
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		inflater = LayoutInflater.from(activity);
		this.activity = (BaseFragmentActivity) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		this.inflater = inflater;
		if(rootView == null) {
			rootView = (ViewGroup) getView(container);
		}
		return rootView;
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
	}
	public View getView(ViewGroup container) {
		return container;
	}

	public void showLoading() {
		activity.showLoading();
	}

	public void hideLoading() {
		activity.hideLoading();
	}

	public String getName() {
		StringBuilder nameBuilder = new StringBuilder(getClass().getName());
		Bundle arguments = getArguments();
		if(arguments != null) {
			for(String k : arguments.keySet()) {
				nameBuilder.append(arguments.get(k).toString());
			}
		}
		return nameBuilder.toString();
	}

	public String getTitle() {
		return "";
	}
	
	public boolean onBackPressed() {
		return false;
	}
}
