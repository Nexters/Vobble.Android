package com.nexters.vobble.fragment;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.RequestParams;
import com.nexters.vobble.R;
import com.nexters.vobble.activity.VobbleActivity;
import com.nexters.vobble.core.Vobble;
import com.nexters.vobble.network.HttpUtil;
import com.nexters.vobble.network.URL;
import com.nexters.vobble.network.VobbleResponseHandler;
import com.nexters.vobble.network.Voice;


public class AllVobblesFragment extends BaseFragment{
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
    	view.findViewById(R.id.iv_vobble_1).setOnClickListener(vobbleClickListener);
    	view.findViewById(R.id.iv_vobble_1).setTag(0);
    	view.findViewById(R.id.iv_vobble_2).setOnClickListener(vobbleClickListener);
    	view.findViewById(R.id.iv_vobble_2).setTag(1);
    	view.findViewById(R.id.iv_vobble_3).setOnClickListener(vobbleClickListener);
    	view.findViewById(R.id.iv_vobble_3).setTag(2);
    	view.findViewById(R.id.iv_vobble_4).setOnClickListener(vobbleClickListener);
    	view.findViewById(R.id.iv_vobble_4).setTag(3);
    	view.findViewById(R.id.iv_vobble_5).setOnClickListener(vobbleClickListener);
    	view.findViewById(R.id.iv_vobble_5).setTag(4);
    	view.findViewById(R.id.iv_vobble_6).setOnClickListener(vobbleClickListener);
    	view.findViewById(R.id.iv_vobble_6).setTag(5);
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
    
    private View.OnClickListener vobbleClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// 임시 처리
			int tag = (Integer)v.getTag();
			if(tag < vobbleArray.size()){
				Intent intent = new Intent(activity.getApplicationContext(), VobbleActivity.class);
				Voice voice = vobbleArray.get(tag);
				intent.putExtra("vobble", voice);
				startActivity(intent);
			}
		}
	};
}
