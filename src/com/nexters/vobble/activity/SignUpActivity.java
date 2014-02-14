package com.nexters.vobble.activity;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.android.http.RequestParams;
import com.nexters.vobble.R;
import com.nexters.vobble.core.Vobble;
import com.nexters.vobble.network.HttpUtil;
import com.nexters.vobble.network.URL;
import com.nexters.vobble.network.VobbleResponseHandler;

public class SignUpActivity extends BaseActivity implements View.OnClickListener {

    private EditText mEtUsername;
    private EditText mEtEmail;
    private EditText mEtPassword;
    private EditText mEtPasswordCheck;
    private Button mBtnSignUp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sign_up);

        initResources();
        initEvents();
	}

    private void initResources() {
        mEtUsername = (EditText) findViewById(R.id.et_sign_up_username);
        mEtEmail = (EditText) findViewById(R.id.et_sign_up_email);
        mEtPassword = (EditText) findViewById(R.id.et_sign_up_password);
        mEtPasswordCheck = (EditText) findViewById(R.id.et_sign_up_password_check);
        mBtnSignUp = (Button) findViewById(R.id.btn_sign_up);
    }

    private void initEvents() {
        mBtnSignUp.setOnClickListener(this);
    }

    private String getUsername() {
        return mEtUsername.getText().toString();
    }

    private String getEmail() {
        return mEtEmail.getText().toString();
    }

    private String getPassword() {
        return mEtPassword.getText().toString();
    }

    private String getPasswordCheck() {
        return mEtPasswordCheck.getText().toString();
    }

    private boolean isAllFormFilled() {
        return !(getUsername().equals("") || getEmail().equals("") ||
                getPassword().equals("") || getPasswordCheck().equals(""));
    }

    private boolean isPasswordCheckCorrected() {
        return getPassword().equals(getPasswordCheck());
    }

    private boolean isValidUsername() {
        return getUsername().length() >= 4;
    }

    private boolean isValidEmail() {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(getEmail()).matches();
    }

    private boolean isValidPassword() {
        return getPassword().length() >= 6;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        case R.id.btn_sign_up:
            if (!isAllFormFilled()) {
                alert(R.string.error_signup_form);
            } else if (!isPasswordCheckCorrected()) {
                alert(R.string.error_signup_password);
            } else if (!isValidUsername()) {
                alert(R.string.error_signup_username);
            } else if (!isValidEmail()) {
                alert(R.string.error_signup_email);
            } else if (!isValidPassword()) {
                alert(R.string.error_signup_passwrd_length);
            } else {
                executeSignUp();
            }
            break;
        }
    }

    private void executeSignUp() {
        String url = URL.SIGN_UP;

        RequestParams params = new RequestParams();
        params.put(Vobble.EMAIL, getEmail());
        params.put(Vobble.PASSWORD, getPassword());
        params.put(Vobble.USERNAME, getUsername());

        HttpUtil.post(url, null, params, new VobbleResponseHandler(SignUpActivity.this) {

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
                Vobble.setUsername(SignUpActivity.this, getUsername());
                Vobble.setEmail(SignUpActivity.this, getEmail());

				Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
			}
        });
    }
}
