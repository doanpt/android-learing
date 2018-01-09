package com.cnc.hcm.cnctracking.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cnc.hcm.cnctracking.R;
import com.cnc.hcm.cnctracking.api.ApiUtils;
import com.cnc.hcm.cnctracking.api.MHead;
import com.cnc.hcm.cnctracking.model.GetTaskDetailResult;
import com.cnc.hcm.cnctracking.service.GPSService;
import com.cnc.hcm.cnctracking.util.CommonMethod;
import com.cnc.hcm.cnctracking.util.Conts;
import com.cnc.hcm.cnctracking.util.UserInfo;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAGG = WorkDetailActivity.class.getSimpleName();
    private LinearLayout llViewControl;
    private FrameLayout flBlurView;
    private FloatingActionsMenu fabMenu;
    private TextView tvComplete;
    private TextView tvCancel;
    private FloatingActionButton fabCall, fabFindWay, fabScanQR, fabAddProduct;
    private String idTask,customerId;
    private RelativeLayout rlExpandHeaderBill;
    private LinearLayout ll_content_bill;
    private LinearLayout tv_detail_work_call_action;
    private LinearLayout tv_detail_work_sms_action;
    private LinearLayout tv_detail_work_address_action;
    private ImageView imv_expand_bill;
    private TextView tv_detail_work_title_work;
    private TextView tv_detail_work_time_get_work_hour;
    private TextView tv_detail_work_address;
    private TextView tv_detail_work_distance;
    private TextView tv_detail_work_contact_name;
    private TextView tv_detail_work_contact_phone;

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
        initViews();
        loadTaskInfoToUI();
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

        rlExpandHeaderBill = (RelativeLayout) findViewById(R.id.rl_expand_header_bill);
        ll_content_bill = (LinearLayout) findViewById(R.id.ll_content_bill);
        imv_expand_bill = (ImageView) findViewById(R.id.imv_expand_bill);
        rlExpandHeaderBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ll_content_bill.getVisibility() == View.GONE) {
                    ll_content_bill.setVisibility(View.VISIBLE);
                    imv_expand_bill.setImageResource(R.drawable.ic_expand_task_details);
                } else {
                    ll_content_bill.setVisibility(View.GONE);
                    imv_expand_bill.setImageResource(R.drawable.ic_chevron_right);
                }
            }
        });

        tv_detail_work_title_work = (TextView) findViewById(R.id.tv_detail_work_title_work);
        tv_detail_work_time_get_work_hour = (TextView) findViewById(R.id.tv_detail_work_time_get_work_hour);
        tv_detail_work_address = (TextView) findViewById(R.id.tv_detail_work_address);
        tv_detail_work_distance = (TextView) findViewById(R.id.tv_detail_work_distance);
        tv_detail_work_contact_name = (TextView) findViewById(R.id.tv_detail_work_contact_name);
        tv_detail_work_contact_phone = (TextView) findViewById(R.id.tv_detail_work_contact_phone);

        tv_detail_work_call_action = (LinearLayout) findViewById(R.id.tv_detail_work_call_action);
        tv_detail_work_sms_action = (LinearLayout) findViewById(R.id.tv_detail_work_sms_action);
        tv_detail_work_address_action = (LinearLayout) findViewById(R.id.tv_detail_work_address_action);
    }

    private void loadTaskInfoToUI() {
        idTask = getIntent().getStringExtra(Conts.KEY_ID_TASK);
        customerId = getIntent().getStringExtra(Conts.KEY_CUSTOMER_ID);
        CommonMethod.makeToast(this, "ID: " + idTask);
        tryGetTaskDetail(UserInfo.getInstance(getApplicationContext()).getAccessToken(), idTask);
    }

    private void tryGetTaskDetail(String accessToken, String idTask) {
        Log.e(TAGG, "tryGetTaskDetail(), accessToken: " + accessToken + ", idTask: " + idTask);
        List<MHead> arrHeads = new ArrayList<>();
        arrHeads.add(new MHead(Conts.KEY_ACCESS_TOKEN, accessToken));
        ApiUtils.getAPIService(arrHeads).getTaskDetails(idTask).enqueue(new Callback<GetTaskDetailResult>() {
            @Override
            public void onResponse(Call<GetTaskDetailResult> call, Response<GetTaskDetailResult> response) {
                int statusCode = response.code();
                Log.e(TAGG, "tryGetTaskDetail.onResponse(), statusCode: " + statusCode);
                if (response.isSuccessful()) {
                    Log.e(TAGG, "tryGetTaskDetail.onResponse(), --> response: " + response.toString());
                    GetTaskDetailResult getTaskDetailResult = response.body();
                    Log.e(TAGG, "tryGetTaskDetail.onResponse(), --> getTaskDetailResult: " + getTaskDetailResult.toString());
                    onTaskInfoLoaded(getTaskDetailResult);
                }
            }

            @Override
            public void onFailure(Call<GetTaskDetailResult> call, Throwable t) {
                Log.e(TAGG, "tryGetTaskDetail.onFailure() --> " + t);
            }
        });
    }

    private void onTaskInfoLoaded(GetTaskDetailResult getTaskDetailResult) {
        try {
            if (getTaskDetailResult != null && getTaskDetailResult.result != null) {
                tv_detail_work_title_work.setText(getTaskDetailResult.result.title + "");
                String date = getTaskDetailResult.result.createdDate;
                if (!TextUtils.isEmpty(date)) {
                    tv_detail_work_time_get_work_hour.setText(date.substring(date.indexOf("T") + 1, date.indexOf("T") + 6));
                }
                if (getTaskDetailResult.result.address != null) {
                    tv_detail_work_address.setText(getTaskDetailResult.result.address.street + "");
                    handleActions(getTaskDetailResult.result.address);
                }
                if (getTaskDetailResult.result.customer != null) {
                    tv_detail_work_contact_name.setText(getTaskDetailResult.result.customer.fullname + "");
                    tv_detail_work_contact_phone.setText(getTaskDetailResult.result.customer.phone + "");

                    handleActions(getTaskDetailResult.result.customer);
                }
                tv_detail_work_distance.setText("0 km");
            }
        } catch (Exception e) {
            Log.e(TAGG, "onTaskInfoLoaded() --> Exception occurs.", e);
        }
    }

    private void handleActions(final GetTaskDetailResult.Result.Address address) {
        tv_detail_work_address_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr=" + latitude + "," + longitude
                                + "&daddr=" + address.location.latitude + "," + address.location.longitude));
                startActivity(intent);
            }
        });
    }

    private void handleActions(final GetTaskDetailResult.Result.Customer customer) {
        tv_detail_work_call_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", customer.phone, null)));
            }
        });
        tv_detail_work_sms_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Uri uri = Uri.parse("smsto:" + customer.phone);
                    Intent smsIntent = new Intent(Intent.ACTION_SENDTO, uri);
                    smsIntent.putExtra("sms_body", "sms content");
                    startActivity(smsIntent);
                } catch (Exception e) {
                    Log.e(TAGG, "send sms --> Exception occurs.", e);
                }
            }
        });
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
                Intent intent = new Intent(this, AddProductActivity.class);
                intent.putExtra(Conts.KEY_CUSTOMER_ID, customerId);
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
