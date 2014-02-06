package com.nexters.vobble;

import org.json.JSONObject;

import android.app.*;
import android.content.Intent;
import android.os.*;
import android.preference.PreferenceManager;
import android.view.*;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.loopj.android.http.RequestParams;

import com.nexters.vobble.core.*;
import com.nexters.vobble.network.*;

public class SignUpActivity extends BaseActivity {

    private EditText etUsername;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etPasswordCheck;
    private Button btnSignUp;

    private SignUpnAsyncTask signUpAsyncTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sign_up);

        initResources();
        initEvents();
	}

    private void initResources() {
        etUsername = (EditText) findViewById(R.id.et_sign_up_username);
        etEmail = (EditText) findViewById(R.id.et_sign_up_email);
        etPassword = (EditText) findViewById(R.id.et_sign_up_password);
        etPasswordCheck = (EditText) findViewById(R.id.et_sign_up_password_check);
        btnSignUp = (Button) findViewById(R.id.btn_sign_up);
    }

    private void initEvents() {
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            private boolean isAllFormFilled() {
                return !(etUsername.getText().toString().equals("") || etEmail.getText().toString().equals("") ||
                        etPassword.getText().toString().equals("") || etPasswordCheck.toString().equals(""));
            }
            public  boolean isValidEmail() {
            	return android.util.Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches();
            }
            private boolean isPasswordCheckCorrected() {
                return etPassword.getText().toString().equals(etPasswordCheck.getText().toString());
            }

            @Override
            public void onClick(View view) {
                if (!isAllFormFilled()) {
                    Toast.makeText(SignUpActivity.this, "Fill in all forms!", Toast.LENGTH_SHORT).show();
                } else if (!isPasswordCheckCorrected()) {
                    Toast.makeText(SignUpActivity.this, "Incorrect password!", Toast.LENGTH_SHORT).show();
                } else if(!isValidEmail()){
                	Toast.makeText(SignUpActivity.this, "Incorrect email!", Toast.LENGTH_SHORT).show();
                } else {
                	String url = URL.SIGN_UP;
                	
                	RequestParams params = new RequestParams();
            		params.put(Vobble.EMAIL, etEmail.getText().toString());
            		params.put(Vobble.PASSWORD, etPassword.getText().toString());
            		params.put(Vobble.USERNAME, etUsername.getText().toString());
            		
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
            				Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                            startActivity(intent);
                            finish();
            			}
            		});
                }
            }
        });
    }

    private class SignUpnAsyncTask extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            boolean result = ServerAPIRequest.signUp(params[0], params[1], params[2]);
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(SignUpActivity.this, "Fail!", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(result);
        }
    }
}
