package com.dvt.lockscreen.activity.dialog;

import android.content.Context;
import android.content.Intent;

import com.dvt.lockscreen.activity.callbackif.OnNegativeClick;
import com.dvt.lockscreen.activity.callbackif.OnPositiveClick;
import com.dvt.lockscreen.activity.callbackif.OnRequestDrawOverlaysListener;


/**
 * Created by sev_user on 9/7/2017.
 */

public class PermissionUtil {
    public static void requestDrawOverlays(final Context context, final OnRequestDrawOverlaysListener listener) {
        PermissionListener.onRequestDrawOverlaysListener = listener;
        DrawOverlaysDialog drawOverlaysDialog = new DrawOverlaysDialog(context);
        drawOverlaysDialog.setOnPositiveClick(new OnPositiveClick() {
            @Override
            public void onClick() {
                Intent intent = new Intent(context, PermissionActivity.class);
                intent.putExtra(Command.EXTRA_COMMAND, Command.COMMAND_REQUEST_DRAW_OVERLAYS);
                context.startActivity(intent);
            }
        });
        drawOverlaysDialog.setOnNegativeClick(new OnNegativeClick() {
            @Override
                public void onClick() {
                listener.onDone(false);
            }
        });
        drawOverlaysDialog.show();
    }
}
