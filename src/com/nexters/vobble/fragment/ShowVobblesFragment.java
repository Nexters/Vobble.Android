package com.nexters.vobble.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGestureListener;
import android.gesture.GestureOverlayView.OnGesturingListener;
import android.graphics.PorterDuff;
import android.location.Location;
import android.os.AsyncTask;
import android.view.MotionEvent;
import com.nexters.vobble.activity.CreateVobbleActivity;
import com.nexters.vobble.activity.ListenVobbleActivity;
import com.nexters.vobble.core.AccountManager;
import com.nexters.vobble.core.App;
import com.nexters.vobble.listener.ImageViewTouchListener;
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
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.loopj.android.http.RequestParams;
import com.nexters.vobble.R;
import com.nexters.vobble.network.HttpUtil;
import com.nexters.vobble.network.URL;
import com.nexters.vobble.entity.User;
import com.nexters.vobble.entity.Vobble;

@SuppressLint("ValidFragment")
public class ShowVobblesFragment extends BaseMainFragment{
    public enum VOBBLE_FRAMGMENT_TYPE { ALL, MY };
    private static final int VOBBLE_COUNT = 12;
    private static final String VOBBLE_IMG_ID_PREFIX = "iv_vobble_";

    private ImageView mIvCreateVobble;
    private ImageView[] vobbleImageViews = new ImageView[VOBBLE_COUNT];
    private Button[] vobbleRemoveButtons = new Button[VOBBLE_COUNT];
    private ArrayList<Vobble> vobbleList = new ArrayList<Vobble>();

    private String userId;
    private Location mLocation;
    private VOBBLE_FRAMGMENT_TYPE type;

    public ShowVobblesFragment(String userId, VOBBLE_FRAMGMENT_TYPE type) {
        this.userId = userId;
        this.type = type;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (type == VOBBLE_FRAMGMENT_TYPE.ALL)
            load();
    }

    @Override
	public void onStart() {
		super.onStart();
		App.getGaTracker().set(Fields.SCREEN_NAME, type.toString());
		App.getGaTracker().send(MapBuilder.createAppView().build());
	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_show_vobbles, null);
        initResources(view);
        initEvents();
        
        return view;
	}
    
    private void initResources(View view) {
        mIvCreateVobble = (ImageView) view.findViewById(R.id.iv_voice_record_btn);
        for (int i = 1; i <= VOBBLE_COUNT; i++) {
            int resId = getResources().getIdentifier(VOBBLE_IMG_ID_PREFIX + i, "id", this.getActivity().getPackageName());
            vobbleImageViews[i - 1] = (ImageView) view.findViewById(resId);
            resId = getResources().getIdentifier(VOBBLE_IMG_ID_PREFIX + "remove_" + i, "id", this.getActivity().getPackageName());
            vobbleRemoveButtons[i-1] = (Button) view.findViewById(resId);
        }
        view.findViewById(R.id.show_vobble_ll).setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				removeVobbleCancel();
				return false;
			}
		});
    }

    private void initEvents() {
        ImageViewTouchListener ivTouchListener = new ImageViewTouchListener();
        mIvCreateVobble.setOnClickListener(btnClickListener);
        mIvCreateVobble.setOnTouchListener(ivTouchListener);
        for (int i = 0; i < VOBBLE_COUNT; i++) {
            vobbleImageViews[i].setOnTouchListener(ivTouchListener);
            vobbleImageViews[i].setOnClickListener(vobbleClickListener);
            vobbleImageViews[i].setTag(i);
            if (type == VOBBLE_FRAMGMENT_TYPE.MY) {
                vobbleImageViews[i].setOnLongClickListener(vobbleLongClickListener);
                vobbleRemoveButtons[i].setOnClickListener(vobbleRemoveClickListener);
                vobbleRemoveButtons[i].setTag(i);
            }
        }
        
    }

    @Override
    public void load() {
        initLocation();
        loadVobbles();
    }

    private void initLocation() {
        LocationHelper locationHelper = new LocationHelper(getActivity());
        if (locationHelper.isGPSEnabled()) {
            mLocation = locationHelper.getCurrentLocation();
            if (mLocation == null) {
            	mLocation = new Location("");
                mLocation.setLatitude(37);
                mLocation.setLongitude(127);
            }
        } else {
            alert(R.string.error_cannot_use_gps);
            mLocation = new Location("");
            mLocation.setLatitude(37);
            mLocation.setLongitude(127);
        }
    }

    private void loadVobbles() {
    	String url = "";
        if (type == VOBBLE_FRAMGMENT_TYPE.ALL) {
            url = URL.VOBBLES;
        } else if (type == VOBBLE_FRAMGMENT_TYPE.MY) {
            url = String.format(URL.USER_VOBBLES, userId);
        }

        RequestParams params = new RequestParams();
        params.put(App.LIMIT, VOBBLE_COUNT + "");
        params.put(Vobble.LATITUDE, String.valueOf(mLocation.getLatitude()));
        params.put(Vobble.LONGITUDE, String.valueOf(mLocation.getLongitude()));

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
    private void deleteVobble(int idx) {
    	if(idx >= vobbleList.size()){
    		return ;
    	}
    	final int tag = idx;
    	String vobbleId = String.valueOf(vobbleList.get(idx).getVobbleId());
    	String url = String.format(URL.USER_VOBBLES_DELETE, userId, vobbleId);
    	RequestParams params = new RequestParams();
        params.put(User.TOKEN, AccountManager.getInstance().getToken(activity));
        
		HttpUtil.post(url, null, params, new APIResponseHandler(activity) {

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
				removeVobbleImages(tag);
			}
			@Override
			public void onFailure(Throwable error, String response){
				super.onFailure(error, response);
			}
			@Override
			public void onFailure(Throwable error, JSONObject response){
				super.onFailure(error, response);
			}
		});
    }
    private void loadVobbleImages() {
        new AttachImageHelper().execute();
    }
    private void removeVobbleImages(final int idx) {
    	Animation scaleDown = AnimationUtils.loadAnimation(activity, R.anim.vobble_scale_down);
		vobbleImageViews[idx].startAnimation(scaleDown);
		scaleDown.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {}
			@Override
			public void onAnimationRepeat(Animation animation) {}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				vobbleImageViews[idx].setVisibility(View.INVISIBLE);
				vobbleList.remove(idx);
				loadVobbleImages();
			}
		});
    }
    public void removeVobbleClick(){
    	for(int i=0; i < vobbleList.size(); i++){
    		vobbleRemoveButtons[i].setVisibility(View.VISIBLE);
    	}
    }
    public void removeVobbleCancel(){
    	for(int i=0; i < vobbleList.size(); i++){
    		vobbleRemoveButtons[i].setVisibility(View.INVISIBLE);
    	}
    }
    private class AttachImageHelper extends AsyncTask<Void, Integer, Void> {
        private final int IMAGE_LOADING_INTERVAL = 200;
        private int vobbleListSize;

        @Override
        protected void onPreExecute() {
            vobbleListSize = vobbleList.size();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            for (int i = 0; i < VOBBLE_COUNT; i++) {
                publishProgress(i);
                try {
                    Thread.sleep(IMAGE_LOADING_INTERVAL);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int i = values[0];
            if (i < vobbleListSize) {
                Animation scaleUp = AnimationUtils.loadAnimation(activity, R.anim.vobble_scale_up);
                ImageManagingHelper.loadAndAttachCroppedImage(vobbleImageViews[i], vobbleList.get(i).getImageUrl(), scaleUp);
                vobbleImageViews[i].setVisibility(View.VISIBLE);
            } else {
                vobbleImageViews[i].setVisibility(View.INVISIBLE);
            }
        }
    }
    
    private View.OnLongClickListener vobbleLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            return false;
        }
    };

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
	private View.OnClickListener vobbleRemoveClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			final int tag = (Integer) v.getTag();
			removeVobbleCancel();
			deleteVobble(tag);
		}
	};
    private View.OnClickListener btnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.iv_voice_record_btn) {
                Intent intent = new Intent(activity.getApplicationContext(), CreateVobbleActivity.class);
                startActivity(intent);
            }
        }
    };

}
