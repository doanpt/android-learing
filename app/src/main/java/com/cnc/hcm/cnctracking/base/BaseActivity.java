package com.cnc.hcm.cnctracking.base;

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

import com.cnc.hcm.cnctracking.R;
import com.cnc.hcm.cnctracking.dialog.DialogNetworkSetting;
import com.cnc.hcm.cnctracking.util.Conts;

public abstract class BaseActivity extends AppCompatActivity {


    private ProgressDialog mProgressDialog;
    private DialogNetworkSetting dialogNetworkSetting;

    private boolean isActivityPause = true;


    public abstract void onViewReady(@Nullable Bundle savedInstanceState);

    public abstract int getLayoutId();

    public interface OnNetworkConnectedListener {
        void onNetworkConnected();

        void onNetworkDisconnect();
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
        mProgressDialog = null;
    }


    private void initObject() {
        dialogNetworkSetting = new DialogNetworkSetting(this);
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
}
