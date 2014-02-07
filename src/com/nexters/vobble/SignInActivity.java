package com.nexters.vobble;

import org.json.JSONObject;

import android.app.*;
import android.content.*;
import android.content.DialogInterface.OnCancelListener;
import android.os.*;
import android.preference.PreferenceManager;
import android.view.*;
import android.widget.*;
<<<<<<< HEAD

import com.nexters.vobble.core.*;
=======
>>>>>>> 5252e6c038186bcce0a58ce98dee61a6794f2b24

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

<<<<<<< HEAD
    private class SignInAsyncTask extends AsyncTask<String, Integer, Integer> {
    	
    	private ProgressDialog dialog;
    	
    	@Override
    	protected void onPreExecute() {
    		super.onPreExecute();
    		dialog = new ProgressDialog(SignInActivity.this);
    		dialog.setCancelable(true);
    		dialog.setMessage("Loading....");
    		dialog.setCanceledOnTouchOutside(false);
    		dialog.setOnCancelListener(new OnCancelListener() {
				
				@Override
				public void onCancel(DialogInterface dialog) {
					SignInAsyncTask.this.cancel(true);
				}
			});
    		dialog.show();
    	}
    	
        @Override
        protected Integer doInBackground(String... params) {
            int userId = ServerAPIRequest.signIn(params[0], params[1]);
            return userId;
=======
    public void onClick(View v) {
        if (isAllFormsFilled()) {
        	/*
            signInAsyncTask = new SignInAsyncTask();
            signInAsyncTask.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,
                    etEmail.getText().toString(), etPassword.getText().toString());
                    */
        	String url = URL.SIGN_IN;
    		
    		RequestParams params = new RequestParams();
    		params.put(Vobble.EMAIL, etEmail.getText().toString());
    		params.put(Vobble.PASSWORD, etPassword.getText().toString());
    		
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
        } else {
            Toast.makeText(SignInActivity.this, "Fill in All forms", Toast.LENGTH_SHORT).show();
>>>>>>> 5252e6c038186bcce0a58ce98dee61a6794f2b24
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
                        .edit().putString(Vobble.TOKEN, response.optString("token")).commit();

<<<<<<< HEAD
        @Override
        protected void onPostExecute(Integer userId) {
        	dialog.dismiss();
            if (userId <= 0) {
                Toast.makeText(SignInActivity.this, "Failed! Retry!", Toast.LENGTH_SHORT).show();
            } else {
                // TODO: Preference에 userId 저장
=======
>>>>>>> 5252e6c038186bcce0a58ce98dee61a6794f2b24
                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
<<<<<<< HEAD
            
            super.onPostExecute(userId);
        }
=======
        });
>>>>>>> 5252e6c038186bcce0a58ce98dee61a6794f2b24
    }
}
