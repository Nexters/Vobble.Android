package com.nexters.vobble.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.nexters.vobble.R;

public class StartActivity extends Activity implements View.OnClickListener {
	public static final int REQUEST_SIGN_IN = 0;
    public static final int REQUEST_SIGN_UP = 1;

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
        switch(view.getId()) {
        case R.id.btn_sign_in:
            dispatchSignIn();
            break;
        case R.id.btn_sign_up:
            dispatchSignUp();
            break;
        }
    }

    private void dispatchSignIn() {
        Intent intent = new Intent(StartActivity.this, SignInActivity.class);
        startActivityForResult(intent, REQUEST_SIGN_IN);
    }

    private void dispatchSignUp() {
        Intent intent = new Intent(StartActivity.this, SignUpActivity.class);
        startActivityForResult(intent, REQUEST_SIGN_UP);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                this.finish();
            }
        } else if (requestCode == REQUEST_SIGN_UP) {
            if (resultCode == Activity.RESULT_OK) {
                dispatchSignIn();
            }
        }
    }
}
