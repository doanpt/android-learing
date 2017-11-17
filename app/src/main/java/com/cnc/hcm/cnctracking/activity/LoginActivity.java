package com.cnc.hcm.cnctracking.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.cnc.hcm.cnctracking.R;
import com.cnc.hcm.cnctracking.api.APIService;
import com.cnc.hcm.cnctracking.api.ApiUtils;
import com.cnc.hcm.cnctracking.model.GetUserProfileResponseStatus;
import com.cnc.hcm.cnctracking.model.LoginResponseStatus;
import com.cnc.hcm.cnctracking.model.UserProfile;
import com.cnc.hcm.cnctracking.util.Conts;
import com.cnc.hcm.cnctracking.util.UserInfo;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final int REQUEST_CODE_LOCATION_UPDATE = 12132;

    @Bind(R.id.input_email)
    EditText _emailText;
    @Bind(R.id.input_password)
    EditText _passwordText;
    @Bind(R.id.btn_login)
    TextView _loginButton;

    @Bind(R.id.tv_clear_text_id)
    TextView tvClearId;

    @Bind(R.id.tv_clear_text_pass)
    TextView tvClearPass;


    private APIService mService;
    private ProgressDialog mProgressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        checkPermistion();


    }

    private void checkPermistion() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                String permissions[] = new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA};
                requestPermissions(permissions, REQUEST_CODE_LOCATION_UPDATE);
                return;
            }
        } else {
            initView();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_LOCATION_UPDATE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initView();
                } else {
                    Toast.makeText(this, "Permision denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    private void initView() {
        ButterKnife.bind(this);

        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(false);
            }
        });
        tvClearId.setOnClickListener(this);
        tvClearPass.setOnClickListener(this);
    }

    public void login(boolean isLoginAuto) {
        Log.d(TAG, "Login()");

        if (!validate()) {
            Log.e(TAG, "Inputted data is invalid!");
            return;
        }

        mProgressDialog = new ProgressDialog(LoginActivity.this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage(getResources().getString(R.string.logging_please_wait));
        mProgressDialog.show();

        final String email = _emailText.getText().toString().trim();
        final String password = _passwordText.getText().toString().trim();

        checkCredential(email, password, isLoginAuto);
    }

    private void checkCredential(final String email, String password, final boolean isLoginAuto) {
        mService = ApiUtils.getAPIService(email, password);
        mService.login().enqueue(new Callback<LoginResponseStatus>() {
            @Override
            public void onResponse(Call<LoginResponseStatus> call, Response<LoginResponseStatus> response) {
                int statusCode = response.code();
                Log.d(TAG, "login.onResponse(), statusCode: " + statusCode);
                if (response.isSuccessful()) {
                    LoginResponseStatus loginResponseStatus = response.body();
                    if (loginResponseStatus != null) {
                        int loginStatusCode = loginResponseStatus.getStatusCode();
                        if (loginStatusCode == Conts.RESPONSE_STATUS_OK) {
                            Log.d(TAG, "login.onResponse().isSuccessful(), loginStatusCode: 200");
                            String accessToken = (String) loginResponseStatus.getResult().get(LoginResponseStatus.LOGIN_RESPONSE_STATUS_KEY_ACCESS_TOKEN);
                            onLoginSuccess(accessToken, loginResponseStatus.getMessage());
                        } else {
                            String message = (String) loginResponseStatus.getResult().get(LoginResponseStatus.LOGIN_RESPONSE_STATUS_KEY_MESSAGE);
                            Log.d(TAG, "login.onResponse().isSuccessful(), loginStatusCode: " + loginStatusCode + ", message: " + message);
                            onLoginFailed(isLoginAuto, message);
                        }
                    } else {
                        Log.d(TAG, "login.onResponse().isSuccessful(), loginResponseStatus = null");
                        onLoginFailed(isLoginAuto, response.message());
                    }
                } else {
                    Log.e(TAG, "login.onResponse().isNOTSuccessful(), statusCode: " + statusCode);
                    onLoginFailed(isLoginAuto, response.message());
                }
            }

            @Override
            public void onFailure(Call<LoginResponseStatus> call, Throwable t) {
                Log.e(TAG, "login.onFailure()", t);
                onLoginFailed(isLoginAuto, t.getMessage());
            }
        });

    }


    public void onLoginSuccess(String accessToken, String message) {
//        mProgressDialog.dismiss();
        _loginButton.setEnabled(true);

        UserInfo.getInstance(LoginActivity.this).setUserInfoLogin(accessToken);
        Log.e(TAG, "onLoginSuccess, accessToken: " + accessToken);

        mProgressDialog.setMessage(getResources().getString(R.string.get_data_user_infor));
        getDataUserProfile(accessToken);

    }

    public void onLoginFailed(boolean isLoginAuto, String message) {
        mProgressDialog.dismiss();
        if (!isLoginAuto) {
            _loginButton.setEnabled(true);
            Snackbar.make(_loginButton, "Login fail: " + message, Snackbar.LENGTH_LONG).show();
        }
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("*");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("*");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    private void getDataUserProfile(String accessToken) {

        mService = ApiUtils.getAPIService(accessToken);
        mService.getUserProfile().enqueue(new Callback<GetUserProfileResponseStatus>() {
            @Override
            public void onResponse(Call<GetUserProfileResponseStatus> call, Response<GetUserProfileResponseStatus> response) {
                int statusCode = response.code();
                Log.d(TAG, "getDataUserProfile.onResponse(), statusCode: " + statusCode);
                if (response.isSuccessful()) {
                    GetUserProfileResponseStatus userProfileResponseStatus = response.body();
                    if (userProfileResponseStatus != null) {
                        Log.d(TAG, "getDataUserProfile.onResponse() Successfuly! Email: " + userProfileResponseStatus.getResult().getEmail());
                        onGetUserProfileSuccessfully(userProfileResponseStatus);
                    }
                } else {
                    String rawResponse = response.raw().toString();
                    Log.e(TAG, "getDataUserProfile.onResponse() NOT Successfully, rawResponse: " + rawResponse);
                    onGetUserProfileFailed(rawResponse);
                }
            }

            @Override
            public void onFailure(Call<GetUserProfileResponseStatus> call, Throwable t) {
                Log.e(TAG, "getDataUserProfile.onFailure()", t);
                onGetUserProfileFailed(t.toString());
            }

        });
    }

    private void onGetUserProfileFailed(String rawResponse) {
        Log.d(TAG, "onGetUserProfileFailed, get UserProfile is fail");
        mProgressDialog.dismiss();
        Toast.makeText(LoginActivity.this, getResources().getString(R.string.cannot_get_image_user_profile), Toast.LENGTH_SHORT).show();
    }

    private void onGetUserProfileSuccessfully(GetUserProfileResponseStatus userProfileResponseStatus) {
        mProgressDialog.dismiss();
        UserProfile userProfile = userProfileResponseStatus.getResult();
        if (userProfile != null) {
            UserInfo.getInstance(LoginActivity.this).setUserEmail(!TextUtils.isEmpty(userProfile.getEmail()) ? userProfile.getEmail() : Conts.UNKNOWN);
            UserInfo.getInstance(LoginActivity.this).setUserName(!TextUtils.isEmpty(userProfile.getFullname()) ? userProfile.getFullname() : Conts.UNKNOWN);
            UserInfo.getInstance(LoginActivity.this).setUserPhone(!TextUtils.isEmpty(userProfile.getPhone()) ? userProfile.getPhone() : Conts.UNKNOWN);
            UserInfo.getInstance(LoginActivity.this).setUserUrlImage(!TextUtils.isEmpty(userProfile.getPhoto()) ? userProfile.getPhoto() : Conts.UNKNOWN);

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

        } else {
            Toast.makeText(LoginActivity.this, getResources().getString(R.string.cannot_get_image_user_profile), Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_clear_text_id:
                _emailText.setText(Conts.BLANK);
                break;
            case R.id.tv_clear_text_pass:
                _passwordText.setText(Conts.BLANK);
                break;
        }
    }
}
