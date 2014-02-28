package com.nexters.vobble.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import com.nexters.vobble.activity.CreateVobbleActivity;
import com.nexters.vobble.activity.ListenVobbleActivity;
import com.nexters.vobble.core.AccountManager;
import com.nexters.vobble.core.App;
import com.nexters.vobble.listener.ImageViewTouchListener;
import com.nexters.vobble.network.APIResponseHandler;
import com.nexters.vobble.util.ImageManagingHelper;
import com.nhn.android.maps.NMapLocationManager;
import com.nhn.android.maps.maplib.NGeoPoint;
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
import android.widget.ImageView;

import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.loopj.android.http.RequestParams;
import com.nexters.vobble.R;
import com.nexters.vobble.network.HttpUtil;
import com.nexters.vobble.network.URL;
import com.nexters.vobble.entity.User;
import com.nexters.vobble.entity.Vobble;

@SuppressLint("ValidFragment")
public class ShowVobblesFragment extends BaseMainFragment {
    public static enum VOBBLE_FRAMGMENT_TYPE { ALL, MY };
    private static final int VOBBLE_COUNT = 12;
    private static final String VOBBLE_IMG_ID_PREFIX = "iv_vobble_";

    private String userId;
    private VOBBLE_FRAMGMENT_TYPE type;

    private ImageView mIvCreateVobble;
    private ImageView[] mIvVobbleImages = new ImageView[VOBBLE_COUNT];
    private ArrayList<Vobble> mVobbles = new ArrayList<Vobble>();

    private NMapLocationManager mLocationManager;
    private OnFragmentListener mCallback;

    // [주의] 빈 생성자를 사용하지는 않지만 만들어놓아야 함
    // 이유는 모르겠지만 빈 생성자가 없으면 프래그먼트에서 에러를 내뱉음
    // 아마도 안드로이드 프래그먼트 내부 버그인 것으로 보임
    public ShowVobblesFragment() {}

    public ShowVobblesFragment(String userId, VOBBLE_FRAMGMENT_TYPE type) {
        this.userId = userId;
        this.type = type;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_show_vobbles, null);
        initResources(view);
        initEvents();
        initVobbles();
        return view;
    }

    private void initResources(View view) {
        mIvCreateVobble = (ImageView) view.findViewById(R.id.iv_voice_record_btn);
        for (int i = 0; i < VOBBLE_COUNT; i++) {
            mIvVobbleImages[i] = (ImageView) view.findViewById(getResourceId(VOBBLE_IMG_ID_PREFIX + i));
        }
    }

    private int getResourceId(String id) {
        return getResources().getIdentifier(id, "id", this.getActivity().getPackageName());
    }

    private void initEvents() {
        ImageViewTouchListener ivTouchListener = new ImageViewTouchListener();

        mIvCreateVobble.setOnTouchListener(ivTouchListener);
        mIvCreateVobble.setOnClickListener(btnClickListener);
        for (int i = 0; i < VOBBLE_COUNT; i++) {
            mIvVobbleImages[i].setOnTouchListener(ivTouchListener);
            mIvVobbleImages[i].setOnClickListener(vobbleClickListener);
            mIvVobbleImages[i].setTag(i);
            if (type == VOBBLE_FRAMGMENT_TYPE.MY) {
                mIvVobbleImages[i].setOnLongClickListener(vobbleLongClickListener);
            }
        }
    }

    private void initVobbles() {
        // 타입이 ALL인 경우에만 생성과 동시에 보블을 초기화한다.
        if (type == VOBBLE_FRAMGMENT_TYPE.ALL)
            load();
    }

    @Override
    public void load() {
        mLocationManager = new NMapLocationManager(getActivity());
        mLocationManager.setOnLocationChangeListener(mLocationListener);
        if (!mLocationManager.enableMyLocation(false)) {
            showDialogForLocationAccessSetting();
        }
    }

    private NMapLocationManager.OnLocationChangeListener mLocationListener = new NMapLocationManager.OnLocationChangeListener() {
        @Override
        public boolean onLocationChanged(NMapLocationManager nMapLocationManager, NGeoPoint location) {
            if (location != null) {
                executeGetVobbles(location);
            } else {
                activity.showShortToast("위치 탐색에 실패했습니다. 다시 시도해주세요.");
            }
            return false;
        }

        @Override
        public void onLocationUpdateTimeout(NMapLocationManager nMapLocationManager) {

        }

        @Override
        public void onLocationUnavailableArea(NMapLocationManager nMapLocationManager, NGeoPoint nGeoPoint) {

        }
    };

    @Override
    public void onStart() {
        super.onStart();
        App.getGaTracker().set(Fields.SCREEN_NAME, type.toString());
        App.getGaTracker().send(MapBuilder.createAppView().build());
    }

    private void executeGetVobbles(NGeoPoint location) {
    	if (location == null)
            return;

        String url = getUrlForGetVobbles();

        RequestParams params = new RequestParams();
        params.put(App.LIMIT, String.valueOf(VOBBLE_COUNT));
        params.put(Vobble.LATITUDE, String.valueOf(location.getLatitude()));
        params.put(Vobble.LONGITUDE, String.valueOf(location.getLongitude()));

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
                super.onSuccess(response);
                clearAndAddVobbles(response.optJSONArray("vobbles"));
				loadVobbleImages();
			}

            @Override
			public void onFailure(Throwable error, String response){
				super.onFailure(error, response);
				loadVobbleImages();
			}

            private void clearAndAddVobbles(JSONArray vobbles) {
                mVobbles.clear();
                int count = vobbles.length();
                for (int i = 0; i < count; i++) {
                    mVobbles.add(Vobble.build(vobbles.optJSONObject(i)));
                }
            }
		});
    }

    private String getUrlForGetVobbles() {
        if (type == VOBBLE_FRAMGMENT_TYPE.ALL) {
            return URL.VOBBLES;
        } else if (type == VOBBLE_FRAMGMENT_TYPE.MY) {
            return String.format(URL.USER_VOBBLES, userId);
        } else {
            // not supported
            return "";
        }
    }

    private void loadVobbleImages() {
        new AttachImageTask().execute();
    }

    private class AttachImageTask extends AsyncTask<Void, Integer, Void> {
        private final int IMAGE_LOADING_INTERVAL = 200;
        private int numVobbles;

        @Override
        protected void onPreExecute() {
            numVobbles = mVobbles.size();
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
            if (i < numVobbles) {
                Animation scaleUp = AnimationUtils.loadAnimation(activity, R.anim.vobble_scale_up);
                ImageManagingHelper.loadAndAttachCroppedImage(mIvVobbleImages[i], mVobbles.get(i).getImageUrl(), scaleUp);
                mIvVobbleImages[i].setVisibility(View.VISIBLE);
            } else {
                mIvVobbleImages[i].setVisibility(View.INVISIBLE);
            }
        }
    }

    private View.OnClickListener vobbleClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			int tag = (Integer) v.getTag();
			if (tag < mVobbles.size()) {
				Intent intent = new Intent(activity.getApplicationContext(), ListenVobbleActivity.class);
				Vobble vobble = mVobbles.get(tag);
				intent.putExtra("vobble", vobble);
				startActivity(intent);
			}
		}
	};

	private View.OnLongClickListener vobbleLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            final int tag = (Integer) view.getTag();
            showVobbleLongClickDialog(tag);
            return false;
        }
    };

    private void showVobbleLongClickDialog(final int tag) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.fragment_my_vobble_long_click_dialog_title);
        builder.setItems(R.array.menu_vobble_long_click_dialog, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        executeDeleteVobble(tag);
                        break;
                }
            }
        });
        builder.show();
    }

    private void executeDeleteVobble(int idx) {
        if (idx >= mVobbles.size()) {
            return;
        }
        final int tag = idx;
        String vobbleId = String.valueOf(mVobbles.get(idx).getVobbleId());
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
                animateRemoveVobble(tag);
            }

            @Override
            public void onFailure(Throwable error, String response) {
                super.onFailure(error, response);
            }
        });
    }

    private void animateRemoveVobble(final int idx) {
        Animation scaleDown = AnimationUtils.loadAnimation(activity, R.anim.vobble_scale_down);
        mIvVobbleImages[idx].startAnimation(scaleDown);
        scaleDown.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationRepeat(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                mCallback.onRemovedVobble();
            }
        });
    }

    private View.OnClickListener btnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.iv_voice_record_btn) {
                Intent intent = new Intent(activity.getApplicationContext(), CreateVobbleActivity.class);
                startActivity(intent);
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLocationManager != null && mLocationManager.isMyLocationEnabled())
            mLocationManager.disableMyLocation();
    }
}
