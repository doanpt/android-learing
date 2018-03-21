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
import com.cnc.hcm.cnctracking.api.MHead;
import com.cnc.hcm.cnctracking.model.GetUserProfileResponseStatus;
import com.cnc.hcm.cnctracking.model.LoginResponseStatus;
import com.cnc.hcm.cnctracking.model.UserProfile;
import com.cnc.hcm.cnctracking.util.Conts;
import com.cnc.hcm.cnctracking.util.UserInfo;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final int REQUEST_CODE_LOCATION_UPDATE = 12132;

    private EditText edtEmail;
    private EditText edtPassword;
    private TextView btnLogin;


    private APIService mService;
    private ProgressDialog mProgressDialog;
    private List<MHead> arrHead;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        checkPermistion();
        arrHead = new ArrayList<>();
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

        edtEmail = (EditText) findViewById(R.id.edt_input_email);
        edtPassword = (EditText) findViewById(R.id.edt_input_password);
        TextView tvClearId = (TextView) findViewById(R.id.tv_clear_text_id);
        TextView tvClearPass = (TextView) findViewById(R.id.tv_clear_text_pass);
        btnLogin = (TextView) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(false);
            }
        });
        tvClearId.setOnClickListener(this);
        tvClearPass.setOnClickListener(this);
    }

    private void login(boolean isLoginAuto) {
        Log.d(TAG, "Login()");

        if (!validate()) {
            Log.e(TAG, "Inputted data is invalid!");
            return;
        }

        mProgressDialog = new ProgressDialog(LoginActivity.this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage(getResources().getString(R.string.logging_please_wait));
        mProgressDialog.show();

        final String email = edtEmail.getText().toString().trim();
        final String password = edtPassword.getText().toString().trim();

        checkCredential(email, password, isLoginAuto);
    }

    private void checkCredential(final String email, String password, final boolean isLoginAuto) {
        String accessAuthen = Credentials.basic(email, password);
        arrHead.clear();
        arrHead.add(new MHead(Conts.KEY_AUTHORIZATION, accessAuthen));
        mService = ApiUtils.getAPIService(arrHead);
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


    private void onLoginSuccess(String accessToken, String message) {
//        mProgressDialog.dismiss();
        btnLogin.setEnabled(true);

        UserInfo.getInstance(LoginActivity.this).setUserInfoLogin(accessToken);
        Log.e(TAG, "onLoginSuccess, accessToken: " + accessToken);

        mProgressDialog.setMessage(getResources().getString(R.string.get_data_user_infor));
        getDataUserProfile(accessToken);
    }

    private void onLoginFailed(boolean isLoginAuto, String message) {
        mProgressDialog.dismiss();
        if (!isLoginAuto) {
            btnLogin.setEnabled(true);
            Snackbar.make(btnLogin, "Login fail: " + message, Snackbar.LENGTH_LONG).show();
        }
    }

    private boolean validate() {
        boolean valid = true;

        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("*");
            valid = false;
        } else {
            edtEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            edtPassword.setError("*");
            valid = false;
        } else {
            edtPassword.setError(null);
        }

        return valid;
    }

    private void getDataUserProfile(String accessToken) {
        arrHead.clear();
        arrHead.add(new MHead(Conts.KEY_ACCESS_TOKEN, accessToken));
        mService = ApiUtils.getAPIService(arrHead);
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
            UserInfo.getInstance(LoginActivity.this).setUserId(!TextUtils.isEmpty(userProfile.getId()) ? userProfile.getId() : Conts.UNKNOWN);
            UserInfo.getInstance(LoginActivity.this).setUserName(!TextUtils.isEmpty(userProfile.getFullname()) ? userProfile.getFullname() : Conts.UNKNOWN);
            UserInfo.getInstance(LoginActivity.this).setUserEmail(!TextUtils.isEmpty(userProfile.getEmail()) ? userProfile.getEmail() : Conts.UNKNOWN);
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
                edtEmail.setText(Conts.BLANK);
                break;
            case R.id.tv_clear_text_pass:
                edtPassword.setText(Conts.BLANK);
                break;
        }
    }
}
