package com.nexters.vobble.fragment;

import org.json.*;

import android.os.*;
import android.view.*;
import android.widget.*;

import com.loopj.android.http.*;
import com.nexters.vobble.*;
import com.nexters.vobble.core.*;
import com.nexters.vobble.network.*;

public class AllVobblesFragment extends BaseFragment {
	private View view;
    private TextView tvAllVobblesCount;
    
    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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

			}
		});
    }
    
    private void setVobblesCount(Integer count) {
        tvAllVobblesCount.setText(count + "");
    }
}
