package com.t3h.demoabc;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_SET_DEFAULT_DIALER = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkDefaultDialer();
    }

    private void checkDefaultDialer() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return;
        TelecomManager telecomManager = (TelecomManager) getSystemService(TELECOM_SERVICE);
        if (getPackageName().equals(telecomManager.getDefaultDialerPackage()))
            return;
        Intent intent = new Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER);
        intent.putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, getPackageName());
        startActivityForResult(intent, REQUEST_CODE_SET_DEFAULT_DIALER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SET_DEFAULT_DIALER) {
            checkSetDefaultDialerResult(resultCode);
        }
    }

    private void checkSetDefaultDialerResult(int resultCode) {
        String message = "";
        switch (resultCode) {
            case RESULT_OK:
                message = "User accepted request to become default dialer";
                break;
            case RESULT_CANCELED:
                message = "User declined request to become default dialer";
                break;
            default:
                message = "Unexpected result code "+resultCode;
                break;
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
