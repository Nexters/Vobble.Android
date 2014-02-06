package com.nexters.vobble.fragment;

import android.os.*;
import android.support.v4.app.Fragment;
import android.view.*;

import android.widget.TextView;
import com.nexters.vobble.*;
import com.nexters.vobble.core.ServerAPIRequest;

public class AllVobblesFragment extends Fragment {
	private View view;
    private TextView tvAllVobblesCount;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_all_vobbles, null);

        initResources(view);
        initVobblesCount();
        return view;
	}

    private void initResources(View view) {
        tvAllVobblesCount = (TextView) view.findViewById(R.id.tv_all_vobbles_count);
    }

    private void initVobblesCount() {
        AllVobblesCountAsyncTask allVobblesCountAsyncTask = new AllVobblesCountAsyncTask();
        allVobblesCountAsyncTask.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
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
