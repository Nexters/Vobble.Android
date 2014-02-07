package com.nexters.vobble;

import org.json.JSONObject;

import android.app.*;
import android.content.*;
import android.content.DialogInterface.OnCancelListener;
import android.os.*;
import android.preference.PreferenceManager;
import android.view.*;
import android.widget.*;
import com.nexters.vobble.core.*;

import com.loopj.android.http.RequestParams;

import com.nexters.vobble.network.*;
import com.nexters.vobble.core.*;

public class SignInActivity extends BaseActivity implements View.OnClickListener {

    private EditText etEmail;
    private EditText etPassword;
    private Button btnSignIn;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_sign_in);

        initResources();
        initEvents();
	}

    private void initResources() {
        etEmail = (EditText) findViewById(R.id.et_sign_in_email);
        etPassword = (EditText) findViewById(R.id.et_sign_in_password);
        btnSignIn = (Button) findViewById(R.id.btn_sign_in);
    }

    private void initEvents() {
        btnSignIn.setOnClickListener(this);
    }


    public void onClick(View v) {
        if (isAllFormsFilled()) {
        	executeSignIn();
        } else {
            alert("이메일과 비밀번호를 올바르게 입력해주세요.");
        }
    }
	
    private String getPassword() {
        return etPassword.getText().toString();
    }

    private String getEmail() {
        return etEmail.getText().toString();
    }

    private boolean isAllFormsFilled() {
        return !getEmail().equals("") && !getPassword().equals("");
    }

    private void executeSignIn() {
        String url = URL.SIGN_IN;

        RequestParams params = new RequestParams();
        params.put(Vobble.EMAIL, getEmail());
        params.put(Vobble.PASSWORD, getPassword());

        HttpUtil.post(url, null, params, new VobbleResponseHandler(SignInActivity.this) {

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
                PreferenceManager.getDefaultSharedPreferences(SignInActivity.this)
                        .edit().putString(Vobble.TOKEN, response.optString(Vobble.TOKEN)).commit();
                PreferenceManager.getDefaultSharedPreferences(SignInActivity.this)
                .edit().putString(Vobble.USER_ID, response.optString(Vobble.USER_ID)).commit();
                
                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
