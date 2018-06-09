package com.cnc.hcm.cnctrack.base;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.cnc.hcm.cnctrack.R;
import com.cnc.hcm.cnctrack.activity.LoginActivity;
import com.cnc.hcm.cnctrack.activity.MainActivity;
import com.cnc.hcm.cnctrack.dialog.DialogNetworkSetting;
import com.cnc.hcm.cnctrack.dialog.DialogNotification;
import com.cnc.hcm.cnctrack.service.GPSService;
import com.cnc.hcm.cnctrack.util.Conts;
import com.cnc.hcm.cnctrack.util.UserInfo;

public abstract class BaseActivity extends AppCompatActivity {


    private ProgressDialog mProgressDialog;
    private DialogNetworkSetting dialogNetworkSetting;

    private boolean isActivityPause = true;
    private GPSService gpsService;


    public abstract void onViewReady(@Nullable Bundle savedInstanceState);

    public abstract int getLayoutId();

    public interface OnNetworkConnectedListener {
        void onNetworkConnected();

        void onNetworkDisconnect();
    }

    public interface OnTokenExpired {
        void onTokenExpired();
    }

    public void setOnTokenExpired(OnTokenExpired onTokenExpired) {
        onTokenExpired.onTokenExpired();
    }

    private OnNetworkConnectedListener onNetworkConnectedListener;

    public void setOnNetworkConnectedListener(OnNetworkConnectedListener onNetworkConnectedListener) {
        this.onNetworkConnectedListener = onNetworkConnectedListener;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        onViewReady(savedInstanceState);
        initObject();
        registerBroadcastReciver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isActivityPause = true;
        onNetworkChange(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        isActivityPause = false;
        if(mProgressDialog!=null && mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }
        mProgressDialog = null;
    }


    private void initObject() {
        dialogNetworkSetting = new DialogNetworkSetting(this);
    }


    public void showMessageRequestLogout() {
        final DialogNotification dialog = new DialogNotification(this);
        dialog.setHiddenBtnExit();
        dialog.setContentMessage(getString(R.string.account_login_other_device));
        dialog.setCancelable(false);
        dialog.setOnClickDialogNotificationListener(new DialogNotification.OnClickDialogNotificationListener() {
            @Override
            public void onClickButtonOK() {
                actionLogout();
            }

            @Override
            public void onClickButtonExit() {

            }
        });
        dialog.show();
    }

    public void actionLogout() {
        final ProgressDialog progressDialog = new ProgressDialog(BaseActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getResources().getString(R.string.text_logout));
        progressDialog.show();

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                UserInfo.getInstance(BaseActivity.this).setUserInfoLogout();
                UserInfo.getInstance(BaseActivity.this).setUploadFirstTime(true);
                if (gpsService != null) {
                    gpsService.disconnectService();
                }
                progressDialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }, 1000);
    }

    public void showProgressLoadding() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage(getResources().getString(R.string.loadding));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.show();
    }

    public void updateMessageProgressDialog(String message) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.setMessage(message);
        }
    }

    public void dismisProgressLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }


    private void showDialogNetworkSetting() {
        if (dialogNetworkSetting != null && !dialogNetworkSetting.isShowing() && this.isActivityPause) {
            dialogNetworkSetting.show();
        }
    }

    private void dismisDialogNetworkSetting() {
        if (dialogNetworkSetting != null && dialogNetworkSetting.isShowing() && this.isActivityPause) {
            dialogNetworkSetting.dismiss();
        }
    }

    private void registerBroadcastReciver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Conts.ACTION_NETWORK_CHANGE);
        registerReceiver(receiver, filter);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null) {
                switch (action) {
                    case Conts.ACTION_NETWORK_CHANGE:
                        onNetworkChange(context);
                        break;
                }
            }
        }
    };

    private void onNetworkChange(Context context) {
        if (checkNetwork(context)) {
            dismisDialogNetworkSetting();
            if (onNetworkConnectedListener != null) {
                onNetworkConnectedListener.onNetworkConnected();
            }
        } else {
            showDialogNetworkSetting();
            if (onNetworkConnectedListener != null) {
                onNetworkConnectedListener.onNetworkDisconnect();
            }
        }
    }


    private boolean checkNetwork(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfor = connectivityManager.getActiveNetworkInfo();
        return activeNetInfor != null && activeNetInfor.isConnectedOrConnecting();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(mProgressDialog!=null && mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }
    }

    public void setGpsService(GPSService gpsService) {
        this.gpsService = gpsService;
    }
}
