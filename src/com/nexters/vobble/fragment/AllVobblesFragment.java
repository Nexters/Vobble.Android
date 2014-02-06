package com.nexters.vobble.fragment;

import org.json.JSONObject;

import android.os.*;
import android.support.v4.app.Fragment;
import android.view.*;

import android.widget.TextView;
import com.nexters.vobble.*;
import com.nexters.vobble.core.ServerAPIRequest;
import com.nexters.vobble.core.Vobble;
import com.nexters.vobble.network.HttpUtil;
import com.nexters.vobble.network.URL;
import com.nexters.vobble.network.VobbleResponseHandler;

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
        Vobble.log("USER_VOBBLES_COUNT");
        return view;
	}

    private void initResources(View view) {
        tvAllVobblesCount = (TextView) view.findViewById(R.id.tv_all_vobbles_count);
    }

    private void initVobblesCount() {
    	String url = URL.VOBBLES_COUNT;
		/*
		RequestParams params = new RequestParams();
		params.put(Vobble.LATITUDE, "127");
		params.put(Vobble.LONGITUDE, "37");
		params.put(Vobble.LIMIT, "6");
		*/
    	
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

    private void setVobblesCount(Integer count) {
        tvAllVobblesCount.setText(count + "");
    }

    private class AllVobblesCountAsyncTask extends AsyncTask<Integer, Integer, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            int count = ServerAPIRequest.getAllVobblesCount();
            return count;
        }

        @Override
        protected void onPostExecute(Integer count) {
            if (count >= 0) {
                setVobblesCount(count);
            }
            super.onPostExecute(count);
        }
    }
}
