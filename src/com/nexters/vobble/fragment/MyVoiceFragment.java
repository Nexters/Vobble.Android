package com.nexters.vobble.fragment;


import android.os.*;
import android.support.v4.app.*;
import android.view.*;

import com.nexters.vobble.*;

public class MyVoiceFragment extends Fragment{
	private View view;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_my_voice, null);
		return view;
	}
}
