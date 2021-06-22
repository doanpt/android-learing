package com.dvt.lockscreen.activity;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dvt.lockscreen.activity.callbackif.OnRequestDrawOverlaysListener;
import com.dvt.lockscreen.activity.dialog.PermissionUtil;
import com.dvt.lockscreen.activity.service.LockService;

public class MainActivity extends AppCompatActivity implements OnRequestDrawOverlaysListener {
//    https://techblog.vn/index.php/android-tao-mot-app-lockscreen
//    https://techblog.vn/window-manager-trong-android
//    https://viblo.asia/p/window-manager-trong-android-MdZkAQmMkox
//    https://developer.android.com/reference/android/view/WindowManager.LayoutParams

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                PermissionUtil.requestDrawOverlays(this, this);
            } else onDone(true);
        } else onDone(true);

    }

    @Override
    public void onDone(boolean result) {
            startService(new Intent(this, LockService.class));
    }
}
