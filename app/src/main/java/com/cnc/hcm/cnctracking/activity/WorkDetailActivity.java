package com.cnc.hcm.cnctracking.activity;

import android.app.ProgressDialog;
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
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cnc.hcm.cnctracking.R;
import com.cnc.hcm.cnctracking.adapter.WorkDetailPageAdapter;
import com.cnc.hcm.cnctracking.api.ApiUtils;
import com.cnc.hcm.cnctracking.api.MHead;
import com.cnc.hcm.cnctracking.fragment.WorkDetailDeviceFragment;
import com.cnc.hcm.cnctracking.fragment.WorkDetailServiceFragment;
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
    private FloatingActionButton fabScanQR, fabAddProduct;

    private ImageView iv_back;
    private TextView tv_detail_work_title_work;
    private TextView tv_detail_work_time;
    private TextView tv_detail_work_address;
    private TextView tv_detail_work_distance;

    //TOTO
    private GPSService gpsService;
    private ProgressDialog mProgressDialog;

    private double latitude;
    private double longitude;
    private boolean isNetworkConnected;
    private String idTask;
    private String customerId;

    private ViewPager vp_body;

    private List<TaskDetailLoadedListener> mTaskDetailLoadedListener = new ArrayList<>();

    private GetTaskDetailResult getTaskDetailResult;

    private WorkDetailPageAdapter mWorkDetailPageAdapter;

    public void setTaskDetailLoadedListener(TaskDetailLoadedListener taskDetailLoadedListener) {
        mTaskDetailLoadedListener.add(taskDetailLoadedListener);
        taskDetailLoadedListener.onTaskDetailLoaded(getTaskDetailResult);
        taskDetailLoadedListener.onLocationUpdate(latitude, longitude);
    }

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
        mTaskDetailLoadedListener.clear();
        registerBroadcastReciver();
        setContentView(R.layout.activity_work_detail);
        bindService(new Intent(this, GPSService.class), serviceConnection, Context.BIND_AUTO_CREATE);
        initViews();
        loadTaskInfoToUI();
    }

    private void initViews() {
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
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

        tvComplete = (TextView) findViewById(R.id.tv_complete_work);
        tvComplete.setOnClickListener(this);

        fabAddProduct = (FloatingActionButton) findViewById(R.id.fab_add_product);
        fabAddProduct.setOnClickListener(this);
        fabScanQR = (FloatingActionButton) findViewById(R.id.fab_scan_qr);
        fabScanQR.setOnClickListener(this);

        tv_detail_work_title_work = (TextView) findViewById(R.id.tv_detail_work_title_work);
        tv_detail_work_time = (TextView) findViewById(R.id.tv_detail_work_time);
        tv_detail_work_address = (TextView) findViewById(R.id.tv_detail_work_address);
        tv_detail_work_distance = (TextView) findViewById(R.id.tv_detail_work_distance);

        vp_body = findViewById(R.id.vp_body);
        mWorkDetailPageAdapter = new WorkDetailPageAdapter(getSupportFragmentManager());
        mWorkDetailPageAdapter.addFragment(new WorkDetailServiceFragment());
        mWorkDetailPageAdapter.addFragment(new WorkDetailDeviceFragment());
        vp_body.setAdapter(mWorkDetailPageAdapter);
    }

    private void loadTaskInfoToUI() {
        idTask = getIntent().getStringExtra(Conts.KEY_ID_TASK);
//        customerId = getIntent().getStringExtra(Conts.KEY_CUSTOMER_ID);
        if (idTask != null) {
            tryGetTaskDetail(UserInfo.getInstance(getApplicationContext()).getAccessToken(), idTask);
        }
    }

    private void tryGetTaskDetail(String accessToken, String idTask) {
        showDialogLoadding();
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
                    getTaskDetailResult = response.body();
                    Log.e(TAGG, "tryGetTaskDetail.onResponse(), --> getTaskDetailResult: " + getTaskDetailResult.toString());
                    onTaskInfoLoaded(getTaskDetailResult);
                    dismisDialogLoading();
                }
            }

            @Override
            public void onFailure(Call<GetTaskDetailResult> call, Throwable t) {
                dismisDialogLoading();
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
                    tv_detail_work_time.setText(date.substring(date.indexOf("T") + 1, date.indexOf("T") + 6));
                }
                if (getTaskDetailResult.result.address != null) {
                    tv_detail_work_address.setText(getTaskDetailResult.result.address.street + "");
                }
                tv_detail_work_distance.setText("0 km");    //TODO update distance later
                if (getTaskDetailResult.result.customer != null) {
                    customerId = getTaskDetailResult.result.customer._id;
                }
                for (TaskDetailLoadedListener taskDetailLoadedListener : mTaskDetailLoadedListener) {
                    if (taskDetailLoadedListener != null) {
                        taskDetailLoadedListener.onTaskDetailLoaded(getTaskDetailResult);
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAGG, "onTaskInfoLoaded() --> Exception occurs.", e);
        }
    }

    public void myLocationHere(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        for (TaskDetailLoadedListener taskDetailLoadedListener : mTaskDetailLoadedListener) {
            if (taskDetailLoadedListener != null) {
                taskDetailLoadedListener.onLocationUpdate(latitude, longitude);
            }
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
//                    if (isNetworkConnected && itemTask != null && latitude != 0 && longitude != 0) {
//                        CommonMethod.jsonRequestUpdateDistance(latitude, longitude, itemTask.getLatitude() + ", " + itemTask.getLongitude(), WorkDetailActivity.this);
//                    }
                    break;
            }

        }
    };

    private void showDialogLoadding() {
        mProgressDialog = new ProgressDialog(WorkDetailActivity.this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage(getResources().getString(R.string.loadding));
        mProgressDialog.show();
    }

    private void dismisDialogLoading() {
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_complete_work:
                CommonMethod.makeToast(WorkDetailActivity.this, "Completed Work");
                fabMenu.collapse();
                break;
            case R.id.fab_add_product:
                Intent intent = new Intent(this, AddProductActivity.class);
                intent.putExtra(Conts.KEY_CUSTOMER_ID, customerId);
                startActivity(intent);
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

    public interface TaskDetailLoadedListener {
        void onTaskDetailLoaded(GetTaskDetailResult getTaskDetailResult);
        void onLocationUpdate(double latitude, double longitude);
    }
}
