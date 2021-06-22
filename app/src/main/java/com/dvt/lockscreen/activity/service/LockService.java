package com.dvt.lockscreen.activity.service;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.dvt.lockscreen.activity.customview.LockView;

/**
 * Created by sev_user on 10/10/2017.
 */

public class LockService extends Service {
    private LockView lockView;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        lockView = new LockView(this);
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_BOOT_COMPLETED);
        //Set up a receiver to listen for the Intents in this Service
        registerReceiver(mReceiverSystem, filter);
        super.onCreate();
    }

    private BroadcastReceiver mReceiverSystem = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_SCREEN_OFF) || action.contentEquals(Intent.ACTION_BOOT_COMPLETED)) {
                lockView.onLockScreen();
            }
        }
    };

}
