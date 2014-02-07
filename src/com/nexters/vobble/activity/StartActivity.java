package com.nexters.vobble.activity;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import com.nexters.vobble.R;

public class StartActivity extends Activity implements View.OnClickListener {
	private Button btnSignIn;
	private Button btnSignUp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_start);

        initResources();
        initEvents();
	}

    private void initResources() {
        btnSignIn = (Button) findViewById(R.id.btn_sign_in);
        btnSignUp = (Button) findViewById(R.id.btn_sign_up);
    }

    private void initEvents() {
        btnSignIn.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
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
