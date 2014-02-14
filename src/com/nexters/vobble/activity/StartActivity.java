package com.nexters.vobble.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.nexters.vobble.R;

public class StartActivity extends Activity implements View.OnClickListener {
	private Button mBtnSignIn;
	private Button mBtnSignUp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_start);

        initResources();
        initEvents();
	}

    private void initResources() {
        mBtnSignIn = (Button) findViewById(R.id.btn_sign_in);
        mBtnSignUp = (Button) findViewById(R.id.btn_sign_up);
    }

    private void initEvents() {
        mBtnSignIn.setOnClickListener(this);
        mBtnSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch(view.getId()) {
        case R.id.btn_sign_in:
            intent = new Intent(StartActivity.this, SignInActivity.class);
            startActivity(intent);
            break;
        case R.id.btn_sign_up:
            intent = new Intent(StartActivity.this, SignUpActivity.class);
            startActivity(intent);
            break;
        }
    }
}
