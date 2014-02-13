package com.nexters.vobble.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nexters.vobble.R;

public class MyVobblesFragment extends BaseFragment{
	private View view;

    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
    }
    
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		view = inflater.inflate(R.layout.fragment_my_vobbles, null);
		
        initResources(view);
        return view;
	}

    private void initResources(View view) {

    }
}
