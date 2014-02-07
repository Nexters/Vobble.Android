package com.nexters.vobble.fragment;


import org.json.JSONObject;

import android.os.*;
import android.support.v4.app.*;
import android.view.*;

import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.nexters.vobble.*;
import com.nexters.vobble.core.*;
import com.nexters.vobble.network.*;

public class MyVobblesFragment extends BaseFragment{
	private View view;
    private TextView tvMyVobblesCount;

    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
    }
    
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		view = inflater.inflate(R.layout.fragment_my_vobbles, null);
		
        initResources(view);
        initVobblesCount();
        return view;
	}

    private void initResources(View view) {
        tvMyVobblesCount = (TextView) view.findViewById(R.id.tv_my_vobbles_count);
    }

    private void initVobblesCount() {
    	String url = String.format(URL.USER_VOBBLES_COUNT,Vobble.getUserId(activity));
    	
		RequestParams params = new RequestParams();
		params.put(Vobble.LATITUDE, "127");
		params.put(Vobble.LONGITUDE, "37");
		params.put(Vobble.LIMIT, "6");
    	
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
        /*
    	MyVobblesCountAsyncTask myVobblesCountAsyncTask = new MyVobblesCountAsyncTask();
        myVobblesCountAsyncTask.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        */
    }

    private void setVobblesCount(Integer count) {
        tvMyVobblesCount.setText(count + "");
    }

    private class MyVobblesCountAsyncTask extends AsyncTask<Integer, Integer, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            // TODO: User 모델 만들면 userId 받아서 넘겨줘야 함
            //int count = ServerAPIRequest.getMyVobblesCount();
            return 0;
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
