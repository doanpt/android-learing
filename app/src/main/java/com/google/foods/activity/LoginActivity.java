package com.google.foods.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.foods.R;
import com.google.foods.dialog.DialogNetworkConnection;
import com.google.foods.models.ItemUser;
import com.google.foods.utils.CommonValue;
import com.google.foods.utils.Keys;
import com.google.foods.utils.UserInfor;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGN_UP = 222;

    @Bind(R.id.input_phone_number)
    EditText _phoneNumberText;
    @Bind(R.id.input_password)
    EditText _passwordText;
    @Bind(R.id.btn_login)
    Button _loginButton;
    @Bind(R.id.link_signup)
    TextView _signupLink;

    private DatabaseReference mDatabase;
    private ArrayList<ItemUser> arrItemUser;
    private boolean isNetworkConnected;
    private DialogNetworkConnection dialogNetworkConnection;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBroadcastReciver();
        setContentView(R.layout.activity_login);
        initDataFirebase();
        initView();
    }

    private void registerBroadcastReciver() {
        dialogNetworkConnection = new DialogNetworkConnection(this);
        dialogNetworkConnection.setCancelable(false);

        IntentFilter filter = new IntentFilter();
        filter.addAction(CommonValue.ACTION_NETWORK_CHANGE);
        registerReceiver(receiver, filter);
    }


    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case CommonValue.ACTION_NETWORK_CHANGE:
                    ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo activeNetInfor = connectivityManager.getActiveNetworkInfo();
                    isNetworkConnected = activeNetInfor != null && activeNetInfor.isConnectedOrConnecting();
                    if (isNetworkConnected) {
                        dialogNetworkConnection.dismiss();
                    } else {
                        dialogNetworkConnection.show();
                    }
                    break;
            }

        }
    };

    private void initDataFirebase() {
        arrItemUser = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child(CommonValue.DATABASE_TABLE_USER).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrItemUser.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    ItemUser user = postSnapshot.getValue(ItemUser.class);
                    arrItemUser.add(user);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void initView() {
        ButterKnife.bind(this);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                intent.putExtra(Keys.KEY_REQUEST_SIGNIN_ACCOUNT, CommonValue.DEFAULT_VALUE_STRING_99);
                startActivityForResult(intent, REQUEST_SIGN_UP);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_SIGN_UP:
                if (resultCode == RESULT_OK) {
                    String phoneNo = UserInfor.getInstance(this).getPhoneNumber();
                    String password = data.getStringExtra(Keys.KEY_USER_PASSWORD);
                    _phoneNumberText.setText(phoneNo);
                    _passwordText.setText(password);
                }
                break;
        }
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            return;
        }

        final String phoneNuber = _phoneNumberText.getText().toString().trim();
        final String password = _passwordText.getText().toString().trim();

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getResources().getString(R.string.check_login));
        progressDialog.show();

        boolean isUserExisted = false;
        for (final ItemUser user : arrItemUser) {

            if (user.getPhoneNo().trim().equals(phoneNuber)) {
                isUserExisted = true;
                if (user.getPassword().trim().equals(password)) {

                    _loginButton.setEnabled(false);
                    new android.os.Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (user.isisAdmin()) {
                                onLoginSuccess(CommonValue.MODE_ADMIN, user);
                                progressDialog.dismiss();
                            } else {
                                onLoginSuccess(CommonValue.MODE_USER, user);
                                progressDialog.dismiss();
                            }
                        }
                    }, 1500);
                    break;
                } else {
                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    Snackbar.make(_phoneNumberText, getResources().getString(R.string.password_wrong), Snackbar.LENGTH_LONG).show();
                                    progressDialog.dismiss();
                                }
                            }, 1000);
                    break;
                }
            } else {
                continue;
            }
        }

        if (!isUserExisted) {
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            Snackbar.make(_phoneNumberText, getResources().getString(R.string.phone_number_wrong), Snackbar.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    }, 1000);
        }
    }


    public void onLoginSuccess(int mode, ItemUser user) {
        if (mode == CommonValue.MODE_ADMIN) {
            Intent intent = new Intent(getApplicationContext(), OrderFoodManagerActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

            UserInfor.getInstance(this).setInforAdminLogin(user.getUserName());
        } else if (mode == CommonValue.MODE_USER) {
            UserInfor.getInstance(this).setInforUserLogin(user.getUserName(), user.getAddress(), user.getPhoneNo());
            Intent intent = new Intent();
            intent.putExtra(Keys.KEY_LOGIN_SUCCESS, true);
            setResult(RESULT_OK, intent);
            finish();
        }
        _loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String phoneNumber = _phoneNumberText.getText().toString();
        String password = _passwordText.getText().toString();

        if (phoneNumber.isEmpty() || !Patterns.PHONE.matcher(phoneNumber).matches()) {
            _phoneNumberText.setError(getResources().getString(R.string.error_enter_phone_number));
            valid = false;
        } else {
            _phoneNumberText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError(getResources().getString(R.string.error_enter_password));
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
