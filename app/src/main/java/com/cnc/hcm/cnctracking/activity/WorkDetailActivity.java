package com.cnc.hcm.cnctracking.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cnc.hcm.cnctracking.R;
import com.cnc.hcm.cnctracking.event.OnResultTimeDistance;
import com.cnc.hcm.cnctracking.model.ItemWork;
import com.cnc.hcm.cnctracking.service.GPSService;
import com.cnc.hcm.cnctracking.util.CommonMethod;
import com.cnc.hcm.cnctracking.util.Conts;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class WorkDetailActivity extends AppCompatActivity implements View.OnClickListener, OnResultTimeDistance {

    private static final String TAGG = WorkDetailActivity.class.getSimpleName();
    private static final int UPDATE_DISTANCE = 23;
    private LinearLayout llViewControl;
    private LinearLayout llContentContact;
    private LinearLayout llContentBill;
    private FrameLayout flBlurView;
    private FloatingActionsMenu fabMenu;
    private RelativeLayout rlExpandBill;
    private RelativeLayout rlExpandContact;
    private ImageView imvExpandContact;
    private ImageView imvExpandBill;
    private TextView tvComplete;
    private TextView tvCancel;
    private TextView tvContactPhone, tvContactAddress, tvNote;
    private TextView tvTitleWork, tvAppointmentDate, tvTimeGetWorkHour, tvTimeGetWorkDate, tvDistance,
            tvStatusWork, tvTimeStart, tvTimeEnd, tvTotalTime, tvContactName, tvTotalPayment, tvStatusPayment;
    private FloatingActionButton fabCall, fabFindWay, fabScanQR, fabAddProduct;

    //TOTO
    private RelativeLayout rlItemProduct;

    private ItemWork itemWork;
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
        getData();

    }

    private void getData() {
        itemWork = getIntent().getParcelableExtra(Conts.KEY_OBJECT_ITEM_WORK);
        setDataToView(itemWork);
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

        rlExpandContact = (RelativeLayout) findViewById(R.id.rl_expand_contact);
        rlExpandContact.setOnClickListener(this);
        rlExpandBill = (RelativeLayout) findViewById(R.id.rl_expand_header_bill);
        rlExpandBill.setOnClickListener(this);

        llContentContact = (LinearLayout) findViewById(R.id.ll_content_contact);
        llContentBill = (LinearLayout) findViewById(R.id.ll_content_bill);
        imvExpandContact = (ImageView) findViewById(R.id.imv_expand_contact);
        imvExpandBill = (ImageView) findViewById(R.id.imv_expand_bill);

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

        tvTitleWork = (TextView) findViewById(R.id.tv_detail_work_title_work);
        tvAppointmentDate = (TextView) findViewById(R.id.tv_detail_work_appointment_date);
        tvAppointmentDate.setSelected(true);
        tvTimeGetWorkHour = (TextView) findViewById(R.id.tv_detail_work_time_get_work_hour);
        tvTimeGetWorkDate = (TextView) findViewById(R.id.tv_detail_work_time_get_work_date);
        tvDistance = (TextView) findViewById(R.id.tv_detail_work_distance);
        tvStatusWork = (TextView) findViewById(R.id.tv_detail_work_status_work);
        tvTimeStart = (TextView) findViewById(R.id.tv_detail_work_time_start_wrok);
        tvTimeEnd = (TextView) findViewById(R.id.tv_detail_work_time_end_wrok);
        tvTotalTime = (TextView) findViewById(R.id.tv_detail_work_total_time);
        tvContactPhone = (TextView) findViewById(R.id.tv_detail_work_contact_phone);
        tvContactAddress = (TextView) findViewById(R.id.tv_detail_work_contact_address);
        tvNote = (TextView) findViewById(R.id.tv_detail_work_note);
        tvContactName = (TextView) findViewById(R.id.tv_detail_work_contact_name);
        tvTotalPayment = (TextView) findViewById(R.id.tv_detail_work_total_payment);
        tvStatusPayment = (TextView) findViewById(R.id.tv_detail_work_status_payment);

        rlItemProduct = (RelativeLayout) findViewById(R.id.item_product);
        rlItemProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WorkDetailActivity.this, ProductDetailActivity.class));
            }
        });
    }

    public void setDataToView(ItemWork itemWork) {
        if (itemWork != null) {
            tvTitleWork.setText(itemWork.getTitleWork());
            tvAppointmentDate.setText(itemWork.getAppointmentDate());
            tvAppointmentDate.setSelected(true);
            tvTimeGetWorkHour.setText(itemWork.getTimeGetWork());
            tvTimeGetWorkDate.setText(itemWork.getTimeGetWork());
            tvDistance.setText(itemWork.getDistanceToMyLocation());
            if (itemWork.isStatusWork()) {
                tvStatusWork.setText(getResources().getString(R.string.status_work_completed));
            } else {
                tvStatusWork.setText(getResources().getString(R.string.status_work_not_completed));
            }
            tvTimeStart.setText(itemWork.getTimeStart());
            tvTimeEnd.setText(itemWork.getTimeEnd());
            tvTotalTime.setText("Chua tinh dc");
            tvContactName.setText(itemWork.getContactName());
            tvContactPhone.setText(itemWork.getContactPhone());
            tvContactAddress.setText(itemWork.getAddress());
            tvNote.setText(itemWork.getNoteService());
            tvTotalPayment.setText("Chua tinh dc");
            if (itemWork.isStatusPayment()) {
                tvStatusPayment.setText(getResources().getString(R.string.status_payment_completed));
            } else {
                tvStatusPayment.setText(getResources().getString(R.string.status_payment_not_completed));
            }
        } else {
            tvTitleWork.setText(getResources().getString(R.string.string_null));
            tvAppointmentDate.setText(getResources().getString(R.string.string_null));
            tvTimeGetWorkHour.setText(getResources().getString(R.string.string_null));
            tvTimeGetWorkDate.setText(getResources().getString(R.string.string_null));
            tvDistance.setText(getResources().getString(R.string.string_null));
            tvStatusWork.setText(getResources().getString(R.string.string_null));
            tvTimeStart.setText(getResources().getString(R.string.string_null));
            tvTimeEnd.setText(getResources().getString(R.string.string_null));
            tvTotalTime.setText(getResources().getString(R.string.string_null));
            tvContactName.setText(getResources().getString(R.string.string_null));
            tvContactPhone.setText(getResources().getString(R.string.string_null));
            tvContactAddress.setText(getResources().getString(R.string.string_null));
            tvNote.setText(getResources().getString(R.string.string_null));
            tvTotalPayment.setText(getResources().getString(R.string.string_null));
            tvStatusPayment.setText(getResources().getString(R.string.string_null));
        }

    }

    public void myLocationHere(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        if (itemWork != null) {
            CommonMethod.jsonRequest(latitude, longitude, itemWork.getLatitude() + ", " + itemWork.getLongitude(), WorkDetailActivity.this);
        }
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
                    if (isNetworkConnected && itemWork != null && latitude != 0 && longitude != 0) {
                        CommonMethod.jsonRequest(latitude, longitude, itemWork.getLatitude() + ", " + itemWork.getLongitude(), WorkDetailActivity.this);
                    }
                    break;
            }

        }
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE_DISTANCE:
                    tvDistance.setText(itemWork.getDistanceToMyLocation());
                    break;
            }
        }
    };
    private Runnable runableNotiDataSetChange = new Runnable() {
        @Override
        public void run() {

            Message message = new Message();
            message.what = UPDATE_DISTANCE;
            message.setTarget(handler);
            message.sendToTarget();
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_expand_contact:
                if (llContentContact.getVisibility() == View.GONE) {
                    llContentContact.setVisibility(View.VISIBLE);
                    imvExpandContact.setImageResource(R.drawable.ic_expand_task_details);
                } else {
                    llContentContact.setVisibility(View.GONE);
                    imvExpandContact.setImageResource(R.drawable.ic_chevron_right);
                }
                break;
            case R.id.rl_expand_header_bill:
                if (llContentBill.getVisibility() == View.GONE) {
                    llContentBill.setVisibility(View.VISIBLE);
                    imvExpandBill.setImageResource(R.drawable.ic_expand_task_details);
                } else {
                    llContentBill.setVisibility(View.GONE);
                    imvExpandBill.setImageResource(R.drawable.ic_chevron_right);
                }
                break;
            case R.id.tv_cancel_work:
                CommonMethod.toast(WorkDetailActivity.this, "Cancel Work");
                fabMenu.collapse();
                break;
            case R.id.tv_complete_work:
                CommonMethod.toast(WorkDetailActivity.this, "Completed Work");
                fabMenu.collapse();
                break;
            case R.id.fab_find_way:
                if (gpsService != null && itemWork != null) {
                    CommonMethod.actionFindWayInMapApp(WorkDetailActivity.this, gpsService.getLatitude(),
                            gpsService.getLongitude(), itemWork.getLatitude(), itemWork.getLongitude());
                } else {
                    CommonMethod.toast(WorkDetailActivity.this, getResources().getString(R.string.cannot_get_current_location));
                }
                fabMenu.collapse();
                break;
            case R.id.fab_call:
                CommonMethod.actionCall(WorkDetailActivity.this, tvContactPhone.getText().toString().trim());
                fabMenu.collapse();
                break;
            case R.id.fab_add_product:
                CommonMethod.toast(WorkDetailActivity.this, "fab_add_product");
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
                CommonMethod.toast(WorkDetailActivity.this, content + ", " + format);
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

    @Override
    public void editData(int index, String distance, String duration) {
        itemWork.setDistanceToMyLocation(distance);
    }

    @Override
    public void postToHandle() {
        handler.post(runableNotiDataSetChange);
    }
}
