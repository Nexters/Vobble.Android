package com.nexters.vobble.fragment;

import java.util.ArrayList;

import android.location.Location;
import com.nexters.vobble.activity.ListenVobbleActivity;
import com.nexters.vobble.entity.User;
import com.nexters.vobble.network.APIResponseHandler;
import com.nexters.vobble.util.LocationHelper;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.nexters.vobble.util.CommonUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

@SuppressLint("ValidFragment")
public class ShowVobblesFragment extends BaseFragment{
    private static final int VOBBLE_COUNT = 12;
    private static final String VOBBLE_IMAGEVIEW_ID_PREFIX = "iv_vobble_";

    private Integer[] vobbleBnts = new Integer[VOBBLE_COUNT];
    private Integer[] vobbleBacks = new Integer[VOBBLE_COUNT];
    private ArrayList<Vobble> vobbleArray = new ArrayList<Vobble>();

    private View view;

    private String userId;
    private Location mLocation;

    public ShowVobblesFragment(String userId) {
        this.userId = userId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

        view = inflater.inflate(R.layout.fragment_show_vobbles, null);

        initLocation();
        initResources(view);
        initVobbles();
        return view;
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

    private void initResources(View view) {
        int resId, backResId;
        for (int i = 1; i <= VOBBLE_COUNT; i++) {
            resId = getResources().getIdentifier(VOBBLE_IMAGEVIEW_ID_PREFIX + i, "id", this.getActivity().getPackageName());
            vobbleBnts[i - 1] = resId;
            backResId = getResources().getIdentifier(VOBBLE_IMAGEVIEW_ID_PREFIX + "back_" + i, "id", this.getActivity().getPackageName());
            vobbleBacks[i - 1] = backResId;
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
        params.put(User.LIMIT, VOBBLE_COUNT + "");
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
				vobbleArray.clear();
				JSONArray dataArr = response.optJSONArray("vobbles");
				for (int i = 0; i < dataArr.length(); i++) {
					Vobble station = Vobble.build(dataArr.optJSONObject(i));
					vobbleArray.add(station);
				}
				initVobbleImage();
			}
			@Override
			public void onFailure(Throwable error, String response){
				super.onFailure(error, response);
				initVobbleImage();
			}
		});
    }

    private void initVobbleImage() {    	
    	int vobbleCnt = VOBBLE_COUNT;
        for (int i = 0; i < vobbleCnt; i++) {
        	if(i < vobbleArray.size()){
        		Vobble vobble = vobbleArray.get(i);
            	if (vobble.isImageExist()) {
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
                	ImageLoader.getInstance().loadImage(vobble.getImageUrl(), targetSize, options, new SimpleImageLoadingListener() {
            		    @Override
            		    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            		        // Do whatever you want with Bitmap
            		    	Animation fadeIn = AnimationUtils.loadAnimation(activity, R.anim.vobble_scale_up);
            		    	vobbleImg.setImageBitmap(CommonUtils.getCroppedBitmap(loadedImage, vobbleImg.getWidth()));
            		    	vobbleImg.startAnimation(fadeIn);
            		    }
            		});
            	}
        	}else{
        		final ImageView vobbleImg = (ImageView) view.findViewById(vobbleBnts[i]);
        		vobbleImg.setVisibility(View.INVISIBLE);
        		final ImageView vobbleBackImg = (ImageView) view.findViewById(vobbleBacks[i]);
        		vobbleBackImg.setVisibility(View.INVISIBLE);
        	}
    		
    	}
    }

    public void reloadVobbles(){
    	initVobbles();
    }

    private View.OnClickListener vobbleClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// 임시 처리
			int tag = (Integer)v.getTag();
			if(tag < vobbleArray.size()){
				Intent intent = new Intent(activity.getApplicationContext(), ListenVobbleActivity.class);
				Vobble vobble = vobbleArray.get(tag);
				intent.putExtra("vobble", vobble);
				startActivity(intent);
			}
		}
	};
}
