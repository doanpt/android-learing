package com.dvt.samsung.finalapp;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import service.demo.samsung.dvt.com.day6_service.MyService.*;

public class MainActivityService extends AppCompatActivity {
    private MyService myService;
    private Button btnA, btnB, btnC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnA = (Button) findViewById(R.id.button);
        btnB = (Button) findViewById(R.id.button2);
        btnC = (Button) findViewById(R.id.button3);
        if(isMyServiceRunning(MyService.class)){
            //toat da ton tai
        }else {
            bindService(new Intent(MainActivity.this, MyService.class), serviceConnection, BIND_AUTO_CREATE);
        }
        btnA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myService.playMedia();
            }
        });
        btnB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myService.pause();
            }
        });
        btnC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myService.forward();
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

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyBinder binder = (MyBinder) service;
            myService = binder.getMyService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            myService = null;
        }
    };
}
