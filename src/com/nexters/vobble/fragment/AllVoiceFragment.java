package com.nexters.vobble.fragment;

import java.util.*;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.location.*;
import android.os.*;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.*;

import com.nexters.vobble.*;
import com.nexters.vobble.record.*;
import com.nexters.vobble.view.*;


public class AllVoiceFragment extends Fragment {
	
	private View view = null;

	private TextView vobbleCountTextView = null;
	private LinearLayout layout1 = null;
	private LinearLayout layout2 = null;
	private LinearLayout layout3 = null;
	private LinearLayout layout4 = null;
	private LinearLayout layout5 = null;
	private LinearLayout layout6 = null;

	private int count = 0;
	private int pageNum = 1;

	private RecordManager recordManager = null;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_all_voice, null);

//		options = new DisplayImageOptions.Builder().cacheInMemory(false)
//				.cacheOnDisc(true).build();

//		mainActivity = (MainActivity) getActivity();
//		recordManager = new RecordManager();

//		vobbleCountTextView = (TextView) view
//				.findViewById(R.id.vobbleCountTextView);

//		layout1 = (LinearLayout) view.findViewById(R.id.vobbleLayout1);
//		layout2 = (LinearLayout) view.findViewById(R.id.vobbleLayout2);
//		layout3 = (LinearLayout) view.findViewById(R.id.vobbleLayout3);
//		layout4 = (LinearLayout) view.findViewById(R.id.vobbleLayout4);
//		layout5 = (LinearLayout) view.findViewById(R.id.vobbleLayout5);
//		layout6 = (LinearLayout) view.findViewById(R.id.vobbleLayout6);

//		for (int i = 0; i < vobbleArr.length; i++) {
//			final int ii = i;
//			vobbleArr[i] = new VobbleWidget(getActivity());
//		}

		
//		layout1.addView(vobbleArr[0]);
//		layout2.addView(vobbleArr[1]);
//		layout3.addView(vobbleArr[2]);
//		layout4.addView(vobbleArr[3]);
//		layout5.addView(vobbleArr[4]);
//		layout6.addView(vobbleArr[5]);



		return view;
	}

	



	
	



}
