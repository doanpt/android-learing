package com.dvt.lockscreen.activity.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import com.dvt.lockscreen.activity.R;
import com.dvt.lockscreen.activity.callbackif.OnNegativeClick;
import com.dvt.lockscreen.activity.callbackif.OnPositiveClick;


/**
 * Created by sev_user on 9/7/2017.
 */

public class DrawOverlaysDialog extends AlertDialog.Builder {
    private OnPositiveClick onPositiveClick;
    private OnNegativeClick onNegativeClick;

    public DrawOverlaysDialog(@NonNull Context context) {
        super(context);
        setTitle(R.string.notify).setIcon(R.mipmap.ic_launcher).setMessage(R.string.request_draw_overlay_dialog).setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (onPositiveClick != null) {
                    onPositiveClick.onClick();
                }
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (onNegativeClick != null) {
                    onNegativeClick.onClick();
                }
            }
        }).setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                if (onNegativeClick != null) {
                    onNegativeClick.onClick();
                }
            }
        }).create();
    }

    public void setOnPositiveClick(OnPositiveClick onPositiveClick) {
        this.onPositiveClick = onPositiveClick;
    }

    public void setOnNegativeClick(OnNegativeClick onNegativeClick) {
        this.onNegativeClick = onNegativeClick;
    }
}
