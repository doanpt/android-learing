package com.google.foods.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.foods.R;
import com.google.foods.customeview.DialogNotification;
import com.google.foods.dialog.DialogNetworkConnection;
import com.google.foods.models.ItemUser;
import com.google.foods.utils.CommonValue;
import com.google.foods.utils.Keys;
import com.google.foods.utils.UserInfor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "SignupActivity";

    @Bind(R.id.input_name)
    EditText _nameText;
    @Bind(R.id.input_address)
    EditText _addressText;
    @Bind(R.id.input_mobile)
    EditText _mobileText;
    @Bind(R.id.input_password)
    EditText _passwordText;
    @Bind(R.id.input_birth)
    EditText _birthText;
    @Bind(R.id.input_reEnterPassword)
    EditText _reEnterPasswordText;
    @Bind(R.id.btn_signup)
    Button _signupButton;

    private DatePickerDialog datePicker;
    private SimpleDateFormat dateFormatter;

    private DatabaseReference mDatabase;
    private ArrayList<ItemUser> arrItemUser;

    private DialogNetworkConnection dialogNetworkConnection;
    private boolean isNetworkConnected;
    private boolean isRequest;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBroadcastReciver();
        setContentView(R.layout.activity_signup);
        initDataFirebase();
        initView();
        isRequest = checkRequestFromLogin();
    }

    private boolean checkRequestFromLogin() {
        Intent intent = getIntent();
        if (intent.getStringExtra(Keys.KEY_REQUEST_SIGNIN_ACCOUNT).equals(CommonValue.DEFAULT_VALUE_STRING_99)) {
            return true;
        }
        return false;
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
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _birthText.setInputType(InputType.TYPE_NULL);
        setDateTimeField();
    }

    private void setDateTimeField() {
        _birthText.setOnClickListener(this);
        Calendar newCalendar = Calendar.getInstance();
        datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                _birthText.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        String name = _nameText.getText().toString();
        String address = _addressText.getText().toString();
        String dateBirthday = _birthText.getText().toString();
        final String mobile = _mobileText.getText().toString();
        final String password = _passwordText.getText().toString();

        final ItemUser user = new ItemUser(name, address, dateBirthday, mobile, password, false);
        if (checkAccExisted(user)) {
            String message = String.format(getResources().getString(R.string.account_existed), mobile);
            DialogNotification notification = new DialogNotification(this);
            notification.setHiddenBtnOK();
            notification.setContentMessage(message);
            notification.show();
        } else {
            _signupButton.setEnabled(false);
            final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this, R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage(getResources().getString(R.string.creatting_account));
            progressDialog.show();

            mDatabase.child(CommonValue.DATABASE_TABLE_USER).push().setValue(user, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError == null) {
                        new android.os.Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        onSignupSuccess(user);
                                        progressDialog.dismiss();
                                    }
                                }, 2000);
                    } else {
                        onSignupFailed();
                    }
                }
            });
        }
    }


    public void onSignupSuccess(ItemUser user) {
        _signupButton.setEnabled(true);
        UserInfor.getInstance(this).setInforUserLogin(user.getUserName(), user.getAddress(), user.getPhoneNo());
        Intent intent = new Intent();
        if (isRequest) {
            intent.putExtra(Keys.KEY_USER_PASSWORD, user.getPassword());
            setResult(RESULT_OK, intent);
        } else {

            intent.putExtra(Keys.KEY_LOGIN_SUCCESS, true);
            setResult(RESULT_OK, intent);
        }
        finish();
    }

    public void onSignupFailed() {
        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        String name = _nameText.getText().toString();
        String address = _addressText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String birthday = _birthText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();
        TextInputLayout tilName = (TextInputLayout) findViewById(R.id.til_input_name);
        if (name.isEmpty() || name.length() < 3) {
            tilName.setErrorEnabled(true);
            tilName.setError(getResources().getString(R.string.error_input_user_name));
            _nameText.setFocusable(true);
            _nameText.requestFocus();
            return false;
        } else {
            tilName.setError("");
        }
        TextInputLayout tilAddress = (TextInputLayout) findViewById(R.id.til_input_address);
        if (address.isEmpty()) {
            tilAddress.setErrorEnabled(true);
            tilAddress.setError(getResources().getString(R.string.error_enter_address));
            _addressText.setFocusable(true);
            _addressText.requestFocus();
            return false;
        } else {
            tilAddress.setError(null);
        }
        TextInputLayout tilMobile = (TextInputLayout) findViewById(R.id.til_input_mobile);
        if (mobile.isEmpty() || mobile.length() < 10 || mobile.length() > 11) {
            tilMobile.setErrorEnabled(true);
            tilMobile.setError(getResources().getString(R.string.error_enter_phone_number));
            _mobileText.setFocusable(true);
            _mobileText.requestFocus();
            return false;
        } else {
            tilMobile.setError(null);
        }
        TextInputLayout tilBirthday = (TextInputLayout) findViewById(R.id.til_input_birth);
        if (birthday.isEmpty()) {
            tilBirthday.setErrorEnabled(true);
            tilBirthday.setError(getResources().getString(R.string.error_enter_date_birthday));
            _birthText.setFocusable(true);
            _birthText.requestFocus();
            return false;
        } else {
            tilBirthday.setError(null);
        }
        TextInputLayout tilPassword = (TextInputLayout) findViewById(R.id.til_input_password);
        if (password.isEmpty() || password.length() < 4 || password.length() > 11) {
            tilPassword.setErrorEnabled(true);
            tilPassword.setError(getResources().getString(R.string.error_enter_password));
            _passwordText.setFocusable(true);
            _passwordText.requestFocus();
            return false;
        } else {
            tilPassword.setError("");
        }
        TextInputLayout tilReEnterPassword = (TextInputLayout) findViewById(R.id.til_input_reEnterPassword);
        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 11 || !(reEnterPassword.equals(password))) {
            tilReEnterPassword.setErrorEnabled(true);
            tilReEnterPassword.setError(getResources().getString(R.string.error_re_enter_password));
            _reEnterPasswordText.setFocusable(true);
            _reEnterPasswordText.requestFocus();
            return false;
        } else {
            tilReEnterPassword.setError("");
        }

        return true;
    }

    private boolean checkAccExisted(ItemUser userNeedCheck) {
        for (ItemUser user : arrItemUser) {
            if (user.getPhoneNo().equals(userNeedCheck.getPhoneNo())) {
                return true;
            }
        }
        return false;
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.input_birth:
                datePicker.show();
                break;
        }
    }
}