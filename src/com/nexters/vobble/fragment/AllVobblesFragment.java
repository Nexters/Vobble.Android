package com.nexters.vobble.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.*;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.*;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.loopj.android.http.*;
import com.nexters.vobble.*;
import com.nexters.vobble.activity.MainActivity;
import com.nexters.vobble.activity.RecordActivity;
import com.nexters.vobble.core.*;
import com.nexters.vobble.network.*;


public class AllVobblesFragment extends BaseFragment implements OnClickListener{
	private View view;
	private ArrayList<Voice> vobbleArray;
    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
    }
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		view = inflater.inflate(R.layout.fragment_all_vobbles, null);

        initResources(view);
        initVobbles();
        return view;
	}

    private void initResources(View view) {
    	vobbleArray = new ArrayList<Voice>();
    	view.findViewById(R.id.iv_vobble_1).setOnClickListener(this);
    }

    private void initVobbles() {
    	String url = URL.VOBBLES;
    	
    	RequestParams params = new RequestParams();
    	params.put(Vobble.LATITUDE, "127");
		params.put(Vobble.LONGITUDE, "37");
		params.put(Vobble.LIMIT, "6");
        
		HttpUtil.get(url, null, params, new VobbleResponseHandler(activity) {

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
				JSONArray dataArr = response.optJSONArray("vobbles");
				for(int i = 0; i < dataArr.length(); i++) {
					Voice station = Voice.parse(dataArr.optJSONObject(i));
					vobbleArray.add(station);
				}
			}
		});
    }
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_vobble_1:
			MediaPlayer mp = new MediaPlayer();
		    try {
		    	Voice voice = vobbleArray.get(0);
		    	mp.setDataSource(voice.getStreamingVoiceUrl());
		        mp.prepare();
		        mp.start();
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
			break;
		}
	}
}
