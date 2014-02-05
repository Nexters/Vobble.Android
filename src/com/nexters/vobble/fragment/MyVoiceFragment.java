package com.nexters.vobble.fragment;


import java.util.*;

import android.location.*;
import android.os.*;
import android.support.v4.app.*;
import android.view.*;
import android.widget.*;

import com.nexters.vobble.*;
import com.nexters.vobble.record.*;
import com.nexters.vobble.view.*;

public class MyVoiceFragment extends Fragment{
	private View view = null;
	

//	private TextView vobbleCountTextView = null;
//	private LinearLayout layout1 = null;
//	private LinearLayout layout2 = null;
//	private LinearLayout layout3 = null;
//	private LinearLayout layout4 = null;
//	private LinearLayout layout5 = null;
//	private LinearLayout layout6 = null;

//	private static VobbleWidget[] vobbleArr = new VobbleWidget[6];
	// location
//	private ImageView updateLoacationImageView = null;
//	private LocationManager locationManager = null;
//	private double currentLongitude = 0;
//	private double currentLatitude = 0;
//	private Criteria criteria = null;
//	private MainActivity mainActivity = null;
//	private String bestProvider = "";

//	private VobbleCountAsyncTask vobbleCountAsyncTask = null;
//	private DisplayImageOptions options = null;

//private AnimateFirstDisplayListener animateListener = new AnimateFirstDisplayListener();

//	private static List<Voice> voiceList = null;
//	private int count = 0;
//	private int pageNum = 1;
//	private int userId = -1;
	
//	private RecordManager recordManager = null;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		view = inflater.inflate(R.layout.fragment_my_voice, null);
		

		/*VobblePrefsManager manager = new VobblePrefsManager(getActivity());
		userId = manager.getUserId();

		options = new DisplayImageOptions.Builder().cacheInMemory(false)
				.cacheOnDisc(true).build();

		mainActivity = (MainActivity) getActivity();
		recordManager = new RecordManager();

		vobbleCountTextView = (TextView) view
				.findViewById(R.id.vobbleCountTextView);
		updateLoacationImageView = (ImageView) view
				.findViewById(R.id.updateLocationImagaView);

		layout1 = (LinearLayout) view.findViewById(R.id.vobbleLayout1);
		layout2 = (LinearLayout) view.findViewById(R.id.vobbleLayout2);
		layout3 = (LinearLayout) view.findViewById(R.id.vobbleLayout3);
		layout4 = (LinearLayout) view.findViewById(R.id.vobbleLayout4);
		layout5 = (LinearLayout) view.findViewById(R.id.vobbleLayout5);
		layout6 = (LinearLayout) view.findViewById(R.id.vobbleLayout6);

		for (int i = 0; i < vobbleArr.length; i++) {
			final int ii = i;
			vobbleArr[i] = new VobbleWidget(getActivity());
		}

		
		layout1.addView(vobbleArr[0]);
		layout2.addView(vobbleArr[1]);
		layout3.addView(vobbleArr[2]);
		layout4.addView(vobbleArr[3]);
		layout5.addView(vobbleArr[4]);
		layout6.addView(vobbleArr[5]);

		loadCurrentLocation();

		updateLoacationImageView.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				String bestProvider = locationManager.getBestProvider(criteria,
						true); // ���� ���� �ϳ��� �����

				String result = currentLongitude + ", " + currentLatitude
						+ "\n";

				Location location = locationManager
						.getLastKnownLocation(bestProvider);
				currentLatitude = location.getLatitude();
				currentLongitude = location.getLongitude();

				// result += currentLongitude + ", " + currentLatitude;
				// Toast.makeText(getActivity(), result,
				// Toast.LENGTH_LONG).show();
			}
		});
		*/
		return view;
	}
	/*
	public LocationListener locationLisenter = new LocationListener() {
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		public void onProviderEnabled(String provider) {

		}

		public void onProviderDisabled(String provider) {

		}

		public void onLocationChanged(Location location) {

			String result = currentLongitude + ", " + currentLatitude + "\n";
			currentLatitude = location.getLatitude();
			currentLongitude = location.getLongitude();

			result += currentLongitude + ", " + currentLatitude;
			// Toast.makeText(getActivity(), "listener!!!\n" + result,
			// Toast.LENGTH_LONG).show();
		}
	};

	public void loadCurrentLocation() {

		locationManager = (LocationManager) getActivity().getSystemService(
				Context.LOCATION_SERVICE);

		criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_COARSE);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setSpeedRequired(false);
		criteria.setCostAllowed(true);

		bestProvider = locationManager.getBestProvider(criteria, true);
		Location location = locationManager.getLastKnownLocation(bestProvider);
		Toast.makeText(getActivity(),
				location.getLongitude() + ", " + location.getLatitude(),
				Toast.LENGTH_LONG).show();
		currentLatitude = location.getLatitude();
		currentLongitude = location.getLongitude();

		locationManager.requestLocationUpdates(bestProvider, 10000, 0,
				locationLisenter);
		vobbleCountAsyncTask = new VobbleCountAsyncTask();
		vobbleCountAsyncTask.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,
				currentLongitude, currentLatitude);
		GetMyVobbleListAsyncTask getMyVobbleListAsyncTask = new GetMyVobbleListAsyncTask();
		getMyVobbleListAsyncTask.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,
				currentLongitude, currentLatitude);

	}

	public void onResume() {
		super.onResume();
		locationManager.requestLocationUpdates(bestProvider, 10000, 0,
				locationLisenter);
	}

	public void onDestroy() {
		super.onDestroy();
		locationManager.removeUpdates(locationLisenter);
	}

	public void onPause() {
		super.onPause();
		locationManager.removeUpdates(locationLisenter);
	}

	///////

	class GetMyVobbleListAsyncTask extends
			AsyncTask<Double, Integer, List<Voice>> {
		protected List<Voice> doInBackground(Double... params) {
//			return ServerApiRequest
//					.getVobbleList(params[0], params[1], pageNum);
			return ServerApiRequest.getMyVobbleList(params[0], params[1], pageNum, userId);
		}

		protected void onPostExecute(List<Voice> list) {
			voiceList = list;
			for (int i = 0; i < list.size(); i++) {
				final int ii = i;
				ImageLoader.getInstance().loadImage(
						"http://14.63.185.152:3000/download?fileName="
								+ list.get(i).getVoiceImageUri(),
						new ImageLoadingListener() {
							public void onLoadingStarted(String arg0, View arg1) {
							}

							public void onLoadingFailed(String arg0, View arg1,
									FailReason arg2) {
							}

							public void onLoadingComplete(String arg0,
									View arg1, Bitmap arg2) {
								vobbleArr[ii].setVobbleImage(arg2);
							}

							public void onLoadingCancelled(String arg0,
									View arg1) {
							}
						});
				vobbleArr[ii].setOnClickListener(new View.OnClickListener() {
					public void onClick(View view) {
						recordManager.vobblePlay(voiceList.get(ii)
								.getVoiceFileUri(), vobbleArr[ii]);
					}
				});
			}

		}
	}

	private static class AnimateFirstDisplayListener extends
			SimpleImageLoadingListener {
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				for (int i = 0; i < voiceList.size(); i++) {
					if (imageUri
							.equals("http://14.63.185.152:3000/download?fileName="
									+ voiceList.get(i).getVoiceImageUri())) {
						vobbleArr[i].setVobbleImage(loadedImage);

						break;
					}
				}
			}
		}
	}

	class VobbleCountAsyncTask extends AsyncTask<Double, Integer, Integer> {
		private ProgressDialog progressDialog = null;

		public VobbleCountAsyncTask() {
		}

		protected void onPreExecute() {
			progressDialog = progressDialog.show(getActivity(),
					"Get Vobble Count", "wait..");
			super.onPreExecute();
		}

		protected Integer doInBackground(Double... params) {
			int vobbleCount = ServerApiRequest.getVobbleCount(params[0],
					params[1]);
			return vobbleCount;
		}

		protected void onPostExecute(Integer vobbleCount) {
			count = vobbleCount;
			vobbleCountTextView.setText(vobbleCount + "");
			if (vobbleCount >= 0 && vobbleCount <= 6) {
				mainActivity.getPrevVobbleImageView().setVisibility(
						View.INVISIBLE);
				mainActivity.getNextVobbleImageView().setVisibility(
						View.INVISIBLE);
			} else if (vobbleCount > 6) {
				mainActivity.getPrevVobbleImageView().setVisibility(
						View.INVISIBLE);
				mainActivity.getNextVobbleImageView().setVisibility(
						View.VISIBLE);
			}
			progressDialog.dismiss();
			super.onPostExecute(vobbleCount);
		}
	}*/
}
