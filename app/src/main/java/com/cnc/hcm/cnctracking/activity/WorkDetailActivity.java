package com.cnc.hcm.cnctracking.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cnc.hcm.cnctracking.R;
import com.cnc.hcm.cnctracking.service.GPSService;
import com.cnc.hcm.cnctracking.util.CommonMethod;
import com.cnc.hcm.cnctracking.util.Conts;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class WorkDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAGG = WorkDetailActivity.class.getSimpleName();
    private LinearLayout llViewControl;
    private FrameLayout flBlurView;
    private FloatingActionsMenu fabMenu;
    private TextView tvComplete;
    private TextView tvCancel;
    private FloatingActionButton fabCall, fabFindWay, fabScanQR, fabAddProduct;

    //TOTO
    private GPSService gpsService;

    private double latitude;
    private double longitude;
    private boolean isNetworkConnected;


    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            Log.e(TAGG, "serviceConnection at WorkDetailActivity is Connected");
            gpsService = ((GPSService.MyBinder) iBinder).getGPSService();
            if (gpsService != null) {
                gpsService.setWorkDetailActivity(WorkDetailActivity.this);
            } else {
                Log.e(TAGG, "serviceConnection at WorkDetailActivity is null");
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e(TAGG, "serviceConnection at WorkDetailActivity is Disconnected");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBroadcastReciver();
        setContentView(R.layout.activity_work_detail);
        bindService(new Intent(this, GPSService.class), serviceConnection, Context.BIND_AUTO_CREATE);
        getIdTask();
        initViews();

    }

    private void getIdTask() {
        String idTask = getIntent().getStringExtra(Conts.KEY_ID_TASK);
        CommonMethod.makeToast(this, "ID: " + idTask);
    }


    private void initViews() {
        llViewControl = (LinearLayout) findViewById(R.id.view_control);
        flBlurView = (FrameLayout) findViewById(R.id.blurView);
        fabMenu = (FloatingActionsMenu) findViewById(R.id.fab_menu);
        fabMenu.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                llViewControl.setVisibility(View.VISIBLE);
                flBlurView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onMenuCollapsed() {
                llViewControl.setVisibility(View.GONE);
                flBlurView.setVisibility(View.GONE);
            }
        });


        tvCancel = (TextView) findViewById(R.id.tv_cancel_work);
        tvCancel.setOnClickListener(this);
        tvComplete = (TextView) findViewById(R.id.tv_complete_work);
        tvComplete.setOnClickListener(this);

        fabFindWay = (FloatingActionButton) findViewById(R.id.fab_find_way);
        fabFindWay.setOnClickListener(this);
        fabCall = (FloatingActionButton) findViewById(R.id.fab_call);
        fabCall.setOnClickListener(this);
        fabAddProduct = (FloatingActionButton) findViewById(R.id.fab_add_product);
        fabAddProduct.setOnClickListener(this);
        fabScanQR = (FloatingActionButton) findViewById(R.id.fab_scan_qr);
        fabScanQR.setOnClickListener(this);

    }


    public void myLocationHere(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    private void registerBroadcastReciver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Conts.ACTION_NETWORK_CHANGE);
        registerReceiver(receiver, filter);
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case Conts.ACTION_NETWORK_CHANGE:
                    ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo activeNetInfor = connectivityManager.getActiveNetworkInfo();
                    isNetworkConnected = activeNetInfor != null && activeNetInfor.isConnectedOrConnecting();
//                    if (isNetworkConnected && itemTask != null && latitude != 0 && longitude != 0) {
//                        CommonMethod.jsonRequestUpdateDistance(latitude, longitude, itemTask.getLatitude() + ", " + itemTask.getLongitude(), WorkDetailActivity.this);
//                    }
                    break;
            }

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel_work:
                CommonMethod.makeToast(WorkDetailActivity.this, "Cancel Work");
                fabMenu.collapse();
                break;
            case R.id.tv_complete_work:
                CommonMethod.makeToast(WorkDetailActivity.this, "Completed Work");
                fabMenu.collapse();
                break;
            case R.id.fab_find_way:
                fabMenu.collapse();
                break;
            case R.id.fab_call:
                fabMenu.collapse();
                break;
            case R.id.fab_add_product:
                Intent intent=new Intent(this,AddDeviceActivity.class);
                startActivity(intent);
//                CommonMethod.makeToast(WorkDetailActivity.this, "fab_add_product");
                fabMenu.collapse();
                break;
            case R.id.fab_scan_qr:
                IntentIntegrator integrator = new IntentIntegrator(this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.initiateScan();
                fabMenu.collapse();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {

            } else {
                String content = result.getContents();
                String format = result.getFormatName();
                CommonMethod.makeToast(WorkDetailActivity.this, content + ", " + format);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
        unregisterReceiver(receiver);
    }

    @Override
    public void onBackPressed() {
        if (fabMenu.isExpanded()) {
            fabMenu.collapse();
            return;
        }
        super.onBackPressed();
    }

}
