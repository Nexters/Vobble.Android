package com.nexters.vobble;

import android.os.Bundle;
import android.app.Activity;
import android.view.*;
import android.support.v4.app.NavUtils;

public class MapActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_map);
	}
}
