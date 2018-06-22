package com.cnc.hcm.cnctrack.receiver;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.cnc.hcm.cnctrack.service.GPSService;
import com.cnc.hcm.cnctrack.util.CommonMethod;
import com.cnc.hcm.cnctrack.util.Conts;
import com.cnc.hcm.cnctrack.util.SettingApp;
import com.cnc.hcm.cnctrack.util.UserInfo;


/**
 * Created by giapmn on 9/22/17.
 */

public class BroadcastRecever extends BroadcastReceiver {

    private static final String TAGG = "BroadcastRecever";

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isUserLogin = UserInfo.getInstance(context).getIsLogin();
        if (isUserLogin) {
            if (!isServiceRunning(context, GPSService.class) && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                Toast.makeText(context, "CNCTracking start", Toast.LENGTH_SHORT).show();
                Intent intentService = new Intent(context, GPSService.class);
                intentService.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
                context.startService(intentService);

                addNotification(context);
            }
        }
        Log.d(TAGG, "BroadcastRecever: " + intent.getAction());
    }

    private static boolean isServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void addNotification(Context context) {
        SettingApp.getInstance(context).setCountRestart(SettingApp.getInstance(context).getCountRestart() + 1);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Conts.SECONDARY_CHANNEL)
                .setSmallIcon(CommonMethod.getSmallIcon())
                .setContentTitle("System restart")
                .setContentText("CNC khởi động lại lần thứ: " + SettingApp.getInstance(context).getCountRestart())
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        // Add as notification
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, builder.build());
    }

}
