package com.cnc.hcm.cnctracking.dialog;

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

import com.cnc.hcm.cnctracking.R;


/**
 * Created by giapmn on 8/22/17.
 */

public class DialogGPSSetting extends Dialog implements View.OnClickListener {

    private Context context;

    public DialogGPSSetting(@NonNull Context context) {
        super(context);
        this.context = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.dialog_gps_connection);
        setCancelable(false);
        initView();
    }

    private void initView() {
        Button btnGPS = (Button) findViewById(R.id.btn_gps_setting);
        btnGPS.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_gps_setting:
                Intent intentSettingGPS = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intentSettingGPS);
                break;
        }
    }

    @Override
    public void onBackPressed() {

    }
}
