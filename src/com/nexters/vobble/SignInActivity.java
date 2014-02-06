package com.nexters.vobble;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import com.nexters.vobble.common.VobblePreferencesManager;
import com.nexters.vobble.core.ServerAPIRequest;

public class SignInActivity extends Activity implements View.OnClickListener {
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

    private String getPassword() {
        return etPassword.getText().toString();
    }

    private String getEmail() {
        return etEmail.getText().toString();
    }

    private boolean isAllFormsFilled() {
        return !getEmail().equals("") && !getPassword().equals("");
    }

    private void showAlertDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg);
        builder.setNegativeButton("확인", null);
        builder.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        case R.id.btn_sign_in:
            if (isAllFormsFilled()) {
                SignInAsyncTask signInAsyncTask = new SignInAsyncTask();
                signInAsyncTask.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, getEmail(), getPassword());
            } else {
                showAlertDialog("이메일과 비밀번호를 올바르게 입력해주세요.");
            }
            break;
        }
    }

    private void saveUserId(int userId) {
        VobblePreferencesManager pm = new VobblePreferencesManager(this);
        pm.setUserId(userId);
    }

    private class SignInAsyncTask extends AsyncTask<String, Integer, Integer> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(SignInActivity.this, "로그인", "로그인 중입니다. 잠시 기다려주세요.");
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... params) {
            int userId = ServerAPIRequest.signIn(params[0], params[1]);
            return userId;
        }

        @Override
        protected void onPostExecute(Integer userId) {
            if (userId <= 0) {
                // TODO: 실패한 이유를 사용자에게 알려주어야 함.
                showAlertDialog("로그인에 실패하였습니다. 다시 시도해 주세요.");
            } else {
                saveUserId(userId);
                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
            progressDialog.dismiss();
            super.onPostExecute(userId);
        }
    }
}
