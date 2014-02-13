package com.nexters.vobble.activity;

import org.json.*;

import android.content.*;
import android.os.*;
import android.preference.*;
import android.support.v4.app.*;
import android.support.v4.view.*;
import android.view.*;
import android.widget.*;

import com.loopj.android.http.*;
import com.nexters.vobble.*;
import com.nexters.vobble.adapter.*;
import com.nexters.vobble.core.*;
import com.nexters.vobble.network.*;

public class SignInActivity extends BaseFragmentActivity implements View.OnClickListener {
    private EditText etEmail;
    private EditText etPassword;
    private Button btnSignIn;
    private CustomFragmentPagerAdapter adapter;

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
            alert(R.string.error_signin);
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
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }
}
