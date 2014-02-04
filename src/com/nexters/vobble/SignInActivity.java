package com.nexters.vobble;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import com.nexters.vobble.core.ServerAPIRequest;

public class SignInActivity extends Activity {
    private EditText etEmail;
    private EditText etPassword;
    private Button btnSignIn;

    private SignInAsyncTask signInAsyncTask;

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
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            private boolean isAllFormsFilled() {
                return !etEmail.getText().toString().equals("") && !etPassword.getText().toString().equals("");
            }

            public void onClick(View v) {
                if (isAllFormsFilled()) {
                    signInAsyncTask = new SignInAsyncTask();
                    signInAsyncTask.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,
                            etEmail.getText().toString(), etPassword.getText().toString());
                } else {
                    Toast.makeText(SignInActivity.this, "Fill in All forms", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private class SignInAsyncTask extends AsyncTask<String, Integer, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            int userId = ServerAPIRequest.signIn(params[0], params[1]);
            return userId;
        }

        @Override
        protected void onPostExecute(Integer userId) {
            if (userId <= 0) {
                Toast.makeText(SignInActivity.this, "Failed! Retry!", Toast.LENGTH_SHORT).show();
            } else {
                // TODO: Preference에 userId 저장
                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
            super.onPostExecute(userId);
        }
    }
}
