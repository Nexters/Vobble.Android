package com.nexters.vobble.fragment;


import android.os.*;
import android.support.v4.app.*;
import android.view.*;

import android.widget.TextView;
import com.nexters.vobble.*;

public class MyVoiceFragment extends Fragment{
	private View view;
    private TextView tvMyVobblesCount;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_my_vobbles, null);
		
        initResources(view);
        initVobblesCount();
        return view;
	}

    private void initResources(View view) {
        tvMyVobblesCount = (TextView) view.findViewById(R.id.tv_my_vobbles_count);
    }

    private void initVobblesCount() {
        MyVobblesCountAsyncTask myVobblesCountAsyncTask = new MyVobblesCountAsyncTask();
        myVobblesCountAsyncTask.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
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
