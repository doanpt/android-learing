package com.dvt.lockscreen.activity.dialog;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.dvt.lockscreen.activity.callbackif.OnRequestDrawOverlaysListener;


/**
 * Created by sev_user on 9/7/2017.
 */

public class PermissionActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_DRAW_OVERLAYS = 0;
    private int mode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mode = getIntent().getIntExtra(Command.EXTRA_COMMAND, -1);
        switch (mode) {
            case Command.COMMAND_REQUEST_DRAW_OVERLAYS:
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, REQUEST_CODE_DRAW_OVERLAYS);
                break;
        }
    }


}
