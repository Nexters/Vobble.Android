package com.nexters.vobble.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import com.nexters.vobble.R;
import com.nexters.vobble.activity.BaseFragmentActivity;


public class BaseFragment extends Fragment {

	protected ViewGroup rootView;

	public LayoutInflater inflater;
	public BaseFragmentActivity activity;
	private AlertDialog alertDialog;
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

	public View getView(ViewGroup container) {
		return container;
	}

	public void showLoading() {
		activity.showLoading();
	}

	public void hideLoading() {
		activity.hideLoading();
	}

    public void alert(int resId) {
        alert(getString(resId));
    }

    public void alert(String message) {
    	if( alertDialog != null && alertDialog.isShowing() ) return;
    	
    	AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setMessage(message);
        builder.setPositiveButton(R.string.ok, null);
        
        alertDialog = builder.create();
        alertDialog.show();
        
        TextView messageText = (TextView) alertDialog.findViewById(android.R.id.message);
        messageText.setGravity(Gravity.CENTER);
    }
}
