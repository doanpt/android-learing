package com.cnc.hcm.cnctrack.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.cnc.hcm.cnctrack.R;


/**
 * Created by giapmn on 8/22/17.
 */

public class DialogNetworkSetting extends Dialog implements View.OnClickListener {

    private Button btnCancel;

    private Context context;

    public DialogNetworkSetting(@NonNull Context context) {
        super(context);
        this.context = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.dialog_network_connection);
        setCancelable(false);
        initView();
    }

    private void initView() {
        Button btnWifj = (Button) findViewById(R.id.btn_wifj_setting);
        btnWifj.setOnClickListener(this);
        Button btn3G = (Button) findViewById(R.id.btn_3g_settting);
        btn3G.setOnClickListener(this);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(this);
        hideBtnCancel();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_3g_settting:
                Intent intent3G = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
                context.startActivity(intent3G);
                break;
            case R.id.btn_wifj_setting:
                Intent intentWifi = new Intent(Settings.ACTION_WIFI_SETTINGS);
                context.startActivity(intentWifi);
                break;
            case R.id.btn_cancel:

                break;
        }
    }

    private void hideBtnCancel() {
        btnCancel.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {

    }

    public Context getContextDialog(){
        return context;
    }
}