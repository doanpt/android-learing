package com.cnc.hcm.cnctracking.receiver;

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
        String action = intent.getAction();
        boolean isUserLogin = UserInfo.getInstance(context).getIsLogin();
        switch (action) {
            case Intent.ACTION_BOOT_COMPLETED:
                if (isUserLogin) {
                    Toast.makeText(context, "CNCTracking start", Toast.LENGTH_SHORT).show();
                    Intent intentService = new Intent(context, GPSService.class);
                    intentService.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
                    context.startService(intentService);
                }
                Log.d(TAGG, "BroadcastRecever, ACTION_BOOT_COMPLETED");
                break;
        }
    }


}
