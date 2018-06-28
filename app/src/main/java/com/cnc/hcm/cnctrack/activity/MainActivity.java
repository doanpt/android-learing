package com.cnc.hcm.cnctrack.activity;

import android.Manifest;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Address;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
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

import com.cnc.hcm.cnctrack.R;
import com.cnc.hcm.cnctrack.adapter.TaskListAdapter;
import com.cnc.hcm.cnctrack.api.ApiUtils;
import com.cnc.hcm.cnctrack.api.MHead;
import com.cnc.hcm.cnctrack.base.BaseActivity;
import com.cnc.hcm.cnctrack.customeview.MyRecyclerView;
import com.cnc.hcm.cnctrack.dialog.DialogGPSSetting;
import com.cnc.hcm.cnctrack.dialog.DialogNotiTaskAppointment;
import com.cnc.hcm.cnctrack.dialog.DialogNotification;
import com.cnc.hcm.cnctrack.dialog.DialogOptionFilter;
import com.cnc.hcm.cnctrack.fragment.DialogDetailTaskFragment;
import com.cnc.hcm.cnctrack.fragment.MonthViewFragment;
import com.cnc.hcm.cnctrack.fragment.YearsViewFragment;
import com.cnc.hcm.cnctrack.model.CommonAPICallBackResult;
import com.cnc.hcm.cnctrack.model.GetTaskListResult;
import com.cnc.hcm.cnctrack.model.ItemCancelTask;
import com.cnc.hcm.cnctrack.model.ItemMarkedMap;
import com.cnc.hcm.cnctrack.model.ItemTask;
import com.cnc.hcm.cnctrack.model.common.TaskListResult;
import com.cnc.hcm.cnctrack.service.GPSService;
import com.cnc.hcm.cnctrack.util.CommonMethod;
import com.cnc.hcm.cnctrack.util.Conts;
import com.cnc.hcm.cnctrack.util.SettingApp;
import com.cnc.hcm.cnctrack.util.UserInfo;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.analytics.FirebaseAnalytics;
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


public class MainActivity extends BaseActivity implements View.OnClickListener,
        OnMapReadyCallback, GoogleMap.OnMarkerClickListener, TaskListAdapter.OnItemWorkClickListener, BaseActivity.OnNetworkConnectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_CODE_LOCATION_UPDATE = 4234;
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 53;
    private GoogleMap mMap;
    private Marker searchMarked;

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
    private MyRecyclerView rcTask;

    private MonthViewFragment monthViewFragment;
    private YearsViewFragment yearsViewFragment;

    private Fragment[] listFrag;
    private GPSService gpsService;
    private DialogDetailTaskFragment dialogDetailTaskFragment;

    private DialogGPSSetting dialogGPSSetting;
    private DialogNotification dialogNotification;
    private TaskListAdapter taskListAdapter;

    private ArrayList<ItemTask> arrItemTask = new ArrayList<>();
    private ArrayList<ItemMarkedMap> arrMarkedTask = new ArrayList<>();
    private int quantityNewTask, quantityDoingTask, quantityCompletedTask;
    private boolean isMainActive;
    private String dateSelected = Conts.BLANK;
    private String startDate;
    private String endDate;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public void onViewReady(@Nullable Bundle savedInstanceState) {
        initObject();
        initViews();
        createFileBackup();
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
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isMainActive = true;
        setOnNetworkConnectedListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isMainActive = false;
        setOnNetworkConnectedListener(null);
    }

    private void checkExistIdTask() {
        int idTask = getIntent().getIntExtra(Conts.KEY_ID_TASK_TO_SHOW_DETAIL, -1);
        if (idTask != -1) {
            if (dialogDetailTaskFragment != null) {
                showTaskDetail(idTask + Conts.BLANK);
            }
        }
    }

    private void initObject() {
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        //READMI below code to using firebase analyze and report custom log to analyze
//        Bundle bundle = new Bundle();
//        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
//        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
//        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
//        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        //below code to report custom crash
//        FirebaseCrash.log("SQL database failed to initialize");
//        FirebaseCrash.log(user.getEmailAddress() + " purchased product " + product.getID());

        UserInfo.getInstance(this).setMainActivityActive(true);
        dialogGPSSetting = new DialogGPSSetting(this);
        dialogDetailTaskFragment = new DialogDetailTaskFragment();
        dialogDetailTaskFragment.setMainActivity(MainActivity.this);

        taskListAdapter = new TaskListAdapter(this);
        taskListAdapter.setOnItemWorkClickListener(this);

        dialogNotification = new DialogNotification(this);
        dialogNotification.setTitle(getString(R.string.error_occurred));
        dialogNotification.setTextBtnOK(getString(R.string.try_again));
        dialogNotification.setOnClickDialogNotificationListener(new DialogNotification.OnClickDialogNotificationListener() {
            @Override
            public void onClickButtonOK() {
                tryGetTaskList(UserInfo.getInstance(MainActivity.this).getAccessToken(), startDate, endDate);
            }

            @Override
            public void onClickButtonExit() {

            }
        });
    }

    private void initViews() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        findViewById(R.id.fab_search_map).setOnClickListener(this);

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
            setGpsService(gpsService);
            Log.d(TAG, "ServiceConnection at MainActivity");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            setGpsService(null);
            Log.d(TAG, "onServiceDisconnected at MainActivity");
        }
    };


    public void tryGetTaskList(String accessToken, final String startDate, final String endDate) {
        Log.v(TAG, "token_key:= " + accessToken);
        this.startDate = startDate;
        this.endDate = endDate;
        showProgressLoadding();
        final List<MHead> arrHeads = new ArrayList<>();
        arrHeads.add(new MHead(Conts.KEY_ACCESS_TOKEN, accessToken));
        arrHeads.add(new MHead(Conts.KEY_START_DATE, startDate));
        arrHeads.add(new MHead(Conts.KEY_END_DATE, endDate));
        ApiUtils.getAPIService(arrHeads).getTaskList().enqueue(new Callback<GetTaskListResult>() {
            @Override
            public void onResponse(Call<GetTaskListResult> call, Response<GetTaskListResult> response) {
                long statusCode = response.body().statusCode;
                Log.e(TAG, "tryGetTaskList.onResponse(), statusCode: " + statusCode);
                if (response.isSuccessful()) {
                    if (statusCode == Conts.RESPONSE_STATUS_OK) {
                        quantityNewTask = 0;
                        quantityDoingTask = 0;
                        quantityCompletedTask = 0;
                        clearData();
                        Log.e(TAG, "tryGetTaskList.onResponse(), --> response: " + response.toString());
                        GetTaskListResult getTaskListResult = response.body();
                        Log.e(TAG, "tryGetTaskList.onResponse(), --> getTaskListResult: " + getTaskListResult.toString());
                        arrItemTask.clear();
                        if (getTaskListResult != null) {
                            TaskListResult[] result = getTaskListResult.result;
                            if (result != null && result.length > 0) {
                                for (TaskListResult itemResult : result) {
                                    ItemTask itemTask = new ItemTask(itemResult);
                                    arrItemTask.add(itemTask);
                                    switch ((int) itemTask.getTaskResult().status.getId()) {
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
                        taskListAdapter.notiDataChange(arrItemTask);
                    } else if (statusCode == Conts.RESPONSE_STATUS_TOKEN_WRONG) {
                        showMessageRequestLogout();
                    }
                    dismisProgressLoading();
                } else {
                    CommonMethod.makeToast(MainActivity.this, response.toString());
                    dismisProgressLoading();
                }

            }

            @Override
            public void onFailure(Call<GetTaskListResult> call, Throwable t) {
                Log.e(TAG, "tryGetTaskList.onFailure() --> " + t);
                t.printStackTrace();
                dismisProgressLoading();
                if (dialogNotification != null) {
                    dialogNotification.setContentMessage("tryGetTaskList.onFailure()\n" + t.getMessage());
                    dialogNotification.show();
                }
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

    public void receiverNewTask(TaskListResult result) {

        String currenDate = CommonMethod.formatTimeStandand(CommonMethod.getInstanceCalendar().getTime());

        String appointmentDate = result.appointmentDate;
        Date appmentDate = CommonMethod.formatTimeFromServerToDate(appointmentDate);
        String appDate = CommonMethod.formatTimeStandand(appmentDate);

        if (currenDate.equals(appDate)) {
            quantityNewTask++;
            updateQuantityTask();
            arrItemTask.add(new ItemTask(result));
            if (taskListAdapter != null) {
                taskListAdapter.notiDataChange(arrItemTask);
            }

            addMarkedTask(result, BitmapDescriptorFactory.fromResource(R.drawable.ic_marked_task_new));
        }

        notiRingtoneTypeAndVibrator(RingtoneManager.TYPE_NOTIFICATION, 1500);

    }

    private void initMarkerOnMap() {
        for (ItemTask task : arrItemTask) {
            BitmapDescriptor bitmapDescriptor = null;
            switch ((int) task.getTaskResult().status.getId()) {
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
            TaskListResult result = task.getTaskResult();
            addMarkedTask(result, bitmapDescriptor);
        }
    }

    private void addMarkedTask(TaskListResult result, BitmapDescriptor bitmapDescriptor) {
        if (result.address != null) {
            if (result.address.getLocation() != null) {
                addMarkerMap(result._id, result.address.getLocation().getLatitude(),
                        result.address.getLocation().getLongitude(), result.address.getStreet(), bitmapDescriptor);
            } else {
                String locationName = result.address.getStreet();
                Address address = CommonMethod.getLocationFromLocationName(MainActivity.this, locationName);
                if (address != null) {
                    addMarkerMap(result._id, address.getLatitude(), address.getLongitude(), locationName, bitmapDescriptor);
                } else {
                    Log.e(TAG, "Error in addMarkerMap and task.getTaskResult().address = null");
                }
            }
        } else {
            if (result.customer != null && result.customer.address != null && result.customer.address.getLocation() != null) {
                addMarkerMap(result._id, result.customer.address.getLocation().getLatitude(),
                        result.customer.address.getLocation().getLongitude(), result.customer.address.getStreet(), bitmapDescriptor);
            } else {
                Log.e(TAG, "Error in addMarkerMap and task.getTaskResult().address CUSTOMER = null");
            }
        }
    }

    private Marker addMarkedOnMap(LatLng latLng, String title, String snippet) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(title);
        markerOptions.snippet(snippet);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        return mMap.addMarker(markerOptions);
    }

    private void callViewCalendar(int typeView) {
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
            case R.id.fab_search_map:
                actionSearchPlaceOnMap();
                break;
        }
    }

    private void actionSearchPlaceOnMap() {
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
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

    private void setCurrentItemViewPager(int position) {
        if (bottomSheetBehavior.getState() == ViewPagerBottomSheetBehavior.STATE_COLLAPSED) {
            bottomSheetBehavior.setState(ViewPagerBottomSheetBehavior.STATE_EXPANDED);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UserInfo.getInstance(this).setMainActivityActive(false);
        unbindService(serviceConnection);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ChangeTimeActivity.CODE_CHANGE_TIME:
                if (resultCode == RESULT_OK) {
                    int typeView = SettingApp.getInstance(this).getTypeView();
                    String accessToken = UserInfo.getInstance(getApplicationContext()).getAccessToken();

                    switch (typeView) {
                        case Conts.TYPE_VIEW_BY_MONTH:
                            if (dateSelected.equals(Conts.BLANK)) {
                                Calendar instance = CommonMethod.getInstanceCalendar();
                                String dateSelected = CommonMethod.formatDateToString(instance.getTime()) + Conts.FORMAT_TIME_FULL;
                                tryGetTaskList(accessToken, dateSelected, dateSelected);
                            } else {
                                tryGetTaskList(accessToken, dateSelected, dateSelected);
                            }
                            break;
                        case Conts.TYPE_VIEW_BY_YEARS:
                            tryGetTaskList(accessToken, startDate, endDate);

                            break;
                    }
                }
                break;
            case PLACE_AUTOCOMPLETE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Place place = PlaceAutocomplete.getPlace(this, data);
                    if (searchMarked != null) {
                        searchMarked.remove();
                    }
                    searchMarked = addMarkedOnMap(place.getLatLng(), "", place.getAddress().toString());
                    moveCameraMap(place.getLatLng());
                } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                    Status status = PlaceAutocomplete.getStatus(this, data);
                    Log.i(TAG, status.getStatusMessage());
                }
                break;
        }


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
    }

    private void addMarkerMap(String idTask, double latitude, double longitude, String addName, BitmapDescriptor bitmapDescriptor) {
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
            if (!dialogDetailTaskFragment.isExpaned()) {
                dialogDetailTaskFragment.setIdTask(idTask);
                dialogDetailTaskFragment.show(getSupportFragmentManager(), dialogDetailTaskFragment.getTag());
                dialogDetailTaskFragment.setExpaned(true);
            } else {
                dialogDetailTaskFragment.setIdTask(idTask);
                dialogDetailTaskFragment.loadInforTask();
            }
        }
    }

    private void updateStatusIsRead(String idTask, final int position) {
        String accessToken = UserInfo.getInstance(this).getAccessToken();
        List<MHead> arrHeads = new ArrayList<>();
        arrHeads.add(new MHead(Conts.KEY_ACCESS_TOKEN, accessToken));
        ApiUtils.getAPIService(arrHeads).updateStatusIsRead(idTask).enqueue(new Callback<CommonAPICallBackResult>() {
            @Override
            public void onResponse(Call<CommonAPICallBackResult> call, Response<CommonAPICallBackResult> response) {
                Log.e(TAG, "updateStatusIsRead.onResponse(), statusCode: " + response.code());
                Log.e(TAG, "updateStatusIsRead.onResponse(), --> response: " + response.toString());
                if (response.isSuccessful()) {
                    Long statusCode = response.body().getStatusCode();
                    int code = (int) statusCode.longValue();
                    switch (code) {
                        case Conts.RESPONSE_STATUS_OK:
                            CommonAPICallBackResult responseCNC = response.body();
                            if (responseCNC.getStatusCode() == Conts.RESPONSE_STATUS_OK) {
                                if (position > 0) {
                                    taskListAdapter.getItem(position).getTaskResult().isRead = true;
                                    taskListAdapter.notifyItemChanged(position);
                                }
                            }
                            break;
                        case Conts.RESPONSE_STATUS_TOKEN_WRONG:
                            showMessageRequestLogout();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<CommonAPICallBackResult> call, Throwable t) {
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

    private void callFragment(Fragment fragment) {
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (!marker.equals(searchMarked)) {
            String idTask = getIDMarked(marker);
            if (dialogDetailTaskFragment != null) {
                dialogDetailTaskFragment.setIdTask(idTask);
                dialogDetailTaskFragment.show(getSupportFragmentManager(), dialogDetailTaskFragment.getTag());
            }
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
            dialogDetailTaskFragment.showDialogCancelTicket(itemCancelTask.content);
        } else {
            Toast.makeText(MainActivity.this, itemCancelTask.content + Conts.BLANK, Toast.LENGTH_SHORT).show();
        }

        for (ItemTask itemTask : arrItemTask) {
            if (TextUtils.equals(itemCancelTask.data._id, itemTask.getTaskResult()._id)) {
                itemTask.getTaskResult().status.setId(Conts.TYPE_CANCEL_TASK);
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
            dialogDetailTaskFragment.showDialogUnAssignedTask();
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
        if (v != null)
            v.vibrate(timeVibrate);

        Uri notification = RingtoneManager.getDefaultUri(ringtoneType);
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        r.play();
    }

    public void setDateSelected(String dateSelected) {
        this.dateSelected = dateSelected;
    }

    @Override
    public void onNetworkConnected() {
        if (tvStatusNetwork != null) {
            tvStatusNetwork.setTextColor(getResources().getColor(R.color.color_text_status));
            tvStatusNetwork.setText(getResources().getString(R.string.On));
        }
    }

    @Override
    public void onNetworkDisconnect() {
        if (tvStatusNetwork != null) {
            tvStatusNetwork.setTextColor(getResources().getColor(android.R.color.darker_gray));
            tvStatusNetwork.setText(getResources().getString(R.string.Off));
        }
    }
}
