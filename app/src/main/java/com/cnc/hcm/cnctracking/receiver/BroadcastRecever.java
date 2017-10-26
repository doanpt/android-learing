package com.cnc.hcm.cnctracking.receiver;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.cnc.hcm.cnctracking.service.GPSService;
import com.cnc.hcm.cnctracking.util.UserInfo;


/**
 * Created by giapmn on 9/22/17.
 */

public class BroadcastRecever extends BroadcastReceiver {

    private static final String TAGG = "BroadcastRecever";

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isUserLogin = UserInfo.getInstance(context).getIsLogin();
        if (isUserLogin) {
            if (!isServiceRunning(context, GPSService.class)) {
                Toast.makeText(context, "CNCTracking start", Toast.LENGTH_SHORT).show();
                Intent intentService = new Intent(context, GPSService.class);
                intentService.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
                context.startService(intentService);
            }
        }
        Log.d(TAGG, "BroadcastRecever: " + intent.getAction());
    }

    public static boolean isServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
