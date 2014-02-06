package com.nexters.vobble;

import org.json.JSONObject;

import android.content.Intent;
import android.os.*;
import android.view.*;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.android.http.RequestParams;

import com.nexters.vobble.core.*;
import com.nexters.vobble.network.*;

public class SignUpActivity extends BaseActivity implements View.OnClickListener {

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
                alert("모든 항목을 입력해 주세요.");
            } else if (!isPasswordCheckCorrected()) {
                alert("비밀번호가 다릅니다. 다시 확인해 주세요.");
            } else if (!isValidUsername()) {
                alert("Username 형식이 잘못되었습니다. 4글자 이상의 영어, 숫자, 하이픈(-), 언더바(_) 조합이어야 합니다.");
            } else if (!isValidEmail()) {
                alert("Email 형식이 잘못되었습니다. 다시 확인해주세요.");
            } else if (!isValidPassword()) {
                alert("비밀번호는 6자리 이상이어야 합니다.");
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
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
