package com.nexters.vobble.fragment;

import java.util.ArrayList;

import android.location.Location;
import com.nexters.vobble.activity.ListenVobbleActivity;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.loopj.android.http.RequestParams;
import com.nexters.vobble.R;
import com.nexters.vobble.core.Vobble;
import com.nexters.vobble.network.HttpUtil;
import com.nexters.vobble.network.URL;
import com.nexters.vobble.network.VobbleResponseHandler;
import com.nexters.vobble.network.Voice;
import com.nexters.vobble.util.CommonUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;


public class ShowVobblesFragment extends BaseFragment{

    private final int VOBBLE_COUNT = 14;
    private final String VOBBLE_IMAGEVIEW_ID_PREFIX = "iv_vobble_";
    private Integer[] vobbleBnts = new Integer[VOBBLE_COUNT];

    private View view;
	private ArrayList<Voice> vobbleArray;

    private String userId;
    private Location mLocation;

    public ShowVobblesFragment(String userId) {
        this.userId = userId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		view = inflater.inflate(R.layout.fragment_show_vobbles, null);

        initResources(view);
        initVobbles();
        return view;
	}

    private void initResources(View view) {
    	mLocation = CommonUtils.getLocation(this.getActivity());
        vobbleArray = new ArrayList<Voice>();
        for (int i = 1; i <= VOBBLE_COUNT; i++) {
            int resId = getResources().getIdentifier(VOBBLE_IMAGEVIEW_ID_PREFIX + i, "id", this.getActivity().getPackageName());
            vobbleBnts[i - 1] = resId;
            view.findViewById(resId).setOnClickListener(vobbleClickListener);
        	view.findViewById(resId).setTag(i - 1);
    	}
    }

    private void initVobbles() {
    	String url;

        if (userId.equals("")) {
            url = URL.VOBBLES;
        } else {
            url = String.format(URL.USER_VOBBLES, userId);
        }

        RequestParams params = new RequestParams();
        params.put(Vobble.LIMIT, VOBBLE_COUNT + "");

        if (mLocation != null) {
            params.put(Vobble.LATITUDE, mLocation.getLatitude() + "");
            params.put(Vobble.LONGITUDE, mLocation.getLongitude() + "");
        } else {
            alert(R.string.error_cannot_use_gps);
            return;
        }

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
				for (int i = 0; i < dataArr.length(); i++) {
					Voice station = Voice.parse(dataArr.optJSONObject(i));
					vobbleArray.add(station);
				}
				initVobbleImage();
			}
		});
    }

    private void initVobbleImage() {
    	int vobbleCnt = (vobbleArray.size() > VOBBLE_COUNT) ? VOBBLE_COUNT : vobbleArray.size();
        for (int i = 0; i < vobbleCnt; i++) {
    		Voice voice = vobbleArray.get(i);
        	if (voice.isImageExist()) {
        		final ImageView vobbleImg = (ImageView) view.findViewById(vobbleBnts[i]);
        		
            	ImageSize targetSize = new ImageSize(vobbleImg.getWidth(), vobbleImg.getHeight());
            	DisplayImageOptions options = new DisplayImageOptions.Builder()
        		// TODO- 이미지 로딩 중 실패 이미지 넣기
                /*
        		.showImageOnLoading(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                */
                .build();
            	ImageLoader.getInstance().loadImage(voice.getImageUrl(), targetSize, options, new SimpleImageLoadingListener() {
        		    @Override
        		    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
        		        // Do whatever you want with Bitmap
        		    	//Animation fadeIn = AnimationUtils.loadAnimation(activity, R.anim.vobble_scale_up);
        		    	vobbleImg.setImageBitmap(CommonUtils.getCroppedBitmap(loadedImage, vobbleImg.getWidth()));
        		    	//vobbleImg.startAnimation(fadeIn);
        		    }
        		});
        	}
    	}
    }
    private View.OnClickListener vobbleClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// 임시 처리
			int tag = (Integer)v.getTag();
			if(tag < vobbleArray.size()){
				Intent intent = new Intent(activity.getApplicationContext(), ListenVobbleActivity.class);
				Voice voice = vobbleArray.get(tag);
				intent.putExtra("vobble", voice);
				startActivity(intent);
			}
		}
	};
}
