package com.nexters.vobble.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.nexters.vobble.R;
import com.nexters.vobble.activity.BaseFragmentActivity;


public class BaseFragment extends Fragment {

	public LayoutInflater inflater;
	public BaseFragmentActivity activity;
	private AlertDialog mAlertDialog;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		inflater = LayoutInflater.from(activity);
		this.activity = (BaseFragmentActivity) activity;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		this.inflater = inflater;
		return container;
	}

	public void showLoading() {
		activity.showLoading();
	}

	public void hideLoading() {
		activity.hideLoading();
	}

    public void showAlert(int resId) {
        showAlert(getString(resId));
    }

    public void showAlert(String message) {
        if (mAlertDialog != null && mAlertDialog.isShowing())
            return;

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setMessage(message);
        builder.setPositiveButton(R.string.ok, null);

        mAlertDialog = builder.create();
        mAlertDialog.show();

        TextView messageText = (TextView) mAlertDialog.findViewById(android.R.id.message);
        messageText.setGravity(Gravity.CENTER);
    }

    public void showDialogForLocationAccessSetting() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle("위치 정보 액세스 설정");
        builder.setMessage("시스템 설정에 의해서 위치 탐색이 불가능합니다. 위치 정보 액세스 설정을 변경한 뒤 다시 시도해주세요.");
        builder.setCancelable(false);
        builder.setIcon(getResources().getDrawable(R.drawable.icn_96));
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });
        builder.create().show();
    }
}
