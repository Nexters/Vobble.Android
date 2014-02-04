package com.nexters.vobble;

import android.app.*;
import android.content.Intent;
import android.os.*;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.nexters.vobble.core.ServerAPIRequest;

public class SignUpActivity extends Activity {

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

            private boolean isPasswordCheckCorrected() {
                return etPassword.getText().toString().equals(etPasswordCheck.getText().toString());
            }

            @Override
            public void onClick(View view) {
                if (!isAllFormFilled()) {
                    Toast.makeText(SignUpActivity.this, "Fill in all forms!", Toast.LENGTH_SHORT).show();
                } else if (!isPasswordCheckCorrected()) {
                    Toast.makeText(SignUpActivity.this, "Incorrect password!", Toast.LENGTH_SHORT).show();
                } else {
                    signUpAsyncTask = new SignUpnAsyncTask();
                    signUpAsyncTask.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, etUsername.getText().toString(),
                            etEmail.getText().toString(), etPassword.getText().toString());
                }
            }
        });
    }

    private class SignUpnAsyncTask extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            boolean result = ServerAPIRequest.signUpUser(params[0], params[1], params[2]);
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
