package com.nexters.vobble;

import android.app.*;
import android.content.Intent;
import android.os.*;
import android.view.*;
import android.widget.Button;
import android.widget.EditText;
import com.nexters.vobble.core.ServerAPIRequest;

public class SignUpActivity extends Activity implements View.OnClickListener {

    private EditText etUsername;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etPasswordCheck;
    private Button btnSignUp;

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
        btnSignUp.setOnClickListener(this);
    }

    private String getUsername() {
        return etUsername.getText().toString();
    }

    private String getEmail() {
        return etEmail.getText().toString();
    }

    private String getPassword() {
        return etPassword.getText().toString();
    }

    private String getPasswordCheck() {
        return etPasswordCheck.getText().toString();
    }

    private boolean isAllFormFilled() {
        return !(getUsername().equals("") || getEmail().equals("") ||
                getPassword().equals("") || getPasswordCheck().equals(""));
    }

    private boolean isPasswordCheckCorrected() {
        return getPassword().equals(getPasswordCheck());
    }

    // TODO: username 형식 확인
    private boolean isUsernameFormatCorrected() {
        return true;
    }

    // TODO: email 형식 확인
    private boolean isEmailFormatCorrected() {
        return true;
    }

    // TODO: password 형식 확인
    private boolean isPasswordFormatCorrected() {
        if (getPassword().length() >= 6) {
            return true;
        } else {
            return false;
        }
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
        case R.id.btn_sign_up:
            if (!isAllFormFilled()) {
                showAlertDialog("모든 항목을 입력해 주세요.");
            } else if (!isPasswordCheckCorrected()) {
                showAlertDialog("비밀번호가 다릅니다. 다시 확인해 주세요.");
            } else if (!isUsernameFormatCorrected()) {
                showAlertDialog("Username 형식이 잘못되었습니다. 4글자 이상의 영어, 숫자, 하이픈(-), 언더바(_) 조합이어야 합니다.");
            } else if (!isEmailFormatCorrected()) {
                showAlertDialog("Email 형식이 잘못되었습니다. 다시 확인해주세요.");
            } else if (!isPasswordFormatCorrected()) {
                showAlertDialog("비밀번호는 6자리 이상이어야 합니다.");
            } else {
                SignUpAsyncTask signUpAsyncTask = new SignUpAsyncTask();
                signUpAsyncTask.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, getUsername(), getEmail(), getPassword());
            }
            break;
        }
    }

    private class SignUpAsyncTask extends AsyncTask<String, Integer, Boolean> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(SignUpActivity.this, "회원가입", "회원가입 중입니다. 잠시 기다려주세요.");
            super.onPreExecute();
        }

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
                // TODO: 회원가입 실패한 이유가 무엇인지 사용자에게 알려주어야 함.
                showAlertDialog("회원가입에 실패하였습니다. 다시 시도해 주세요.");
            }
            progressDialog.dismiss();
            super.onPostExecute(result);
        }
    }
}
