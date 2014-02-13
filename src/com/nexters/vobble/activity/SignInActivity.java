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
import com.nexters.vobble.adapter.CustomFragmentPagerAdapter;
import com.nexters.vobble.core.Vobble;
import com.nexters.vobble.network.HttpUtil;
import com.nexters.vobble.network.URL;
import com.nexters.vobble.network.VobbleResponseHandler;

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
