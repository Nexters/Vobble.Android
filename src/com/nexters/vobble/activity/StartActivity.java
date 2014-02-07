package com.nexters.vobble.activity;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import com.nexters.vobble.R;

public class StartActivity extends Activity implements View.OnClickListener {
	private ImageView loginImageView;
	private ImageView signUpImageView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_start);

        initResources();
        initEvents();
	}

    private void initResources() {
        loginImageView = (ImageView) findViewById(R.id.loginBtn);
        signUpImageView = (ImageView) findViewById(R.id.signUpBtn);
    }

    private void initEvents() {
        loginImageView.setOnClickListener(this);
        signUpImageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch(view.getId()) {
        case R.id.loginBtn:
            intent = new Intent(StartActivity.this, SignInActivity.class);
            startActivity(intent);
            break;
        case R.id.signUpBtn:
            intent = new Intent(StartActivity.this, SignUpActivity.class);
            startActivity(intent);
            break;
        }
    }
}
