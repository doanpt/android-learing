package com.dvt.samsung.finalapp;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuAdapter;
import android.support.v7.widget.ButtonBarLayout;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dvt.samsung.service.MusicService;
import com.dvt.samsung.utils.CommonValue;
import com.dvt.samsung.utils.MediaController;

public class MainActivity extends Activity {
    Button btnStart,btnEnd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnEnd= (Button) findViewById(R.id.btn_end);
        btnStart= (Button) findViewById(R.id.btn_start);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check contain of service
                //if contain is toast
                //else start service
                if(isMyServiceRunning(MusicService.class)){
                    Toast.makeText(MainActivity.this,"Contained",Toast.LENGTH_SHORT).show();
                }else {
                    Intent playbackServiceIntent;
                    playbackServiceIntent = new Intent(MainActivity.this, MusicService.class);
                    startService(playbackServiceIntent);
                    finish();
                }

            }
        });
        btnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //option1
                //stop service when click button end
//                stopService(new Intent(MainActivity.this,MusicService.class));
                //option2
                //send broadcast stop
                Intent i = new Intent(CommonValue.ACTION_STOP);
                sendBroadcast(i);
            }
        });
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
