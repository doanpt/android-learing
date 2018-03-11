package com.cnc.hcm.cnctracking.activity;

import android.Manifest;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Address;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cnc.hcm.cnctracking.R;
import com.cnc.hcm.cnctracking.adapter.TaskListAdapter;
import com.cnc.hcm.cnctracking.api.ApiUtils;
import com.cnc.hcm.cnctracking.api.MHead;
import com.cnc.hcm.cnctracking.customeview.MyRecyclerView;
import com.cnc.hcm.cnctracking.dialog.DialogDetailTaskFragment;
import com.cnc.hcm.cnctracking.dialog.DialogGPSSetting;
import com.cnc.hcm.cnctracking.dialog.DialogNetworkSetting;
import com.cnc.hcm.cnctracking.dialog.DialogNotiTaskAppointment;
import com.cnc.hcm.cnctracking.dialog.DialogNotification;
import com.cnc.hcm.cnctracking.dialog.DialogOptionFilter;
import com.cnc.hcm.cnctracking.fragment.MonthViewFragment;
import com.cnc.hcm.cnctracking.fragment.YearsViewFragment;
import com.cnc.hcm.cnctracking.model.GetTaskDetailResult;
import com.cnc.hcm.cnctracking.model.GetTaskListResult;
import com.cnc.hcm.cnctracking.model.ItemCancelTask;
import com.cnc.hcm.cnctracking.model.ItemMarkedMap;
import com.cnc.hcm.cnctracking.model.ItemTask;
import com.cnc.hcm.cnctracking.model.ResponseCNC;
import com.cnc.hcm.cnctracking.service.GPSService;
import com.cnc.hcm.cnctracking.util.CommonMethod;
import com.cnc.hcm.cnctracking.util.Conts;
import com.cnc.hcm.cnctracking.util.SettingApp;
import com.cnc.hcm.cnctracking.util.UserInfo;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import biz.laenger.android.vpbs.ViewPagerBottomSheetBehavior;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends FragmentActivity implements View.OnClickListener,
        OnMapReadyCallback, GoogleMap.OnMarkerClickListener, TaskListAdapter.OnItemWorkClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_CODE_LOCATION_UPDATE = 4234;
    private GoogleMap mMap;

    private LinearLayout llClickNetwork, llClickGPS, llClickSetting, llClickHelp, llTabs,
            llTabNewWork, llTabDoingWork, llTabCompleteWork;
    private LinearLayout bottomSheetLayout;
    private ViewPagerBottomSheetBehavior bottomSheetBehavior;

    private TextView tvStatusNetwork, tvStatusGPS, tvUsername, tvUserMail, tvMonth, tvYear, tvChangeViewMonthYear, tvToday;
    private TextView tvQuantityNewTask, tvQuantityDoingTask, tvQuantityCompleteTask;
    private Button btnLogout;
    private CircleImageView imvAvatar;
    private ImageView imvMenuDrawer, imvFilterActionBar, imvProfile, imvFilter;
    private DrawerLayout drawer;
    private ProgressDialog mProgressDialog;
    private MyRecyclerView rcTask;

    private MonthViewFragment monthViewFragment;
    private YearsViewFragment yearsViewFragment;

    private Fragment[] listFrag;
    private GPSService gpsService;
    private DialogDetailTaskFragment dialogDetailTaskFragment;
    private DialogNetworkSetting dialogNetworkSetting;
    private DialogGPSSetting dialogGPSSetting;
    private TaskListAdapter taskListAdapter;

    private ArrayList<ItemTask> arrItemTask = new ArrayList<>();
    private ArrayList<ItemMarkedMap> arrMarkedTask = new ArrayList<>();
    private boolean isNetworkConnected;
    private int quantityNewTask, quantityDoingTask, quantityCompletedTask;
    private boolean isMainActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initObject();
        initViews();
        createFileBackup();
        UserInfo.getInstance(this).setMainActivityActive(true);
        registerBroadcastReciver();
        if (!isServiceRunning(GPSService.class)) {
            Intent intent = new Intent(this, GPSService.class);
            intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
            startService(intent);
        }
        bindService(new Intent(this, GPSService.class), serviceConnection, Context.BIND_AUTO_CREATE);
        checkUserLoginOnOtherDevice();
        initMap();
        checkExistIdTask();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isMainActive = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isMainActive = false;
    }

    private void checkExistIdTask() {
        String idTask = getIntent().getStringExtra(Conts.KEY_ID_TASK_TO_SHOW_DETAIL);
        if (idTask != null && !idTask.equals(Conts.BLANK)) {
            if (dialogDetailTaskFragment != null) {
                showTaskDetail(idTask);
            }
        }
    }

    private void initObject() {
        dialogNetworkSetting = new DialogNetworkSetting(this);
        dialogGPSSetting = new DialogGPSSetting(this);
        dialogDetailTaskFragment = new DialogDetailTaskFragment();
        dialogDetailTaskFragment.setMainActivity(MainActivity.this);

        taskListAdapter = new TaskListAdapter(this);
        taskListAdapter.setOnItemWorkClickListener(this);
    }

    private void initViews() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        imvMenuDrawer = (ImageView) findViewById(R.id.imv_menu);
        imvFilterActionBar = (ImageView) findViewById(R.id.imv_filter_list_on_action_bar);
        imvProfile = (ImageView) findViewById(R.id.imv_profile);
        imvFilter = (ImageView) findViewById(R.id.imv_filter_list);

        imvProfile.setOnClickListener(this);
        imvMenuDrawer.setOnClickListener(this);
        imvFilterActionBar.setOnClickListener(this);
        imvFilter.setOnClickListener(this);

        llClickNetwork = (LinearLayout) findViewById(R.id.ll_click_network);
        llClickGPS = (LinearLayout) findViewById(R.id.ll_click_gps);
        llClickSetting = (LinearLayout) findViewById(R.id.ll_click_setting);
        llClickHelp = (LinearLayout) findViewById(R.id.ll_click_help);
        llTabs = (LinearLayout) findViewById(R.id.tabs);
        llTabNewWork = (LinearLayout) findViewById(R.id.ll_tab_work_new_not_read);
        llTabDoingWork = (LinearLayout) findViewById(R.id.ll_tab_work_doing);
        llTabCompleteWork = (LinearLayout) findViewById(R.id.ll_tab_work_complete);

        llTabs.setOnClickListener(this);
        llClickNetwork.setOnClickListener(this);
        llClickGPS.setOnClickListener(this);
        llClickSetting.setOnClickListener(this);
        llClickHelp.setOnClickListener(this);
        llTabNewWork.setOnClickListener(this);
        llTabDoingWork.setOnClickListener(this);
        llTabCompleteWork.setOnClickListener(this);

        tvStatusNetwork = (TextView) findViewById(R.id.tv_status_network);
        tvStatusGPS = (TextView) findViewById(R.id.tv_status_gps);
        tvMonth = (TextView) findViewById(R.id.tv_month_calendar);
        tvMonth.setOnClickListener(this);
        tvYear = (TextView) findViewById(R.id.tv_year_calendar);
        tvUsername = (TextView) findViewById(R.id.tv_username_profile);
        tvUserMail = (TextView) findViewById(R.id.tv_user_email_profile);
        tvUsername.setText(UserInfo.getInstance(this).getUsername());
        tvUserMail.setText(UserInfo.getInstance(this).getUserEmail());
        tvChangeViewMonthYear = (TextView) findViewById(R.id.tv_change_view_month_year);
        tvChangeViewMonthYear.setOnClickListener(this);
        tvToday = (TextView) findViewById(R.id.tv_today);
        tvToday.setOnClickListener(this);
        tvQuantityNewTask = (TextView) findViewById(R.id.tv_quantity_new_task);
        tvQuantityDoingTask = (TextView) findViewById(R.id.tv_quantity_doing_task);
        tvQuantityCompleteTask = (TextView) findViewById(R.id.tv_quantity_done_task);


        btnLogout = (Button) findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(this);

        imvAvatar = (CircleImageView) findViewById(R.id.imv_avatar_profile);
        imvAvatar.setOnClickListener(this);


        Picasso.with(this).load(Conts.URL_BASE + UserInfo.getInstance(this).getUserUrlImage())
                .placeholder(R.drawable.ic_account)
                .error(R.drawable.ic_account).into(imvAvatar);


        bottomSheetLayout = (LinearLayout) findViewById(R.id.linear_layout_bottom_sheet);
        bottomSheetBehavior = ViewPagerBottomSheetBehavior.from(bottomSheetLayout);
        bottomSheetBehavior.setBottomSheetCallback(new ViewPagerBottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
            }

            @Override
            public void onSlide(@NonNull View view, float v) {
                if (v <= 0) {
                    llTabs.setVisibility(View.VISIBLE);
                } else if (v > 0) {
                    llTabs.setVisibility(View.GONE);
                }

            }
        });

        rcTask = (MyRecyclerView) findViewById(R.id.rc_task);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcTask.setLayoutManager(layoutManager);
        rcTask.setAdapter(taskListAdapter);

        int typeView = SettingApp.getInstance(this).getTypeView();
        callViewCalendar(typeView);


    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            gpsService = ((GPSService.MyBinder) iBinder).getGPSService();
            gpsService.setMainActivity(MainActivity.this);

            //FIXME this code which is commented could remove if you want
//            if (gpsService.getmSocket() == null || !gpsService.getmSocket().connected()) {
//                String token = UserInfo.getInstance(MainActivity.this).getAccessToken();
//                Log.d("GPSService","Call connect in MainActivity");
//                gpsService.connectSocket(token);
//            }
            Log.d(TAG, "ServiceConnection at MainActivity");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d(TAG, "onServiceDisconnected at MainActivity");
        }
    };

    private void registerBroadcastReciver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Conts.ACTION_NETWORK_CHANGE);
        registerReceiver(receiver, filter);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case Conts.ACTION_NETWORK_CHANGE:
                    ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo activeNetInfor = connectivityManager.getActiveNetworkInfo();
                    isNetworkConnected = activeNetInfor != null && activeNetInfor.isConnectedOrConnecting();
                    if (tvStatusNetwork != null)
                        if (isNetworkConnected) {
                            tvStatusNetwork.setTextColor(getResources().getColor(R.color.color_text_status));
                            tvStatusNetwork.setText(getResources().getString(R.string.On));
                            if (gpsService != null) {
                                updateDistanceTo4TabWork();
                            }

                        } else {
                            tvStatusNetwork.setTextColor(getResources().getColor(android.R.color.darker_gray));
                            tvStatusNetwork.setText(getResources().getString(R.string.Off));
                        }
                    break;
            }

        }
    };


    public void tryGetTaskList(String accessToken, final String startDate, final String endDate) {
        showProgressLoadding();
        final List<MHead> arrHeads = new ArrayList<>();
        arrHeads.add(new MHead(Conts.KEY_ACCESS_TOKEN, accessToken));
        arrHeads.add(new MHead(Conts.KEY_START_DATE, startDate));
        arrHeads.add(new MHead(Conts.KEY_END_DATE, endDate));
        ApiUtils.getAPIService(arrHeads).getTaskList().enqueue(new Callback<GetTaskListResult>() {
            @Override
            public void onResponse(Call<GetTaskListResult> call, Response<GetTaskListResult> response) {
                int statusCode = response.code();
                Log.e(TAG, "tryGetTaskList.onResponse(), statusCode: " + statusCode);

                if (response.isSuccessful()) {
                    quantityNewTask = 0;
                    quantityDoingTask = 0;
                    quantityCompletedTask = 0;
                    clearData();
                    Log.e(TAG, "tryGetTaskList.onResponse(), --> response: " + response.toString());
                    GetTaskListResult getTaskListResult = response.body();
                    Log.e(TAG, "tryGetTaskList.onResponse(), --> getTaskListResult: " + getTaskListResult.toString());
                    arrItemTask.clear();
                    if (getTaskListResult != null) {
                        GetTaskListResult.Result[] result1 = getTaskListResult.result;
                        if (result1 != null && result1.length > 0) {
                            for (int index = 0; index < result1.length; index++) {
                                ItemTask itemTask = new ItemTask(result1[index]);
                                arrItemTask.add(itemTask);
                                switch ((int) itemTask.getTaskResult().status._id) {
                                    case Conts.TYPE_COMPLETE_TASK:
                                        quantityCompletedTask++;
                                        break;
                                    case Conts.TYPE_DOING_TASK:
                                        quantityDoingTask++;
                                        break;
                                    case Conts.TYPE_NEW_TASK:
                                        quantityNewTask++;
                                        break;
                                }
                                String idTask = MainActivity.this.getIntent().getStringExtra(Conts.KEY_ID_TASK_TO_SHOW_DETAIL);
                                if (itemTask.getTaskResult()._id.equals(idTask) && !itemTask.getTaskResult().isRead) {
                                    itemTask.getTaskResult().isRead = true;
                                    updateStatusIsRead(idTask, -1);
                                }
                            }
                            if (startDate.equals(endDate)) {
                                if (CommonMethod.checkCurrentDay(startDate)) {
                                    if (gpsService != null) {
                                        gpsService.setListTaskToDay(arrItemTask);
                                    }
                                }
                            }
                            initMarkerOnMap();
                        }
                        updateQuantityTask();
                    } else {
                        CommonMethod.makeToast(MainActivity.this, "Không có công việc nào cả");
                    }
                    dismisProgressLoading();
                    taskListAdapter.notiDataChange(arrItemTask);
                } else {
                    CommonMethod.makeToast(MainActivity.this, response.toString());
                    dismisProgressLoading();
                }

            }

            @Override
            public void onFailure(Call<GetTaskListResult> call, Throwable t) {
                Log.e(TAG, "tryGetTaskList.onFailure() --> " + t);
                t.printStackTrace();
                CommonMethod.makeToast(MainActivity.this, t.getMessage() != null ? t.getMessage().toString() : "onFailure");
                dismisProgressLoading();
            }
        });
    }

    private void clearData() {
        if (mMap != null) {
            mMap.clear();
        }
    }


    private void updateQuantityTask() {
        tvQuantityNewTask.setText(quantityNewTask + Conts.BLANK);
        tvQuantityDoingTask.setText(quantityDoingTask + Conts.BLANK);
        tvQuantityCompleteTask.setText(quantityCompletedTask + Conts.BLANK);
    }

    public void receiverNewTask(GetTaskListResult.Result result) {

        String currenDate = CommonMethod.formatTimeStandand(CommonMethod.getInstanceCalendar().getTime());

        String appointmentDate = result.appointmentDate.substring(0, result.appointmentDate.lastIndexOf(".")) + "Z";
        Date appmentDate = CommonMethod.formatTimeFromServerToDate(appointmentDate);
        String appDate = CommonMethod.formatTimeStandand(appmentDate);

        if (currenDate.equals(appDate)) {
            quantityNewTask++;
            updateQuantityTask();
            arrItemTask.add(new ItemTask(result));
            if (taskListAdapter != null) {
                taskListAdapter.notiDataChange(arrItemTask);
            }

        }

        notiRingtoneTypeAndVibrator(RingtoneManager.TYPE_NOTIFICATION, 1500);

    }

    private void initMarkerOnMap() {
        for (ItemTask task : arrItemTask) {
            BitmapDescriptor bitmapDescriptor = null;
            switch ((int) task.getTaskResult().status._id) {
                case Conts.TYPE_DOING_TASK:
                    bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.ic_marked_task_doing);
                    break;
                case Conts.TYPE_COMPLETE_TASK:
                    bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.ic_marked_task_done);
                    break;
                case Conts.TYPE_CANCEL_TASK:
                    bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.ic_marked_task_cancel);
                    break;
                case Conts.TYPE_NEW_TASK:
                    bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.ic_marked_task_new);
                    break;

            }
            GetTaskListResult.Result result = task.getTaskResult();
            if (result.address != null) {
                if (result.address.location != null) {
                    addMarkerMap(result._id, result.address.location.latitude,
                            result.address.location.longitude, result.address.street, bitmapDescriptor);
                } else {
                    String locationName = result.address.street;
                    Address address = CommonMethod.getLocationFromLocationName(MainActivity.this, locationName);
                    if (address != null) {
                        addMarkerMap(result._id, address.getLatitude(), address.getLongitude(), locationName, bitmapDescriptor);
                    } else {
                        Toast.makeText(this, "Error Address null :" + result.title, Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Error in addMarkerMap and task.getTaskResult().address = null");
                    }
                }
            } else {
                if (result.customer != null && result.customer.address != null && result.customer.address.location != null) {
                    addMarkerMap(result._id, result.customer.address.location.latitude,
                            result.customer.address.location.longitude, result.customer.address.street, bitmapDescriptor);
                } else {
                    Toast.makeText(this, "Error Address CUSTOMER null :" + result.title, Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error in addMarkerMap and task.getTaskResult().address CUSTOMER = null");
                }
            }
        }
    }


    public void callViewCalendar(int typeView) {
        long time = CommonMethod.getInstanceCalendar().getTime().getTime();
        switch (typeView) {
            case Conts.TYPE_VIEW_BY_MONTH:
                SettingApp.getInstance(MainActivity.this).setTypeView(Conts.TYPE_VIEW_BY_MONTH);
                callFragment(new MonthViewFragment());
                tvYear.setVisibility(View.VISIBLE);
                tvMonth.setClickable(false);
                tvMonth.setText(CommonMethod.formatTimeToMonth(time));
                tvYear.setText(CommonMethod.formatTimeToYear(time));
                tvMonth.setTextSize(40);
                tvChangeViewMonthYear.setText(getResources().getString(R.string.text_month));
                break;
            case Conts.TYPE_VIEW_BY_YEARS:
                SettingApp.getInstance(MainActivity.this).setTypeView(Conts.TYPE_VIEW_BY_YEARS);
                callFragment(new YearsViewFragment());
                tvYear.setVisibility(View.GONE);
                tvMonth.setText(CommonMethod.formatTimeToYear(time));
                tvMonth.setClickable(true);
                tvMonth.setTextSize(30);
                tvChangeViewMonthYear.setText(getResources().getString(R.string.text_year));
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imv_avatar_profile:
            case R.id.imv_profile:
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.imv_menu:
                drawer.openDrawer(GravityCompat.START);
                break;
            case R.id.imv_filter_list_on_action_bar:
            case R.id.imv_filter_list:
                DialogOptionFilter filter = new DialogOptionFilter(this);
                filter.show();
                break;
            case R.id.ll_click_network:
                Intent intentWifi = new Intent(Settings.ACTION_WIFI_SETTINGS);
                startActivity(intentWifi);
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.ll_click_gps:
                Intent intentGps = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intentGps);
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.ll_click_setting:
                Toast.makeText(this, "Tính năng đang hoàn thiện. Vui lòng thử lại sau!", Toast.LENGTH_SHORT).show();
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.ll_click_help:
                Toast.makeText(this, "Tính năng đang hoàn thiện. Vui lòng thử lại sau!", Toast.LENGTH_SHORT).show();
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.tabs:
                setCurrentItemViewPager(SettingApp.getInstance(this).getTypeFilterList());
                break;
            case R.id.ll_tab_work_new_not_read:
                setCurrentItemViewPager(Conts.DEFAULT_VALUE_INT_0);
                break;
            case R.id.ll_tab_work_doing:
                setCurrentItemViewPager(Conts.DEFAULT_VALUE_INT_1);
                break;
            case R.id.ll_tab_work_complete:
                setCurrentItemViewPager(Conts.DEFAULT_VALUE_INT_2);
                break;
            case R.id.tv_change_view_month_year:
                showPopupMenu(view);
                break;
            case R.id.tv_today:
                int typeView = SettingApp.getInstance(MainActivity.this).getTypeView();
                switch (typeView) {
                    case Conts.TYPE_VIEW_BY_MONTH:
                        if (monthViewFragment != null) {
                            String dateSelected = monthViewFragment.gotoCurrentDate();
                            String accessToken = UserInfo.getInstance(this).getAccessToken();
                            tryGetTaskList(accessToken, dateSelected, dateSelected);
                        }
                        break;
                    case Conts.TYPE_VIEW_BY_YEARS:
                        if (yearsViewFragment != null) {
                            int month = CommonMethod.getInstanceCalendar().get(Calendar.MONTH);
                            int yeah = CommonMethod.getInstanceCalendar().get(Calendar.YEAR);
                            yearsViewFragment.setMonth(month);
                            yearsViewFragment.getCountTask(yeah);
                        }
                        break;
                }
                break;
            case R.id.tv_month_calendar:
                showPopupMenuSelectYears(view);
                break;
            case R.id.btn_logout:
                actionLogout();
                drawer.closeDrawer(GravityCompat.START);
                break;

        }
    }

    private void showDialogNetworkSetting() {
        if (dialogNetworkSetting != null && !dialogNetworkSetting.isShowing() && !MainActivity.this.isDestroyed()) {
            dialogNetworkSetting.show();
        }
    }

    private void dismisDialogNetworkSetting() {
        if (dialogNetworkSetting != null && dialogNetworkSetting.isShowing() && !MainActivity.this.isDestroyed()) {
            dialogNetworkSetting.dismiss();
        }
    }

    public void handleNetworkSetting(boolean isNetworkConnected) {
        if (isNetworkConnected) {
            dismisDialogNetworkSetting();
        } else {
            showDialogNetworkSetting();
        }
    }

    private void showDialogGPSSetting() {
        if (dialogGPSSetting != null && !dialogGPSSetting.isShowing() && !MainActivity.this.isDestroyed()) {
            dialogGPSSetting.show();
        }
    }

    private void dismisDialogGPSSetting() {
        if (dialogGPSSetting != null && dialogGPSSetting.isShowing() && !MainActivity.this.isDestroyed()) {
            dialogGPSSetting.dismiss();
        }
    }

    public void handleGPSSetting(boolean statusGPS) {
        if (statusGPS) {
            tvStatusGPS.setTextColor(getResources().getColor(R.color.color_text_status));
            tvStatusGPS.setText(getResources().getString(R.string.On));
            dismisDialogGPSSetting();
        } else {
            tvStatusGPS.setTextColor(getResources().getColor(android.R.color.darker_gray));
            tvStatusGPS.setText(getResources().getString(R.string.Off));
            showDialogGPSSetting();
        }
    }


    private void showPopupMenu(View view) {
        PopupMenu popup = new PopupMenu(MainActivity.this, view);
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                int viewType = -1;
                switch (item.getItemId()) {
                    case R.id.menu_month:
                        viewType = Conts.TYPE_VIEW_BY_MONTH;
                        break;
                    case R.id.menu_year:
                        viewType = Conts.TYPE_VIEW_BY_YEARS;
                        break;
                }
                if (viewType != -1) {
                    callViewCalendar(viewType);
                }
                return true;
            }
        });
        popup.show();
    }

    private void showPopupMenuSelectYears(View view) {
        PopupMenu popup = new PopupMenu(MainActivity.this, view);
        popup.getMenuInflater().inflate(R.menu.years_menu, popup.getMenu());

        int currentYears = CommonMethod.getInstanceCalendar().get(Calendar.YEAR);
        int previousYears = currentYears - Conts.DEFAULT_VALUE_INT_1;
        int nextYears = currentYears + Conts.DEFAULT_VALUE_INT_1;

        popup.getMenu().getItem(Conts.DEFAULT_VALUE_INT_0).setTitle(previousYears + Conts.BLANK);
        popup.getMenu().getItem(Conts.DEFAULT_VALUE_INT_1).setTitle(currentYears + Conts.BLANK);
        popup.getMenu().getItem(Conts.DEFAULT_VALUE_INT_2).setTitle(nextYears + Conts.BLANK);

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                int years = Integer.parseInt(item.getTitle().toString());
                if (yearsViewFragment != null) {
                    yearsViewFragment.setYears(years);
                    yearsViewFragment.getCountTask(years);
                    tvMonth.setText(years + Conts.BLANK);
                }
                return true;
            }
        });
        popup.show();
    }

    public void setCurrentItemViewPager(int position) {
        if (bottomSheetBehavior.getState() == ViewPagerBottomSheetBehavior.STATE_COLLAPSED) {
            bottomSheetBehavior.setState(ViewPagerBottomSheetBehavior.STATE_EXPANDED);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UserInfo.getInstance(this).setMainActivityActive(false);
        unbindService(serviceConnection);
        unregisterReceiver(receiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (dialogDetailTaskFragment != null) {
            dialogDetailTaskFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }

        if (bottomSheetBehavior.getState() == ViewPagerBottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(ViewPagerBottomSheetBehavior.STATE_COLLAPSED);
            return;
        }

        super.onBackPressed();
    }

    public void showMessageRequestLogout() {
        final DialogNotification dialog = new DialogNotification(MainActivity.this);
        dialog.setHiddenBtnExit();
        dialog.setContentMessage("Tài khoản này đã được đăng nhập trên thiết bị khác. Ứng dụng sẽ đăng xuất tài khoản. Vui lòng đăng nhập lại để tiếp tục sử dụng.");
        dialog.setCancelable(false);
        dialog.setOnClickDialogNotificationListener(new DialogNotification.OnClickDialogNotificationListener() {
            @Override
            public void onClickButtonOK() {
                actionLogout();
            }

            @Override
            public void onClickButtonExit() {

            }
        });
        dialog.show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_LOCATION_UPDATE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permision accept", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permision denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String permissions[] = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
            requestPermissions(permissions, MainActivity.REQUEST_CODE_LOCATION_UPDATE);
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnMarkerClickListener(MainActivity.this);
        if (gpsService != null && gpsService.getLatitude() != 0 && gpsService.getLongitude() != 0) {
            myLocationHere(gpsService.getLatitude(), gpsService.getLongitude(),
                    gpsService.getAccuracy(), gpsService.getAddressName(), gpsService.getCityName());
        }
    }

    public void myLocationHere(double latitude, double longitude, float accuracy, String addName, String cityName) { // TODO did we need accuracy here?
        if (mMap != null) {
            LatLng myLocation = new LatLng(latitude, longitude);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15));
        }

        updateDistanceTo4TabWork();
    }

    public void addMarkerMap(String idTask, double latitude, double longitude, String addName, BitmapDescriptor bitmapDescriptor) {
        if (mMap != null) {
            //BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
            LatLng myLocation = new LatLng(latitude, longitude);
            Marker marker = mMap.addMarker(new MarkerOptions().position(myLocation).title(addName));
            marker.setIcon(bitmapDescriptor);
            arrMarkedTask.add(new ItemMarkedMap(idTask, marker));
        }
    }


    public void moveCameraMap(LatLng myLocation) {
        if (mMap != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15));
        }
    }

    public double getLongtitude() {
        if (gpsService != null) {
            return gpsService.getLongitude();
        }
        return 0;
    }

    public double getLatitude() {
        if (gpsService != null) {
            return gpsService.getLatitude();
        }
        return 0;
    }

    private void updateDistanceTo4TabWork() {
        if (taskListAdapter != null && gpsService != null) {
            taskListAdapter.updateDistanceForTask(isNetworkConnected, gpsService.getLatitude(), gpsService.getLongitude());
        }
    }

    private void createFileBackup() {
        try {
            File folderBackup = new File(Environment.getExternalStorageDirectory(), "/CoolBackup");
            if (!folderBackup.exists()) {
                folderBackup.mkdirs();
                Log.w("DEBUG", "Created default directory.");
            }
            File file_network = new File(Environment.getExternalStorageDirectory() + "/CoolBackup", Conts.FILE_LOCATION_NETWORK);
            File file_no_network = new File(Environment.getExternalStorageDirectory() + "/CoolBackup", Conts.FILE_LOCATION_NO_NETWORK);
            File file_size = new File(Environment.getExternalStorageDirectory() + "/CoolBackup", Conts.FILE_LOCATION_UPLOAD_SIZE);
            File file_response = new File(Environment.getExternalStorageDirectory() + "/CoolBackup", Conts.FILE_RESPONSE);

            if (!file_network.exists()) {
                file_network.createNewFile();
            }
            if (!file_no_network.exists()) {
                file_no_network.createNewFile();
            }
            if (!file_size.exists()) {
                file_size.createNewFile();
            }
            if (!file_response.exists()) {
                file_response.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void actionLogout() {
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getResources().getString(R.string.text_logout));
        progressDialog.show();

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                UserInfo.getInstance(MainActivity.this).setUserInfoLogout();
                UserInfo.getInstance(MainActivity.this).setUploadFirstTime(true);
                if (gpsService != null) {
                    gpsService.disconnectService();
                }
                progressDialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }, 1000);
    }

    public void appendText(String str) {
//        tvStatus.append("\n" + str);
    }

    @Override
    public void onClickItemWork(int position) {
        if (position == Conts.DEFAULT_VALUE_INT_INVALID) {
            Log.e(TAG, "onClickItemWork() -> position: DEFAULT_VALUE_INT_INVALID");
            Toast.makeText(MainActivity.this, "Công việc này đã bị hủy", Toast.LENGTH_LONG).show();
            return;
        }

        String idTask = taskListAdapter.getItem(position).getTaskResult()._id;
        showTaskDetail(idTask);
        updateStatusIsRead(idTask, position);
    }

    public void showTaskDetail(String idTask) {
        if (dialogDetailTaskFragment != null) {
            dialogDetailTaskFragment.setIdTask(idTask);
            dialogDetailTaskFragment.show(getSupportFragmentManager(), dialogDetailTaskFragment.getTag());
            dialogDetailTaskFragment.setExpaned(true);
        }
    }

    private void updateStatusIsRead(String idTask, final int position) {
        String accessToken = UserInfo.getInstance(this).getAccessToken();
        List<MHead> arrHeads = new ArrayList<>();
        arrHeads.add(new MHead(Conts.KEY_ACCESS_TOKEN, accessToken));
        ApiUtils.getAPIService(arrHeads).updateStatusIsRead(idTask).enqueue(new Callback<ResponseCNC>() {
            @Override
            public void onResponse(Call<ResponseCNC> call, Response<ResponseCNC> response) {
                Log.e(TAG, "updateStatusIsRead.onResponse(), statusCode: " + response.code());
                if (response.isSuccessful()) {
                    Log.e(TAG, "updateStatusIsRead.onResponse(), --> response: " + response.toString());
                    ResponseCNC responseCNC = response.body();
                    if (responseCNC.getStatusCode() == Conts.RESPONSE_STATUS_OK) {
                        if (position > 0) {
                            taskListAdapter.getItem(position).getTaskResult().isRead = true;
                            taskListAdapter.notifyItemChanged(position);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseCNC> call, Throwable t) {
                Log.e(TAG, "updateStatusIsRead.onFailure() --> " + t);
                t.printStackTrace();
            }
        });
    }

    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo serviceInfo : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(serviceInfo.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void setTitleStatusGPS(boolean status) {

    }

    public void filerTask(int type) {
        if (taskListAdapter != null) {
            taskListAdapter.filter(type);
        }
    }

    private void checkUserLoginOnOtherDevice() {
        if (UserInfo.getInstance(this).getUserLoginOnOtherDevice()) {
            showMessageRequestLogout();
        }
    }

    public void callFragment(Fragment fragment) {
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment, fragment);
        fragmentTransaction.commit();
    }

    public void showProgressLoadding() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
        mProgressDialog = new ProgressDialog(MainActivity.this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage(getResources().getString(R.string.loadding));
        mProgressDialog.show();
    }

    public void dismisProgressLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public boolean isNetworkConnected() {
        return isNetworkConnected;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {


        String idTask = getIDMarked(marker);
        if (dialogDetailTaskFragment != null) {
            dialogDetailTaskFragment.setIdTask(idTask);
            dialogDetailTaskFragment.show(getSupportFragmentManager(), dialogDetailTaskFragment.getTag());
        }
        return false;
    }


    private String getIDMarked(Marker marker) {
        String id = Conts.BLANK;
        for (int index = 0; index < arrMarkedTask.size(); index++) {
            if (arrMarkedTask.get(index).getMarker().equals(marker)) {
                id = arrMarkedTask.get(index).getId();
                break;
            }
        }
        return id;
    }

    public void updateMonthChange(CalendarDay date) {
        int month = (date.getMonth() + 1);
        tvMonth.setText(month > 9 ? (month + Conts.BLANK) : "0" + month);
        tvYear.setText(date.getYear() + Conts.BLANK);
    }

    public void setMonthViewFragment(MonthViewFragment monthViewFragment) {
        this.monthViewFragment = monthViewFragment;
    }

    public void setYearsViewFragment(YearsViewFragment yearsViewFragment) {
        this.yearsViewFragment = yearsViewFragment;
    }

    public GPSService getGpsService() {
        return gpsService;
    }

    public boolean isMainActive() {
        return isMainActive;
    }

    public void onCancelTicket(ItemCancelTask itemCancelTask) {
        if (itemCancelTask == null) {
            return;
        }

        if (dialogDetailTaskFragment != null && dialogDetailTaskFragment.isVisible()) {
            dialogDetailTaskFragment.showDialogCancelTicket(itemCancelTask.content, itemCancelTask.data._id);
        } else {
            Toast.makeText(MainActivity.this, itemCancelTask.content + Conts.BLANK, Toast.LENGTH_SHORT).show();
        }

        for (ItemTask itemTask : arrItemTask) {
            if (TextUtils.equals(itemCancelTask.data._id, itemTask.getTaskResult()._id)) {
                itemTask.getTaskResult().status._id = Conts.TYPE_CANCEL_TASK;
                taskListAdapter.notiDataChange(arrItemTask);
                // TODO update các item khác nếu cần. ex: taskCount,...
                return;
            }
        }

    }

    public void onUnAssignedTask(ItemCancelTask itemCancelTask) {
        if (itemCancelTask == null) {
            return;
        }

        if (dialogDetailTaskFragment != null && dialogDetailTaskFragment.isVisible()) {
            dialogDetailTaskFragment.showDialogCancelTicket(itemCancelTask.content, itemCancelTask.data._id);
        } else {
            Toast.makeText(MainActivity.this, itemCancelTask.content + Conts.BLANK, Toast.LENGTH_SHORT).show();
        }

        for (ItemTask itemTask : arrItemTask) {
            if (TextUtils.equals(itemCancelTask.data._id, itemTask.getTaskResult()._id)) {
                arrItemTask.remove(itemTask);
                taskListAdapter.notiDataChange(arrItemTask);
                // TODO update các item khác nếu cần. ex: taskCount,...
                return;
            }
        }

    }

    public void showDialogAppointmentTask(ItemTask item) {
        DialogNotiTaskAppointment notiTaskAppointment = new DialogNotiTaskAppointment(MainActivity.this);
        notiTaskAppointment.setData(item);
        notiTaskAppointment.show();

        notiRingtoneTypeAndVibrator(RingtoneManager.TYPE_ALARM, 3000);
    }


    private void notiRingtoneTypeAndVibrator(int ringtoneType, int timeVibrate) {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        v.vibrate(timeVibrate);

        Uri notification = RingtoneManager.getDefaultUri(ringtoneType);
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        r.play();
    }

}
