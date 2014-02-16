package com.nexters.vobble.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.location.Location;
import com.nexters.vobble.activity.ListenVobbleActivity;
import com.nexters.vobble.core.App;
import com.nexters.vobble.network.APIResponseHandler;
import com.nexters.vobble.util.ImageManagingHelper;
import com.nexters.vobble.util.LocationHelper;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.loopj.android.http.RequestParams;
import com.nexters.vobble.R;
import com.nexters.vobble.network.HttpUtil;
import com.nexters.vobble.network.URL;
import com.nexters.vobble.entity.Vobble;

@SuppressLint("ValidFragment")
public class ShowVobblesFragment extends BaseFragment {
    private static final int VOBBLE_COUNT = 12;
    private static final String VOBBLE_IMG_ID_PREFIX = "iv_vobble_";
    private static final String VOBBLE_BACK_IMG_ID_PREFIX = "iv_vobble_back_";

    private ImageView[] vobbleImageViews = new ImageView[VOBBLE_COUNT];
    private ImageView[] vobbleBackupImageViews = new ImageView[VOBBLE_COUNT];
    private ArrayList<Vobble> vobbleList = new ArrayList<Vobble>();

    private String userId;
    private Location mLocation;

    public ShowVobblesFragment(String userId) {
        this.userId = userId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_show_vobbles, null);

        initResources(view);
        initEvents();
        if (userId.equals(""))
            load();
        return view;
	}

    private void initResources(View view) {
        int resId, backResId;
        for (int i = 1; i <= VOBBLE_COUNT; i++) {
            resId = getResources().getIdentifier(VOBBLE_IMG_ID_PREFIX + i, "id", this.getActivity().getPackageName());
            vobbleImageViews[i - 1] = (ImageView) view.findViewById(resId);
            vobbleImageViews[i - 1].setVisibility(View.INVISIBLE);

            backResId = getResources().getIdentifier(VOBBLE_BACK_IMG_ID_PREFIX + i, "id", this.getActivity().getPackageName());
            vobbleBackupImageViews[i - 1] = (ImageView) view.findViewById(backResId);
            vobbleBackupImageViews[i - 1].setVisibility(View.INVISIBLE);
    	}
    }

    private void initEvents() {
        for (int i = 0; i < VOBBLE_COUNT; i++) {
            vobbleImageViews[i].setOnClickListener(vobbleClickListener);
            vobbleImageViews[i].setTag(i);
        }
    }

    public void load() {
        initLocation();
        initVobbleImageViews();
        loadVobbles();
    }

    private void initLocation() {
        LocationHelper locationHelper = new LocationHelper(getActivity());
        if (locationHelper.isGPSEnabled()) {
            mLocation = locationHelper.getCurrentLocation();
        } else {
            alert(R.string.error_cannot_use_gps);
            mLocation = new Location("");
            mLocation.setLatitude(37);
            mLocation.setLongitude(127);
        }
    }

    private void initVobbleImageViews() {
        for (int i = 0; i < VOBBLE_COUNT; i++) {
            vobbleImageViews[i].setImageResource(R.drawable.vobble_cache_image_icon);
            vobbleImageViews[i].setVisibility(View.INVISIBLE);
            vobbleBackupImageViews[i].setVisibility(View.INVISIBLE);
        }
    }

    private void loadVobbles() {
    	String url;

        if (userId.equals("")) {
            url = URL.VOBBLES;
        } else {
            url = String.format(URL.USER_VOBBLES, userId);
        }

        RequestParams params = new RequestParams();
        params.put(App.LIMIT, VOBBLE_COUNT + "");
        params.put(Vobble.LATITUDE, mLocation.getLatitude() + "");
        params.put(Vobble.LONGITUDE, mLocation.getLongitude() + "");

		HttpUtil.get(url, null, params, new APIResponseHandler(activity) {

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
				vobbleList.clear();
				JSONArray dataArr = response.optJSONArray("vobbles");
				for (int i = 0; i < dataArr.length(); i++)
                    vobbleList.add(Vobble.build(dataArr.optJSONObject(i)));
				loadVobbleImages();
			}
			@Override
			public void onFailure(Throwable error, String response){
				super.onFailure(error, response);
				loadVobbleImages();
			}
		});
    }

    private void loadVobbleImages() {
    	Animation scaleUp = AnimationUtils.loadAnimation(activity, R.anim.vobble_scale_up);
        for (int i = 0; i < VOBBLE_COUNT; i++) {
            if (i < vobbleList.size()) {
                vobbleBackupImageViews[i].setVisibility(View.VISIBLE);
                vobbleImageViews[i].setVisibility(View.VISIBLE);
                ImageManagingHelper.loadAndAttachCroppedImage(vobbleImageViews[i], vobbleList.get(i).getImageUrl(), scaleUp);
        	} else {
                vobbleBackupImageViews[i].setVisibility(View.INVISIBLE);
                vobbleImageViews[i].setVisibility(View.INVISIBLE);
        	}
    	}
    }

    private View.OnClickListener vobbleClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			int tag = (Integer) v.getTag();
			if (tag < vobbleList.size()) {
				Intent intent = new Intent(activity.getApplicationContext(), ListenVobbleActivity.class);
				Vobble vobble = vobbleList.get(tag);
				intent.putExtra("vobble", vobble);
				startActivity(intent);
			}
		}
	};
}
