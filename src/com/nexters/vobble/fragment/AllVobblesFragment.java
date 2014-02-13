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
import android.widget.*;

import com.loopj.android.http.*;
import com.nexters.vobble.*;
import com.nexters.vobble.activity.MainActivity;
import com.nexters.vobble.activity.RecordActivity;
import com.nexters.vobble.core.*;
import com.nexters.vobble.network.*;

public class AllVobblesFragment extends BaseFragment implements OnClickListener{
	private View view;
    private TextView tvAllVobblesCount;
    private ArrayList<Voice> voiceList = new ArrayList<Voice>();
    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		voiceList = new ArrayList<Voice>();
    }
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		view = inflater.inflate(R.layout.fragment_all_vobbles, null);

        initResources(view);
        initVobblesCount();
        initVobbles();
        return view;
	}

    private void initResources(View view) {
        tvAllVobblesCount = (TextView) view.findViewById(R.id.tv_all_vobbles_count);
        view.findViewById(R.id.vobble1).setOnClickListener(this);
        view.findViewById(R.id.vobble2).setOnClickListener(this);
        view.findViewById(R.id.vobble3).setOnClickListener(this);
        view.findViewById(R.id.vobble4).setOnClickListener(this);
        view.findViewById(R.id.vobble5).setOnClickListener(this);
        view.findViewById(R.id.vobble6).setOnClickListener(this);
    }

    private void initVobblesCount() {
    	String url = URL.VOBBLES_COUNT;
		HttpUtil.get(url, null, null, new VobbleResponseHandler(activity) {
			
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
				String count = response.optString("count");
				setVobblesCount(Integer.parseInt(count));
			}
		});
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
				JSONArray arr = response.optJSONArray("vobbles");
				for(int i = 0; i < arr.length(); i++) {
					Voice voice = Voice.parse(arr.optJSONObject(i));
					voiceList.add(voice);
					
				}
			}
		});
    }
    
    private void setVobblesCount(Integer count) {
        tvAllVobblesCount.setText(count + "");
    }
    
    public void onClick(View view) {
		switch (view.getId()) {
		case R.id.fl_my_voice_tab_button:
		
			break;
		case R.id.iv_voice_record_btn:
			
			break;
		case R.id.vobble1:
			playVoice(0);
			break;
		case R.id.vobble2:
			playVoice(1);
			break;
		case R.id.vobble3:
			playVoice(2);
			break;
		case R.id.vobble4:
			playVoice(3);
			break;
		case R.id.vobble5:
			playVoice(4);
			break;
		case R.id.vobble6:
			playVoice(5);
			break;
		}
	}
    
    private void playVoice(int idx){
    	Voice voice = voiceList.get(idx);
		Vobble.log("voice : " + voice.getStreamingVoiceUrl());
		
		MediaPlayer player = new MediaPlayer();
		player.setAudioStreamType(AudioManager.STREAM_MUSIC);
		try {
			player.setDataSource(voice.getStreamingVoiceUrl());
			player.prepareAsync();
		} catch (IllegalArgumentException e) {
		    e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		player.start();
    }
}
